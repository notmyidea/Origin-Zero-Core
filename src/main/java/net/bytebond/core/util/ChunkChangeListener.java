package net.bytebond.core.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import net.bytebond.core.data.NationYML;
import org.mineacademy.fo.Messenger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Listener for when a player moves from an unclaimed chunk to a claimed chunk.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChunkChangeListener implements Listener {

    /**
     * The singleton instance
     */
    @Getter
    private static final Listener instance = new ChunkChangeListener();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Check if the player has moved from one chunk to another
        if (event.getFrom().getChunk() != event.getTo().getChunk()) {
            String chunkStr = player.getLocation().getWorld().getName() + "," + player.getLocation().getChunk().getX() + "," + player.getLocation().getChunk().getZ();
            NationYML nation = NationYML.getNationByTerritory(chunkStr);

            // Check if the player has moved to a claimed chunk
            if (nation != null) {
                // Display a title to the player
                player.sendTitle(ChatColor.GREEN + "Welcome to " + nation.getString("nationName"), ChatColor.WHITE + nation.getString("nationDescription"), 10, 70, 20);
            }
        }
    }
}