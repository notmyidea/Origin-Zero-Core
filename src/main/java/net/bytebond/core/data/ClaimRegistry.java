package net.bytebond.core.data;

import com.gmail.nossr50.mcmmo.acf.annotation.Optional;
import net.bytebond.core.Core;
import net.bytebond.core.util.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ClaimRegistry extends YamlConfig {

    private static Integer debugging = 1;

    private static final String CLAIMS_DIRECTORY = "data/claims/";

    private final String chunk;
    private String latestClaimant;
    private String ownerUUID;
    private String ownerNationName;
    private List<String> history;

    public ClaimRegistry(Chunk chunk, UUID owner, String ownerNationName, String playerName) {
        this.chunk = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        if(latestClaimant == null) {
            latestClaimant = "None";
        } else {
            this.latestClaimant = playerName.toString();
        }
        this.ownerUUID = owner != null ? owner.toString() : null;
        this.ownerNationName = ownerNationName;
        this.history = new ArrayList<>();

        this.loadConfiguration(NO_DEFAULT, CLAIMS_DIRECTORY + this.chunk + ".yml");
    }

    public static boolean doesClaimExist(Chunk chunk) {
        String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        File claimFile = new File("plugins/Core/data/claims/" + chunkStr + ".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(claimFile);
        if (yaml.isSet("chunk")) return true;
        return false;
    }

    public void setOwner(UUID owner, String nationName) {
        this.ownerUUID = owner != null ? owner.toString() : null;
        this.ownerNationName = nationName;
        this.save();
    }

    public void setLatestClaimant(String playerName) {
        this.latestClaimant = playerName;
        this.save();
    }

    public void addHistory(String historyEntry) {
        this.history.add(historyEntry);
        this.save();
    }

    public void saveData() {
        this.set("chunk", this.chunk);
        this.set("latestClaimant", this.latestClaimant);
        this.set("ownerUUID", this.ownerUUID);
        this.set("ownerNationName", this.ownerNationName);
        this.set("history", this.history);

        super.save();
    }

    public static File getClaimFile(Chunk chunk) {
        if(doesClaimExist(chunk)) {
            String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
            return new File("plugins/Core/data/claims/" + chunkStr + ".yml");
        }
        return null;
    }

    public static NationYML getNation(Chunk chunk) {
        String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        for (NationYML nation : NationYML.getNations().values()) {
            if (nation.isSet("territory")) {
                if (nation.getStringList("territory").contains(chunkStr)) {
                    return nation;
                }
            }
        }
        return null;
    }

    public static String getOwnerOfChunk(Chunk chunk) {
        if (doesClaimExist(chunk)) {
            File claimFile = getClaimFile(chunk);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(claimFile);
            if (yaml.isSet("ownerUUID")) {
                return yaml.getString("ownerUUID");
            }
        }
        return null;
    }

    public static void transferChunkOwnership(Chunk chunk, NationYML nation, Boolean silent, @Optional String optionalHistoryMessage) throws IOException {
        if(!(doesClaimExist(chunk) || getOwnerOfChunk(chunk) == null)) {
            return;
        }

        ItemManager itemManager = new ItemManager();

        String newOwnerNationName = nation.getString("nationName");
        String newOwnerUUID = nation.getString("owner");

        File claimFile = getClaimFile(chunk);
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(claimFile);
        String chunkStr = yaml.getString("chunk");
        String oldNationName = yaml.getString("ownerNationName");
        String oldOwnerUUID = yaml.getString("ownerUUID");

        yaml.set("ownerUUID", newOwnerUUID);
        yaml.set("ownerNationName", newOwnerNationName);
        yaml.save(claimFile);

        if(optionalHistoryMessage != null) {
            yaml.getStringList("history").add(optionalHistoryMessage);
        }

        NationYML oldNation = new NationYML(UUID.fromString(oldOwnerUUID));
        oldNation.getStringList("territory").remove(chunkStr);
        oldNation.save();

        // Transfer housing objects
        List<Block> housingBlocks = itemManager.checkForHousingObjects(chunk);
        List<String> oldHousingList = oldNation.getStringList("housing");
        List<String> newHousingList = nation.getStringList("housing");

        for (Block block : housingBlocks) {
            String blockString = block.getX() + "," + block.getY() + "," + block.getZ();
            String chunkString = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ() + "," + blockString;

            oldHousingList.remove(chunkString);
            newHousingList.add(chunkString);
        }

        oldNation.set("housing", oldHousingList);
        oldNation.save();

        nation.set("housing", newHousingList);
        nation.save();

        // Transfer drills


        // Transfer defence blocks

        Core.getInstance().debugLog("Ownership of chunk " + chunkStr + " has been transferred from " + oldNationName + " to " + newOwnerNationName);

        if(silent) {
            return;
        }
        Player oldOwner = Bukkit.getOnlinePlayers().stream().filter(player -> player.getUniqueId().toString().equals(oldOwnerUUID)).findFirst().orElse(null);
        Player newOwner = Bukkit.getOnlinePlayers().stream().filter(player -> player.getUniqueId().toString().equals(newOwnerUUID)).findFirst().orElse(null);

        if(oldOwner != null) {
            //Common.tellNoPrefix(oldOwner, "&cYour claim at " + chunkStr + " has been transferred to " + newOwnerNationName);
        }

        if(newOwner != null) {
            Common.tellNoPrefix(newOwner, "&fThe claim at &7" + chunkStr + "&f has been transferred  from &7" + oldNationName + "&f to your Nation.");
        }

    }



}