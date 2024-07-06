package net.bytebond.core.util;

import net.bytebond.core.data.Villager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class EntityMoveListener implements Listener {
    private final Map<UUID, Chunk> villagerChunks = new HashMap<>();
    private final Random random = new Random();
/*
    @EventHandler
    public void onVillagerSpawn(VillagerSpawnEvent event) {
        Villager villager = event.getVillager();
        villagerChunks.put(villager.getUniqueId(), villager.getLocation().getChunk());

        // Make the villager walk around within the chunk
        walkAround(villager);
    }

    private void walkAround(Villager villager) {
        Chunk chunk = villagerChunks.get(villager.getUniqueId());
        if (chunk != null) {
            // Generate a random location within the chunk
            int x = chunk.getX() * 16 + random.nextInt(16);
            int z = chunk.getZ() * 16 + random.nextInt(16);
            int y = chunk.getWorld().getHighestBlockYAt(x, z) + 1;
            Location targetLocation = new Location(chunk.getWorld(), x, y, z);

            // Make the villager walk to the target location
            PathfindingUtil.walkToLocation(villager.getVillagerEntity(), targetLocation, 1.0, entity -> {
                // When the villager reaches the target location, make it walk to another random location
                walkAround(villager);
            });
        }
   } */
}