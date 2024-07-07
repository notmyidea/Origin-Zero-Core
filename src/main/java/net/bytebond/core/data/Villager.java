package net.bytebond.core.data;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Villager {
    @Getter
    private NationYML nation;
    @Getter
    private Chunk chunk;
    private LivingEntity villagerEntity;


    public enum VillagerNames {
        HERBERT,
        WILHELM,
        ALFRED,
        BERTRAM,
        CECIL,
        DOUGLAS,
        EDMUND,
        FREDERICK,
        GILBERT,
        HAROLD,
        IVAN,
        JASPER,
        KENNETH,
        LEONARD,
        MORTIMER,
        NIGEL,
        OSWALD,
        PERCIVAL,
        QUENTIN,
        REGINALD,
        STANLEY,
        THEODORE,
        UPTON,
        VICTOR,
        WALTER,
        XAVIER,
        YORICK,
        ZACHARY,
        ARTHUR,
        BENJAMIN,
        CHARLES,
        DAVID,
        EDWARD,
        FRANCIS,
        GEORGE,
        HENRY,
        ISAAC,
        JONATHAN,
        KEVIN,
        LOUIS,
        MARTIN,
        NICHOLAS,
        OLIVER,
        PETER,
        RICHARD,
        SAMUEL,
        THOMAS,
        ULYSSES,
        VINCENT
    }

    public Villager(NationYML nation, Chunk chunk) {
        this.nation = nation;
        this.chunk = chunk;
    }

    public void spawnVillager() {
        // Spawn a villager at the chunk
        int x = chunk.getX() * 16 + 8;
        int z = chunk.getZ() * 16 + 8;
        int y = chunk.getWorld().getHighestBlockYAt(x, z) + 1;
        Location spawnLocation = new Location(chunk.getWorld(), x, y, z);
        this.villagerEntity = (LivingEntity) chunk.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        this.villagerEntity.setAI(false);

        VillagerNames[] names = VillagerNames.values();
        String randomName = names[new Random().nextInt(names.length)].toString();
        String formattedName = randomName.substring(0, 1).toUpperCase() + randomName.substring(1).toLowerCase();
        this.villagerEntity.setCustomName(formattedName);

    }

    public boolean isAlive() {
        return this.villagerEntity != null && !this.villagerEntity.isDead();
    }

    public String getVillagerName() {
        if (villagerEntity != null && villagerEntity.getCustomName() != null) {
            return villagerEntity.getCustomName();
        }
        return null;
    }

    public static Villager findVillager(Chunk chunk, NationYML nation) {
        if(nation.getStringList("villagers").isEmpty()) {
            return null;
        }
        if(!ClaimRegistry.doesClaimExist(chunk)) {
            return null;
        }
        List<String> villagersList = nation.getStringList("villagers");
        for(String villagerString : villagersList) {
            String[] split = villagerString.split(",");
            String villagerName = split[0];
            String worldName = split[1];
            //String villagerLocationString = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
            //String villagerLocationStringWithName = villager.getVillagerName() + "," + villagerLocationString;
            int chunkX = Integer.parseInt(split[2]);
            int chunkZ = Integer.parseInt(split[3]);
            if(worldName.equals(chunk.getWorld().getName()) && chunk.getX() == chunkX && chunk.getZ() == chunkZ) {
                // search chunk for all villagers and compare names
                for(LivingEntity entity : chunk.getWorld().getLivingEntities()) {
                    if(entity.getCustomName() != null && entity.getCustomName().equals(villagerName)) {
                        return new Villager(nation, chunk);
                    }
                }

            }

        }
        return null;
    }

    public void removeVillagerInChunk(Chunk chunk, NationYML nation) {
        List<String> villagersList = nation.getStringList("villagers");
        Iterator<String> iterator = villagersList.iterator();

        while (iterator.hasNext()) {
            String villagerString = iterator.next();
            String[] split = villagerString.split(",");
            String villagerName = split[0];
            String worldName = split[1];
            int chunkX = Integer.parseInt(split[2]);
            int chunkZ = Integer.parseInt(split[3]);

            if (worldName.equals(chunk.getWorld().getName()) && chunkX == chunk.getX() && chunkZ == chunk.getZ()) {
                for (org.bukkit.entity.Entity entity : chunk.getEntities()) {
                    if (entity.getType() == EntityType.VILLAGER && entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(villagerName)) {
                        entity.remove();
                        iterator.remove();
                        nation.set("villagers", villagersList);
                        nation.save();
                    }
                }
            }
        }
    }


    public org.bukkit.entity.Villager getVillagerWithNameFromEnum() {
        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity.getType() == EntityType.VILLAGER) {
                org.bukkit.entity.Villager villager = (org.bukkit.entity.Villager) entity;
                if (villager.getCustomName() != null && isNameInEnum(villager.getCustomName())) {
                    return villager;
                }
            }
        }
        return null;
    }

    public boolean isNameInEnum(String name) {
        for (VillagerNames villagerName : VillagerNames.values()) {
            if (villagerName.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }


}
