package net.bytebond.core.commands.adminsubcommands;

import com.comphenix.net.bytebuddy.implementation.bytecode.Throw;
import net.bytebond.core.Core;
import net.bytebond.core.commands.EconomyHandler;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.Drill;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Drills;
import net.bytebond.core.settings.Messages;
import net.bytebond.core.util.ItemManager;
import net.bytebond.core.util.NationTaxCollection;
import net.bytebond.core.util.handler.DrillHandling;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Material.CHEST;

public class AdminNationManager extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public AdminNationManager(SimpleCommandGroup parent) {
        super(parent, "admin");
        setDescription("Admin nation manager");
        setUsage("/nation admin <delete/rename/list/removechunk/endwar/givetroop/give> <nation/mob/block>");
        setPermission("nation.admin");
    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);
        checkPerm("nation.admin");
        //checkPerm("nation.admin");

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
                //setPermission("nation.admin");
                if (args.length < 2) {
                    tellInfo("Usage: /nation admin give <housing, drill, wood, stone, brick, ...> <resource/player> <amount> <> ()");
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
                        // THE ENTIRE ITEMSTACK HAS BEEN MOVED TO UTIL.ITEMMANAGER
                        ItemManager.giveHousingObject(targetPlayer, false);

                        tellSuccess("Given Housing Block to " + playerName);
                        break;
                    case "drill":
                        if (args.length < 4) {
                            tellInfo("Usage: /nation admin give drill <material> <player>");
                            return;
                        }
                        String material = args[2];
                        String drillPlayerName = args[3];

                        if (!material.equals("wood") && !material.equals("stone") && !material.equals("brick") && !material.equals("darkstone") && !material.equals("obsidian")) {
                            tellWarn("Invalid material: " + material);
                            return;
                        }

                        Player drillTargetPlayer = Bukkit.getPlayer(args[3]);
                        if (drillTargetPlayer == null) {
                            tellWarn("Player not found: " + args[3]);
                            return;
                        }
                        Drill.DrillType drillType = Drill.DrillType.valueOf(material.toUpperCase());
                        ItemManager.giveDrill(drillTargetPlayer, drillType, false);

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
                    case "block":

                        if (args.length < 6) {
                            tellInfo("Usage: /nation admin give block <nation> <currency> <cost> <blockType> <amount>");
                            return;
                        }
                        String currencyType = args[2];
                        int cost;
                        try {
                            cost = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            tellError("Invalid cost.");
                            return;
                        }
                        Material blockType;
                        try {
                            blockType = Material.valueOf(args[4].toUpperCase());
                        } catch (IllegalArgumentException e) {
                            tellError("Invalid block type.");
                            return;
                        }
                        int amount;
                        try {
                            amount = Integer.parseInt(args[5]);
                        } catch (NumberFormatException e) {
                            tellError("Invalid amount.");
                            return;
                        }
                        NationPlayer giveBlockNationPlayer = new NationPlayer((Player) sender);
                        if (!giveBlockNationPlayer.inNation() || !giveBlockNationPlayer.getNation().equals(nation)) {
                            tellWarn("You are not in a Nation.");
                            return;
                        }
                        EconomyHandler.Currency currency = EconomyHandler.Currency.valueOf(currencyType.toUpperCase());
                        if (EconomyHandler.checkAvailableEconomy(giveBlockNationPlayer.getNation(), currency, cost)) {
                            EconomyHandler.SubtractEconomy(giveBlockNationPlayer.getNation(), currency, cost);
                            ItemStack blockStack = new ItemStack(blockType, amount);
                            ((Player) sender).getInventory().addItem(blockStack);
                        }
                        tellSuccess("You have bought &7'" + amount + " " + blockType + "'&r for &7" + cost + " " + currencyType + "&r.");
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
            case "collecttax":
                if(args.length != 2) {
                    tellWarn("Usage: /nation admin collecttax <nation>");
                    return;
                }
                String collectTaxNationName = args[1];
                NationYML collectTaxNation = NationYML.getNationsByName(collectTaxNationName).stream().findFirst().orElse(null);
                if (collectTaxNation == null) {
                    tellWarn("Nation not found: " + collectTaxNationName);
                    return;
                }
                NationTaxCollection nationTaxCollection = new NationTaxCollection();
                nationTaxCollection.collectTax(collectTaxNation);
                tellSuccess("Manually started tax collection for nation " + collectTaxNationName);
                break;
            case "set":
                if (args.length < 3) {
                    tellWarn("Usage: /nation admin set <economy/ > <> ()");
                    return;
                }
                switch (args[1]) {
                    case "economy":


                        String targetNationName = args[2];
                        String typeOrReset = args[3];

                        // Check if the targeted nation exists
                        NationYML targetNation = NationYML.getNationsByName(targetNationName).stream().findFirst().orElse(null);
                        if (targetNation == null) {
                            tellWarn("Nation not found: " + targetNationName);
                            return;
                        }

                        // Check if the type equals one of the Economy Handler Materials or "reset"
//----

                        // If the type is "reset", reset the economy of the targeted nation
                        if (typeOrReset.equalsIgnoreCase("reset")) {
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.WOOD, Config.Nations.Creation.starting_resources);
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.STONE, Config.Nations.Creation.starting_resources);
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.BRICK, Config.Nations.Creation.starting_resources);
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.DARKSTONE, Config.Nations.Creation.starting_resources);
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.OBSIDIAN, Config.Nations.Creation.starting_resources);
                            tellSuccess("Economy of the nation " + targetNationName + " has been reset.");
                            return;
                        }

                        // If the type is one of the Economy Handler Materials, update the economy of the targeted nation
                        int amount;
                        try {
                            amount = Integer.parseInt(args[4]);
                            EconomyHandler.SetEconomy(targetNation, EconomyHandler.Currency.valueOf(typeOrReset.toUpperCase()), amount);
                        } catch (NumberFormatException e) {
                            tellError("Invalid amount.");
                            return;
                        }

                        // Update economy logic here
                        tellSuccess("Economy of the nation " + targetNationName + " has been updated.");
                        break;
                    default:
                        tellWarn("Usage: /nation admin set <economy/ > <> ()");
                        break;
                }
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
                return completeLastWord("delete", "rename", "list", "removechunk", "endwar", "givetroop", "give", "collecttax", "set");
            case 2:
                if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("collectTax")) {
                    // Get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    // Extract nation names
                    return completeLastWord(nations.values().stream()
                            .map(nation -> nation.getString("nationName")).toArray(String[]::new));
                }

                if(args[0].equalsIgnoreCase("set")) {
                    return completeLastWord("economy");
                }

                if (args[0].equalsIgnoreCase("give")) {
                    return completeLastWord("drill", "housing", "wood", "stone", "brick", "darkstone", "block");
                }
            case 3:
                if (args[1].equalsIgnoreCase("housing")) {
                    return completeLastWord(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .toArray(String[]::new));
                }
                if(args[1].equalsIgnoreCase("drill")) {
                    return completeLastWord("wood", "stone", "brick", "darkstone", "obsidian");
                }
                if(args[1].equalsIgnoreCase("block")) {
                    return completeLastWord("wood", "stone", "brick", "darkstone", "obsidian");
                }
                if(args[1].equalsIgnoreCase("economy")) {
                    // Get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    // Extract nation names
                    return completeLastWord(nations.values().stream()
                            .map(nation -> nation.getString("nationName")).toArray(String[]::new));
                }
            case 4:
                if(args[1].equalsIgnoreCase("block")) {
                    return completeLastWord("cost");
                }
                if(args[1].equalsIgnoreCase("drill")) {
                    return completeLastWord(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .toArray(String[]::new));
                }
                if(args[1].equalsIgnoreCase("economy")) {
                    return completeLastWord("wood", "stone", "brick", "darkstone", "obsidian", "reset");
                }
            case 5:
                if (args[1].equalsIgnoreCase("economy")) {
                    return completeLastWord("amount");
                }
                if(args[1].equalsIgnoreCase("block")) {
                    return completeLastWord(Arrays.stream(Material.values())
                            .filter(Material::isBlock)
                            .map(Material::name)
                            .toArray(String[]::new));
                }
            case 6:
                if(args[1].equalsIgnoreCase("block")) {
                    return completeLastWord("amount");
                }
            case 7:
                if(args[1].equalsIgnoreCase("block")) {
                    return completeLastWord(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .toArray(String[]::new));
                }
        }

        return NO_COMPLETE;
    }


}


