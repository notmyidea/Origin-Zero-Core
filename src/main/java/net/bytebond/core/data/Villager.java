package net.bytebond.core.data;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Random;

public class Villager {
    @Getter
    private NationYML nation;
    @Getter
    private Chunk chunk;
    private LivingEntity villagerEntity;
    private Block bedrock;


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

    public Villager(NationYML nation, Chunk chunk, Block bedrock) {
        this.nation = nation;
        this.chunk = chunk;
        this.bedrock = bedrock;
    }

    public void runVillagerSpawn() {
        Location bedrockLocation = bedrock.getLocation();
        Location spawnLocation = bedrockLocation.subtract(0, 0, 1);

        this.villagerEntity = (LivingEntity) chunk.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        this.villagerEntity.setAI(false);

        VillagerNames[] names = VillagerNames.values();
        String randomName = names[new Random().nextInt(names.length)].toString();
        String formattedName = randomName.substring(0, 1).toUpperCase() + randomName.substring(1).toLowerCase();
        this.villagerEntity.setCustomName(formattedName);

    }




    public String getVillagerName() {
        if (villagerEntity != null && villagerEntity.getCustomName() != null) {
            return villagerEntity.getCustomName();
        }
        return null;
    }


    public LivingEntity getVillager() {
        return villagerEntity;
    }

    public Villager getVillagerFromNBT(Block block) {
        if(block.hasMetadata("villager")) {
            return (Villager) block.getMetadata("villager").get(0).value();
        }
        return null;
    }



}
