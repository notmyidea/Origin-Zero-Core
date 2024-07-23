package net.bytebond.core.util;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.bytebond.core.Core;
import net.bytebond.core.data.Drill;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Drills;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static void giveHousingObject(Player player, Boolean silent) {
        String playerName = player.getName();

        ItemStack housingBlock = new ItemStack(Material.CHEST);
        ItemMeta meta = housingBlock.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Housing Block (Placeable) (" + playerName + ")");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Place this block to create a housing object.");
        lore.add(ChatColor.WHITE + "You can only place " + ChatColor.GRAY + Config.Housing.max_houses + ChatColor.WHITE + " housing objects.");
        lore.add(ChatColor.WHITE + "Each chunk can contain a maximum of " + ChatColor.GRAY + Config.Housing.max_housing_per_chunk + ChatColor.WHITE + " housing objects.");
        lore.add("");
        lore.add(ChatColor.WHITE + "Over time, villagers will naturally spawn.");
        lore.add(ChatColor.WHITE + "Higher " + ChatColor.GREEN + "happiness" + ChatColor.WHITE + " results in more villagers (§aPull§f- and §cPush§f-System).");
        lore.add(ChatColor.WHITE + "If the chunk is lost in a war,");
        lore.add(ChatColor.WHITE + "ownership of the block will transfer to the new owner.");
        meta.setLore(lore);
        meta.setCustomModelData(22222); // Set a unique identifier for the housing block

        housingBlock.setItemMeta(meta);

        player.getInventory().addItem(housingBlock);
        if(!silent) {
            Common.tellNoPrefix(player, "" + ChatColor.WHITE + "You have been given a Housing Block");
        }
        Core.getInstance().debugLog("Housing Block given to " + playerName);
    }

    public List<Block> checkForHousingObjects(Chunk chunk) {
        List<Block> housingBlocks = new ArrayList<>();

        for (BlockState state : chunk.getTileEntities()) {
            if (state instanceof Sign) {
                NBTBlock nbtBlock = new NBTBlock(state.getBlock());
                NBTCompound compound = nbtBlock.getData();

                if (compound.hasKey("isHousingSign") && compound.getInteger("isHousingSign") == 1) {
                    Block blockUnder = state.getBlock().getRelative(BlockFace.DOWN);
                    if (blockUnder.getType() == Material.BEDROCK) {
                        housingBlocks.add(blockUnder);
                    }
                }
            }
        }

        return housingBlocks;
    }

    public static void giveDrill(Player player, Drill.DrillType drillType, Boolean silent) {
        String playerName = player.getName();

        ItemStack drillBlock = new ItemStack(Material.CHEST);
        ItemMeta meta = drillBlock.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Drill " + ChatColor.GRAY + "(" + ChatColor.GOLD + playerName + ChatColor.GRAY + ")");
        List<String> lore = new ArrayList<>();

        if(drillType == Drill.DrillType.WOOD) {
            lore.add(ChatColor.WHITE + "This is the " + ChatColor.GRAY + "WOOD" + ChatColor.WHITE + " drill.");
            lore.add(ChatColor.WHITE + "You can only place the drill a specific chunk " + ChatColor.RED + "!");
            lore.add("");
            lore.add(ChatColor.WHITE + "Over time, your drill will generate " + ChatColor.GRAY  + Drills.Drill.Wood.rate_per_hour.toString() +  ChatColor.WHITE + " Wood per hour.");
            meta.setCustomModelData(22223);
        }
        if(drillType == Drill.DrillType.STONE) {
            lore.add(ChatColor.WHITE + "This is the " + ChatColor.GRAY + "STONE" + ChatColor.WHITE + " drill.");
            lore.add(ChatColor.WHITE + "You can only place the drill a specific chunk " + ChatColor.RED + "!");
            lore.add("");
            lore.add(ChatColor.WHITE + "Over time, your drill will generate " + ChatColor.GRAY + Drills.Drill.Stone.rate_per_hour.toString() +  ChatColor.WHITE + " Stone per hour.");
            meta.setCustomModelData(22224);
        }
        if(drillType == Drill.DrillType.BRICK) {
            lore.add(ChatColor.WHITE + "This is the " + ChatColor.GRAY + "BRICK" + ChatColor.WHITE + " drill.");
            lore.add(ChatColor.WHITE + "You can only place the drill a specific chunk " + ChatColor.RED + "!");
            lore.add("");
            lore.add(ChatColor.WHITE + "Over time, your drill will generate " + ChatColor.GRAY  + Drills.Drill.Brick.rate_per_hour.toString() +  ChatColor.WHITE + " Brick per hour.");
            meta.setCustomModelData(22225);
        }
        if(drillType == Drill.DrillType.DARKSTONE) {
            lore.add(ChatColor.WHITE + "This is the " + ChatColor.GRAY + "DARKSTONE" + ChatColor.WHITE + " drill.");
            lore.add(ChatColor.WHITE + "You can only place the drill a specific chunk " + ChatColor.RED + "!");
            lore.add("");
            lore.add(ChatColor.WHITE + "Over time, your drill will generate " + ChatColor.GRAY  + Drills.Drill.Darkstone.rate_per_hour.toString() +  ChatColor.WHITE + " Darkstone per hour.");
            meta.setCustomModelData(22226);
        }
        if(drillType == Drill.DrillType.OBSIDIAN) {
            lore.add(ChatColor.WHITE + "This is the " + ChatColor.GRAY + "OBSIDIAN" + ChatColor.WHITE + " drill.");
            lore.add(ChatColor.WHITE + "You can only place the drill a specific chunk " + ChatColor.RED + "!");
            lore.add("");
            lore.add(ChatColor.WHITE + "Over time, your drill will generate " + ChatColor.GRAY  + Drills.Drill.Obsidian.rate_per_hour.toString() +  ChatColor.WHITE + " Obsidian per hour.");
            meta.setCustomModelData(22227);
        }
        lore.add(ChatColor.WHITE + "If the chunk is lost in a war: ownership of");
        lore.add(ChatColor.WHITE + "the drill will transfer to the new owner.");
        lore.add("");
        lore.add(ChatColor.GRAY + "Right-click" + ChatColor.WHITE + " to place the drill.");
        Common.colorize(lore);
        meta.setLore(lore);

        drillBlock.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(drillBlock);
        nbtItem.setBoolean("isDrill", true);
        nbtItem.setString("drillType", drillType.toString());

        player.getInventory().addItem(drillBlock);
        if(!silent) {
            Common.tellNoPrefix(player, "" + ChatColor.WHITE + "You have been given a Drill Object");
        }
        Core.getInstance().debugLog("Drill Object given to " + playerName);
    }


    }
