package net.bytebond.core.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.*;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnterChunkTerritoryEvent implements Listener {


    @Getter
    private static final EnterChunkTerritoryEvent instance = new EnterChunkTerritoryEvent();

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Check if the player has moved from one chunk to another
        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            Chunk newChunk = event.getTo().getChunk();
            Chunk oldChunk = event.getFrom().getChunk();

            if (Config.General.debugging) {
                System.out.println("!!!Player moved a chunk");
            }

            // Get the nation for the new chunk
            NationYML newNation = ClaimRegistry.getNation(newChunk);
            NationYML oldNation = ClaimRegistry.getNation(oldChunk);

            // Check if the player is moving from a chunk claimed by the same faction
            if (oldNation != null && newNation != null && oldNation.getFileName().equals(newNation.getFileName())) {
                return;
            }

            // Check if the player is moving from a wilderness chunk to another wilderness chunk
            if (oldNation == null && newNation == null) {
                return;
            }

            if (newNation == null) {
                player.sendTitle("Welcome to Wilderness", "", 10, 20, 20);
                return;
            }

            if (newNation.getFileName().equals("null")) {
                player.sendTitle("Welcome to Wilderness", "", 10, 20, 20);
                return;
            }

            if (newNation.getStringList("territory").contains(newChunk.getWorld().getName() + "," + newChunk.getX() + "," + newChunk.getZ())) {
                if (Config.General.debugging) {
                    System.out.println("Player is in a claimed chunk");
                }
                // Check if the nation still exists
                if (newNation.getString("nationName") != null) {
                    // Display a title to the player
                    player.sendTitle("Welcome to §7" + newNation.getString("nationName") + "§f", "§7" + newNation.getString("nationDescription").replace("&", "§"), 10, 20, 20);
                }
            } else {
                player.sendTitle("Welcome to Wilderness", "", 10, 20, 20);
            }
        }
    }




}
