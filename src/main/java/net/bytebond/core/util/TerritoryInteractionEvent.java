package net.bytebond.core.util;


import de.tr7zw.nbtapi.plugin.NBTAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TerritoryInteractionEvent implements Listener {

    @Getter
    private static final TerritoryInteractionEvent instance = new TerritoryInteractionEvent();

    public Boolean isPlayerInNation(Player player) {
        NationYML nation = new NationYML(player.getUniqueId());
        if (nation.isSet("nationName")) {
            return true;
        }
        return false;
    }


    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (!ClaimRegistry.doesClaimExist(chunk)) {
            return;
        }

        NationYML chunkNation = ClaimRegistry.getNation(chunk);
        if (chunkNation == null) {
            return;
        }

        if (!isPlayerInNation(player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot break blocks here, this chunk is claimed.");
            return;
        }

        NationYML playerNation = new NationYML(player.getUniqueId());

        if (chunkNation.getString("nationName").equals(playerNation.getString("nationName"))) {
            return;
        }

        if (chunkNation.getStringList("allied_nations").contains(playerNation.getString("nationName"))) {
            if (chunkNation.getBoolean("allyPermissions")) {
                return;
            }
            event.setCancelled(true);
        }

        event.setCancelled(true);
        player.sendMessage("§cYou cannot break blocks here. This territory belongs to another nation.");
    }

    @EventHandler
    public void onBlockBuildEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (!ClaimRegistry.doesClaimExist(chunk)) {
            return;
        }

        NationYML chunkNation = ClaimRegistry.getNation(chunk);
        if (chunkNation == null) {
            return;
        }

        if (!isPlayerInNation(player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot build here, this chunk is claimed.");
            return;
        }

        NationYML playerNation = new NationYML(player.getUniqueId());

        if (chunkNation.getString("nationName").equals(playerNation.getString("nationName"))) {
            return;
        }

        if (chunkNation.getStringList("allied_nations").contains(playerNation.getString("nationName"))) {
            if (chunkNation.getBoolean("allyPermissions")) {
                return;
            }
            event.setCancelled(true);
        }

        event.setCancelled(true);
        player.sendMessage("§cYou cannot build here. This territory belongs to another nation.");
    }


    @EventHandler
    public void onBlockInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        // Check if the player is interacting with a block
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            // Check if the block is a chest
            if (block.getType() == Material.CHEST) {
                // Get the chest
                Chest chest = (Chest) block.getState();
                // Wrap it in an NBTItem
                NBTItem nbtItem = new NBTItem(new ItemStack(block.getType()));

                // Check the NBT data and handle the interaction accordingly
                if (nbtItem.hasKey("drill")) {
                    // Handle drill chest interaction
                } else if (nbtItem.hasKey("tradingchest") && !player.isSneaking()) {
                    // Handle trading chest interaction
                }
            }
        }
    }



}
