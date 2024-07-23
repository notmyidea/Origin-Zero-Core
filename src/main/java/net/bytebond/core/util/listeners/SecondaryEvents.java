package net.bytebond.core.util.listeners;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.Core;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.Drill;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.util.ItemManager;
import net.bytebond.core.util.TerritoryInteractionEvent;
import net.bytebond.core.util.handler.DrillHandling;
import net.bytebond.core.util.handler.HousingHandler;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Objects;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecondaryEvents implements Listener {

    @Getter
    private static final SecondaryEvents instance = new SecondaryEvents();

    /*
     *  SecondaryEvents
     *  Handling the Secondary Events
     *
     *  Comes AFTER the regular Events, priority = lowest
     *  Check if a placed block belongs to an object and is
     *  subject to special events and behaviros
     */


    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        NationYML nation = new NationYML(player.getUniqueId());
        Boolean inNation = nation.getStringList("territory").contains(chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ());

        if(event.getBlock().getType() != Material.CHEST) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        NBTItem nbtItem = new NBTItem(itemInHand);
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if(!inNation && itemMeta != null && itemMeta.hasCustomModelData()) {
            event.setCancelled(true);
            Common.tellTimedNoPrefix(5, player, "&fYou can't place this block outside of your territory.");
            return;
        }

        if(itemMeta != null && itemMeta.hasCustomModelData()) {
            DrillHandling drillHandling = new DrillHandling();
            NationPlayer nationPlayer = new NationPlayer(player);
        switch(itemMeta.getCustomModelData()) {
            case 22222:
                // check if there is a housing block in that chunk already and the amount of housing blocks in that chunk exceeds the Config value
                if (Objects.equals(ClaimRegistry.getOwnerOfChunk(chunk), nationPlayer.getNation().getString("owner"))) {

                    if (TerritoryInteractionEvent.getInstance().checkForHousingBlockInChunk(chunk, nation)) {
                        event.setCancelled(true);
                        Common.tellTimedNoPrefix(5, player, "You already reached the maximum amount of housing blocks in this chunk!");
                        //Common.tellNoPrefix(player, "There is already a housing block in this chunk!");
                        return;
                    }
                }

                Core.getInstance().debugLog("Housing Block placed");
                HousingHandler housingHandler = new HousingHandler();
                housingHandler.runHousingHandler(player, chunk, event.getBlock(), nation);

                break;
            case 22223:
                // Wood-Drill
                //ItemManager.giveDrill(player, Drill.DrillType.WOOD, true);
                drillHandling.newDrill(chunk, event.getBlock().getLocation(), Drill.DrillType.WOOD, nation);
                break;
            case 22224:
                // Stone-Drill
                //ItemManager.giveDrill(player, Drill.DrillType.STONE, true);
                drillHandling.newDrill(chunk, event.getBlock().getLocation(), Drill.DrillType.STONE, nation);
                break;
            case 22225:
                // Brick-Drill
                //ItemManager.giveDrill(player, Drill.DrillType.BRICK, true);
                drillHandling.newDrill(chunk, event.getBlock().getLocation(), Drill.DrillType.BRICK, nation);
                break;
            case 22226:
                // Darkstone-Drill
                //ItemManager.giveDrill(player, Drill.DrillType.DARKSTONE, true);
                drillHandling.newDrill(chunk, event.getBlock().getLocation(), Drill.DrillType.DARKSTONE, nation);
                break;
            case 22227:
                // Obsidian-Drill
                //ItemManager.giveDrill(player, Drill.DrillType.OBSIDIAN, true);
                drillHandling.newDrill(chunk, event.getBlock().getLocation(), Drill.DrillType.OBSIDIAN, nation);
                break;
        }
        }


    }
}