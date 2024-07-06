package net.bytebond.core.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Getter
public final class NationYML extends YamlConfig {

    private static final Map<UUID, NationYML> nations = new HashMap<>();

    // Name, Owner and Description
    private String nationName;
    private String nationHash;

    private String owner;
    private String nationDescription;
    private String tag;
    // Ntion Diplomatic announcements
    private List<String> nationAnnouncements;
    // List of alliances by nationName // Cannot declare war on these
    private List<String> alliancesByName;
    // List of enemies by nationName // Can declare war on these
    private List<String> enemiesByName;

    // *************
    // * Special (needs) Items
    // *************
    // List of all drills in posession
    private List<String> drills;
    private Integer Beds;
    private Integer Soldiers;


    // *************
    // * Territory
    // *************
    private Integer minTerritory;
    private Integer maxTerritory;
    private List<String> territory;


    // *************
    // * Allies
    // *************
    private Boolean allyPermissions;
    private List<String> allied_nations;
    private List<String> pending_allied_nations;
    private List<String> enemy_nations;


    //*************
    // * War
    // *************
    private Integer warscore;
    private Integer warswon;
    private Integer warslost;
    private Integer troops;


    // *************
    // * Archive
    // *************
    private List<String> archive_messages;


    // *************
    // * Economy
    // *************
    // Taxation (boo)
    private final Integer taxRate; // 0.0 - 10.0
    // Population
    private final List<String> housing;

    private final Integer population;

    private final Integer treasury;
    // Wood
    private final Integer wood;
    // Stone
    private final Integer stone;
    // Brick
    private final Integer brick;
    // Darkstone
    private final Integer darkstone;
    // Obsidian
    private final Integer obsidian;

    // Claimed territories in a List
    private List<Chunk> claimedTerritories;

    public void checkDataFolder() {
    // 24/05, getting tired from the errors
    // check the data folder exists
        Path dataFolder = Paths.get("plugins/Core/data");
        if (Files.notExists(dataFolder)) {
            try {
                Files.createDirectory(dataFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // get all .yml files in the nations folder
    public static Map<UUID, NationYML> getNations() {
        Map<UUID, NationYML> nations = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get("plugins/Core/data"))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".yml"))
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        String uuidStr = fileName.substring(0, fileName.length() - 4); // remove .yml
                        if (uuidStr.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) {
                            UUID uuid = UUID.fromString(uuidStr);
                            NationYML nation = new NationYML(uuid);
                            nation.loadConfiguration(NO_DEFAULT, "data/" + uuid + ".yml");
                            nations.put(uuid, nation);
                        }
                    });
        } catch (IOException e) {
            // Spam on server start
        }
        return nations;
    }



    public NationYML(UUID owner) {



        this.setHeader("Nation Data\n\nThis file stores all the data about a nation.\n\nDo not modify this file manually, use /nation commands instead.\n\nNation");

        this.nationName = this.getString("nationName");
        this.nationHash = this.getString("nationHash");
        this.owner = owner.toString();
        this.nationDescription = this.getString("nationDescription");
        this.tag = this.getString("tag");
        this.territory = this.getStringList("territory");
        this.allyPermissions = this.getBoolean("allyPermissions", false);



        // If the YAML file doesn't contain the data, set default values
        this.treasury = this.getInteger("treasury", 1000);
        this.wood = this.getInteger("wood", 1000);
        this.stone = this.getInteger("stone", 1000);
        this.brick = this.getInteger("brick", 1000);
        this.darkstone = this.getInteger("darkstone", 1000);
        this.obsidian = this.getInteger("obsidian", 1000);
        this.taxRate = this.getInteger("taxRate", 10);
        this.housing = this.getStringList("housing");
        this.population = this.getInteger("population", 0);
        this.archive_messages = this.getStringList("archive_messages");


        this.allied_nations = this.getStringList("allied_nations");
        this.pending_allied_nations = this.getStringList("pending_allied_nations");
        this.enemy_nations = this.getStringList("enemy_nations");
        this.warscore = this.getInteger("warscore", 0);
        this.warswon = this.getInteger("warswon", 0);
        this.warslost = this.getInteger("warslost", 0);
        this.troops = this.getInteger("troops", 0);

        this.drills = this.getStringList("drills");
        // Load the configuration from the YAML file
        this.loadConfiguration(NO_DEFAULT, "data/" + owner + ".yml");

        this.save();
    }

    public List<Chunk> getTerritoryChunks() {
        List<Chunk> chunks = new ArrayList<>();
        for (String chunkStr : territory) {
            String[] parts = chunkStr.split(",");
            String worldName = parts[0];
            int x = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            Chunk chunk = Bukkit.getWorld(worldName).getChunkAt(x, z);
            chunks.add(chunk);
        }
        return chunks;
    }

    public void addTerritoryChunk(Chunk chunk) {
        String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        territory.add(chunkStr);
        this.save();
    }

    public void removeTerritoryChunk(Chunk chunk) {
        String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        territory.remove(chunkStr);
        this.save();
    }

    public static Boolean doesNationExist(String nationName) {
        for (NationYML nation : NationYML.getNations().values()) {
            if (nation.getString("nationName").equals(nationName)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean doesPlayerHaveANation(UUID playerUUID) {
        for (NationYML nation : NationYML.getNations().values()) {
            if (nation.getString("owner").equals(playerUUID.toString())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static List<NationYML> getNationsByName(String nationName) {
        List<NationYML> matchingNations = new ArrayList<>();
        for (NationYML nation : NationYML.getNations().values()) {
            if (nation.getString("nationName").equals(nationName)) {
                matchingNations.add(nation);
            }
        }
        if (matchingNations.isEmpty()) {
            return null;
        }
        return matchingNations;
    }

}