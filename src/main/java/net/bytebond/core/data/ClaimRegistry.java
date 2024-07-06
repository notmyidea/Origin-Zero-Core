package net.bytebond.core.data;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
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



}