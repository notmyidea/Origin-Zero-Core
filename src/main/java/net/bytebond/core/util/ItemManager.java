package net.bytebond.core.util;

import net.bytebond.core.Core;
import net.bytebond.core.settings.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        lore.add(ChatColor.WHITE + "And only " + ChatColor.GRAY + Config.Housing.max_housing_per_chunk + ChatColor.WHITE + " per chunk.");
        lore.add("");
        lore.add(ChatColor.WHITE + "It will by time spawn villagers.");
        lore.add(ChatColor.WHITE + "Higher " + ChatColor.GREEN + "happiness " + ChatColor.WHITE + "means more villagers. (§aPull§f- and §cPush§f-System)");
        lore.add(ChatColor.WHITE + "Losing the chunk in a war will give the block to the new owner.");
        meta.setLore(lore);
        meta.setCustomModelData(22222); // Set a unique identifier for the housing block

        housingBlock.setItemMeta(meta);

        player.getInventory().addItem(housingBlock);
        if(!silent) {
            Common.tellNoPrefix(player, "&fYou have been given a Housing Block");
        }
        Core.getInstance().debugLog("Housing Block given to " + playerName);
    }




}