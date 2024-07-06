package net.bytebond.core.data;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

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


}
