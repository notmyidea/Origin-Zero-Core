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
            Common.tellNoPrefix(player, "&fYou have been given a Housing Block");
        }
        Core.getInstance().debugLog("Housing Block given to " + playerName);
    }




}