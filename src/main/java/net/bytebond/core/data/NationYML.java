package net.bytebond.core.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.mineacademy.fo.settings.YamlConfig;

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
    private Boolean allyBuilding;


    // *************
    // * Economy
    // *************
    // Taxation (boo)
    private final Double taxRate; // 0.0 - 10.0
    // Population
    private final Integer population;

    private final Double vaultBalance;
    // Wood
    private final Double UEConWood;
    // Stone
    private final Double UEConStone;
    // Brick
    private final Double UEConBrick;
    // Darkstone
    private final Double UEConDarkstone;
    // Obsidian
    private final Double UEConObsidian;

    // Claimed territories in a List
    private List<Chunk> claimedTerritories;

    // get all .yml files in the nations folder
    public static Map<UUID, NationYML> getNations() {
        Map<UUID, NationYML> nations = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get("plugins/Core/data"))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".yml"))
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        UUID uuid = UUID.fromString(fileName.substring(0, fileName.length() - 4)); // remove .yml
                        NationYML nation = new NationYML(uuid);
                        nation.loadConfiguration(NO_DEFAULT, "data/" + uuid + ".yml");
                        nations.put(uuid, nation);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nations;
    }



    public NationYML(UUID owner) {
        this.setHeader("Nation Data\n\nThis file stores all the data about a nation.\n\nDo not modify this file manually, use /nation commands instead.\n\nNation");

        this.nationName = this.getString("nationName");
        this.owner = owner.toString();
        this.nationDescription = this.getString("nationDescription");
        this.tag = this.getString("tag");
        this.territory = this.getStringList("territory");




        // If the YAML file doesn't contain the data, set default values
        this.vaultBalance = this.getDouble("vaultBalance", 0.0);
        this.UEConWood = this.getDouble("UEConWood", 0.0);
        this.UEConStone = this.getDouble("UEConStone", 0.0);
        this.UEConBrick = this.getDouble("UEConBrick", 0.0);
        this.UEConDarkstone = this.getDouble("UEConDarkstone", 0.0);
        this.UEConObsidian = this.getDouble("UEConObsidian", 0.0);
        this.taxRate = this.getDouble("taxRate", 0.0);
        this.population = this.getInteger("population", 0);
        this.allyBuilding = this.getBoolean(("allyBuilding"), false);

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



}