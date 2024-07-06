package net.bytebond.core.commands.adminsubcommands;

import com.comphenix.net.bytebuddy.implementation.bytecode.Throw;
import net.bytebond.core.Core;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Drills;
import net.bytebond.core.settings.Messages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.nbt.ReadableItemNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.bukkit.Material.CHEST;

public class AdminNationManager extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public AdminNationManager(SimpleCommandGroup parent) {
        super(parent, "admin");
        setPermission("bytebond.command.admin");
        setDescription("Admin nation manager");
        setUsage("/nation admin <delete/rename/list/removechunk/endwar/givetroop/give> <nation/mob/block>");
    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);
        checkPerm("nations.command.adminsubcommands.adminnationmanager");

        if (args.length < 1) {
            tellWarn("&fYou must specify a subcommand. &7/nation admin <delete/rename/list/removechunk/endwar/givetroop/give> <nation/mob/block>");
            return;
        }

        String firstArg = args[0];

        switch (firstArg) {
            case "delete":
                // Delete a nation
                if (args.length != 2) {
                    tellWarn("&fYou must specify a nation to delete. &7/nation admin delete <nation>");
                    return;
                }
                String secondArgDeletion = args[1];
                Map<UUID, NationYML> nations = NationYML.getNations();
                NationYML nationToDelete = null;
                for (NationYML nation1 : nations.values()) {
                    if (nation1.getString("nationName").equals(secondArgDeletion)) {
                        nationToDelete = nation1;
                        break;
                    }
                }
                if (nationToDelete != null) {
                    nationToDelete.deleteFile();
                    tellSuccess("&rSuccessfully deleted the nation: &7" + secondArgDeletion);
                } else {
                    tellWarn("&rNo nation found with the name: &7" + secondArgDeletion);
                }
                break;
            case "rename":
                // rename a nation, requires 3 arguments
                //checkArgs(3, "&fYou must specify a nation to rename and a new name. &7/nation admin rename <nation> <newname>");
                String nationName = args[1];
                String newName = args[2];
                // /nation admin rename <nation> <newname>
                //                 0       1         2     = 3
                Map<UUID, NationYML> nations1 = NationYML.getNations();
                NationYML nationToRename = null;
                for (NationYML nation2 : nations1.values()) {
                    if (nation2.getString("nationName").equals(nationName)) {
                        nationToRename = nation2;
                        break;
                    }
                }
                if (nationToRename == null) {
                    tellWarn("&rNo nation found with the name: &7" + nationName);
                    return;
                }
                nationToRename.set("nationName", newName);
                nationToRename.save();
                tellSuccess("&rSuccessfully renamed the nation: &7" + nationName + "&r to &7" + newName);
                return;
            case "removechunk":
                // get the chunk the admin stays on
                Chunk chunk = player.getLocation().getChunk();
                // is the chunk registered in ClaimRegistry?
                if (ClaimRegistry.doesClaimExist(chunk)) {
                    NationYML nationToRemoveChunk = ClaimRegistry.getNation(chunk);
                    List<String> territory = nationToRemoveChunk.getStringList("territory");
                    String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
                    territory.remove(chunkStr);
                    nationToRemoveChunk.set("territory", territory);
                    nationToRemoveChunk.save();
                    ClaimRegistry.getClaimFile(chunk).delete();
                } else {
                    tellWarn("&rThere are no records of this chunk in the ClaimRegistry");
                    return;
                }
                tellSuccess("&rSuccessfully removed the chunk from the ClaimRegistry");
                break;
            case "endwar":
                tellWarn("unimplemented");
                break;
            case "givetroop":
                tellWarn("unimplemented");
                break;
            case "give":

                if (args.length < 2) {
                    tellInfo("Usage: /nation admin give <housing, drill, wood, stone, brick, ...> <resource/player> <amount>");
                }
                String secondArg = args[1];
                switch (secondArg) {
                    case "housing":
                        if (args.length < 3) {
                            tellInfo("Usage: /nation admin give housing <player>");
                            return;
                        }
                        String playerName = args[2];
                        Player targetPlayer = Bukkit.getPlayer(playerName);

                        if (targetPlayer == null) {
                            tellWarn("Player not found: " + playerName);
                            return;
                        }

                        // Create a new ItemStack for the chest
                        ItemStack housingBlock = new ItemStack(Material.CHEST);

                        // Get the ItemMeta of the ItemStack
                        ItemMeta meta = housingBlock.getItemMeta();

                        // Set the display name and lore of the item
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


                        // Add a hidden enchantment to make the item glow
                        //meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
                        //.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                        // Apply the modified ItemMeta to the ItemStack
                        housingBlock.setItemMeta(meta);

                        // Add the custom item to the player's inventory
                        targetPlayer.getInventory().addItem(housingBlock);

                        Common.tellNoPrefix(targetPlayer, "&fYou have been given a Housing Block by an admin.");

                        tellSuccess("Given Housing Block to " + playerName);
                        break;
                    case "drill":
                        if (args.length < 4) {
                            tellInfo("Usage: /nation admin give drill <material> <player>");
                            return;
                        }
                        String material = args[2];
                        String drillPlayerName = args[3];

                        if (!material.equals("wood") && !material.equals("stone") && !material.equals("brick") && !material.equals("darkstone")) {
                            tellWarn("Invalid material: " + material);
                            return;
                        }

                        Player drillTargetPlayer = Bukkit.getPlayer(args[3]);
                        if (drillTargetPlayer == null) {
                            tellWarn("Player not found: " + args[3]);
                            return;
                        }

                        int modelData = 0;
                        String drillType = "none";
                        String Location = null;
                        switch (material) {
                            case "wood":
                                modelData = 11112;
                                drillType = "wood";
                                Location = Drills.Drill.Wood.position.toString();
                                break;
                            case "stone":
                                modelData = 11113;
                                drillType = "stone";
                                Location = Drills.Drill.Stone.position.toString();
                                break;
                            case "brick":
                                modelData = 11114;
                                drillType = "brick";
                                Location = Drills.Drill.Brick.position.toString();
                                break;
                            case "obsidian":
                                modelData = 11115;
                                drillType = "obsidian";
                                Location = Drills.Drill.Obsidian.position.toString();
                                break;
                            case "darkstone":
                                modelData = 11116;
                                drillType = "darkstone";
                                Location = Drills.Drill.Darkstone.position.toString();
                            default:
                                throw new IllegalArgumentException("Invalid material: " + material);
                        }

                        ItemStack drill = new ItemStack(Material.OBSIDIAN);
                        ItemMeta drillMeta = drill.getItemMeta();
                        drillMeta.setDisplayName(ChatColor.WHITE + "Drill (Placeable) (" + drillPlayerName + ")");
                        List<String> drilllore = new ArrayList<>();
                        drilllore.add(ChatColor.WHITE + "Place this block to create a " + ChatColor.GOLD + "DRILL" + ChatColor.WHITE + " object.");
                        drilllore.add(ChatColor.WHITE + "It will by time earn you " + ChatColor.GOLD + drillType + ChatColor.WHITE + " for your nation's economy.");
                        drilllore.add("");
                        drilllore.add(ChatColor.WHITE + "It can only be spawned at " + ChatColor.GRAY + Location + ".");
                        drilllore.add(ChatColor.WHITE + "Losing the chunk in a war will give the " + ChatColor.GOLD + "DRILL" + ChatColor.WHITE + "to the new owner.");
                        drillMeta.setLore(drilllore);

                        drillMeta.setCustomModelData(modelData);
                        drill.setItemMeta(drillMeta);

                        drillTargetPlayer.getInventory().addItem(drill);

                        Common.tellNoPrefix(drillTargetPlayer, "&fYou have been given a Drill by an admin.");

                        tellSuccess("Given Drill Block to " + drillPlayerName);

                        
                        break;
                    case "wood":
                        tellWarn("unimplemented");
                        break;
                    case "stone":
                        tellWarn("unimplemented");
                        break;
                    case "brick":
                        tellWarn("unimplemented");
                        break;
                    case "darkstone":
                        tellWarn("unimplemented");
                        break;
                    default:
                        tellWarn("Invalid resource type");
                        break;
                }
                break;
            case "list":
                // List all nations
                Map<UUID, NationYML> nations2 = NationYML.getNations();
                List<String> listAdminMessage = new ArrayList<>();
                listAdminMessage.add("&f" + Common.chatLineSmooth());
                listAdminMessage.add("   &fNation List");
                for (NationYML nation3 : nations2.values()) {
                    listAdminMessage.add("   &f- &7" + nation3.getString("nationName"));
                }
                listAdminMessage.add("&f" + Common.chatLineSmooth());
                tellNoPrefix(listAdminMessage);
                break;
            default:
                tellWarn("&fYou must specify a subcommand. &7/nation admin <delete/rename/removechunk/endwar/givetroop/give> <nation/mob/block>");
                break;
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                return completeLastWord("delete", "rename", "list", "removechunk", "endwar", "givetroop", "give");
            case 2:
                if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rename")) {
                    // Get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    // Extract nation names
                    return completeLastWord(nations.values().stream()
                            .map(nation -> nation.getString("nationName")).toArray(String[]::new));
                }

                if (args[0].equalsIgnoreCase("give")) {
                    return completeLastWord("drill", "housing", "wood", "stone", "brick", "darkstone");
                }
            case 3:
                if (args[1].equalsIgnoreCase("housing")) {
                    return completeLastWord(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .toArray(String[]::new));
                }
        }

        return NO_COMPLETE;
    }


}


