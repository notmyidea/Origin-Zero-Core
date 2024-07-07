package net.bytebond.core.commands.diplomaticsubcommands;

import net.bytebond.core.data.NationYML;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NationDiplomacySubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public NationDiplomacySubCommand(SimpleCommandGroup parent) {
        super(parent, "diplomacy||d");

        setPermission("nation.player");
        setDescription("Manage your nations diplomacy");
        setUsage("/nation diplomacy <ally/unally/declare/surrender>");
    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML senderNation = new NationYML(UUID);

        if(!(senderNation.isSet("nationName"))) {
            tellWarn("&fYou are not currently part of a Nation.");
            return;
        }

        if(args.length != 2) {
            tellWarn("&fYou must specify a Nation to do diplomacy with. &7/nation ally <name>");
            return;
        }

        if (args[1].equals(senderNation.getString("nationName"))) {
            tellWarn("&fYou cannot do diplomacy with your own nation.");
            return;
        }



        String firstArg = args[0];
        String secondArg = args[1];
        switch (args[0]) {
            case "ally":
                // request alliance requires the user to be online
                // check if a nation with the name exists
                Map<UUID, NationYML> nations = NationYML.getNations();
                for (NationYML nation : nations.values()) {
                    if (nation.getString("nationName").equals(secondArg)) {
                        // The nation with the name secondArg exists

                        // Get the owner's UUID
                        String ownerUUIDString = nation.getString("owner");
                        UUID ownerUUID = UUID.fromString(ownerUUIDString);

                        // check if they are already allied
                        List<String> senderAlliedNations1 = senderNation.getStringList("allied_nations");
                        List<String> receiverAlliedNations = nation.getStringList("allied_nations");
                        List<String> receiverPendingAlliances = nation.getStringList("pending_allied_nations");

                        if (senderAlliedNations1.contains(secondArg) && receiverAlliedNations.contains(senderNation.getString("nationName"))) {
                            tellWarn("&fYou are already allied with the nation: " + secondArg);
                            return;
                        } else if (receiverPendingAlliances.contains(senderNation.getString("nationName"))) {
                            // The receiving nation has already requested an alliance, confirm it immediately
                            receiverPendingAlliances.remove(senderNation.getString("nationName"));
                            nation.set("pending_allied_nations", receiverPendingAlliances);
                            nation.save();

                            // Add each other to the list of allied nations
                            senderAlliedNations1.add(secondArg);
                            senderNation.set("allied_nations", senderAlliedNations1);
                            senderNation.save();

                            receiverAlliedNations.add(senderNation.getString("nationName"));
                            nation.set("allied_nations", receiverAlliedNations);
                            nation.save();

                            tellSuccess("&fYou are now allied with the nation: " + secondArg);
                            return;
                        }

                        // Check if the owner is online
                        Player owner = Bukkit.getPlayer(ownerUUID);
                        if (owner != null) {
                            // The owner is online

                            // Check if the nation has already requested an alliance
                            List<String> senderAllianceRequests = senderNation.getStringList("pending_allied_nations");
                            if (senderAllianceRequests.contains(secondArg)) {
                                // The sender has already requested an alliance, remove the pending alliance &&
                                senderAllianceRequests.remove(secondArg);
                                senderNation.set("pending_allied_nations", senderAllianceRequests);
                                senderNation.save();

                                // remove pending_allied_nations from the receiving nation
                                List<String> receivingAllianceRequests = nation.getStringList("pending_allied_nations");
                                receivingAllianceRequests.remove(senderNation.getString("nationName"));
                                nation.set("pending_allied_nations", receivingAllianceRequests);
                                nation.save();

                                // set into allied_nations
                                List<String> senderAlliedNations = senderNation.getStringList("allied_nations");
                                senderAlliedNations.add(secondArg);
                                senderNation.set("allied_nations", senderAlliedNations);
                                senderNation.save();
                                // set into receiving allied_nations
                                List<String> receivingAlliedNations = nation.getStringList("allied_nations");
                                receivingAlliedNations.add(senderNation.getString("nationName"));
                                nation.set("allied_nations", receivingAlliedNations);
                                nation.save();

                                tellInfo("&fYou are now allied with the nation: " + secondArg);
                                owner.sendMessage("§7§l[§1!§7] §rYou are now allied with §7" + senderNation.getString("nationName"));

                            } else {
                                // Request an alliance in pending_allied_nations
                                List<String> receiverPendingAlliances1 = nation.getStringList("pending_allied_nations");
                                receiverPendingAlliances1.add(senderNation.getString("nationName"));
                                nation.set("pending_allied_nations", receiverPendingAlliances1);
                                nation.save();
                                tellSuccess("&fYou have requested an alliance with the nation: " + secondArg);
                                // inform the receiving nation
                                owner.sendMessage("§7§l[§1!§7] §rThe nation: §7" + senderNation.getString("nationName") + "§r has requested an §aalliance§r with you.");
                                owner.sendMessage("Type /nation diplomacy ally §7" + senderNation.getString("nationName") + "§r to accept.");
                            }
                        } else {
                            // The owner is offline
                            tellWarn("&fThe owner of the nation: §7" + secondArg + "§r is currently offline.");
                        }

                        return;
                    }
                }
                // If the code reaches this point, no nation with the name secondArg was found
                tellWarn("&fNo nation found with the name: §7" + secondArg);
                break;


            case "unally":
                // unally with a nation, requires the user to be online
                Map<UUID, NationYML> nations1 = NationYML.getNations();
                for (NationYML nation : nations1.values()) {
                    if (nation.getString("nationName").equals(secondArg)) {
                        // owners uuid
                        String ownerUUIDString = nation.getString("owner");
                        UUID ownerUUID = UUID.fromString(ownerUUIDString);
                        // Check if the owner is online
                        Player owner = Bukkit.getPlayer(ownerUUID);
                        if (owner != null) {
                            // The owner is online
                            // check if they are already allied
                            List<String> senderAlliedNations1 = senderNation.getStringList("allied_nations");
                            if (!(senderAlliedNations1.contains(secondArg))) {
                                tellWarn("&fYou are not allied with the nation: §7" + secondArg);
                                return;
                            }
                            // unally
                            senderAlliedNations1.remove(secondArg);
                            senderNation.set("allied_nations", senderAlliedNations1);
                            senderNation.save();
                            // remove the sender nation from the allied nations of the other nation
                            List<String> alliedNations = nation.getStringList("allied_nations");
                            alliedNations.remove(senderNation.getString("nationName"));
                            nation.set("allied_nations", alliedNations);
                            nation.save();
                            // inform the other nation
                            owner.sendMessage("§7§l[§1!§7] §rYou are no longer allied with §7" + senderNation.getString("nationName") + "§r.");
                            tellSuccess("&rSuccessfully unallied with the nation: §7" + secondArg);
                        } else {
                            // The owner is offline
                            tellWarn("&fThe owner of the nation: §7" + secondArg + "§r is currently offline.");
                        }

                        return;
                    }
                }
                tellWarn("&fNo nation found with the name: " + secondArg);
                break;
            case "declare":
                tellWarn("&fThis feature is currently disabled.");
                break;
            case "surrender":
                tellWarn("&fThis feature is currently disabled.");
                break;
        }
    }


    @Override
    protected List<String> tabComplete() {
        if(!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML senderNation = new NationYML(UUID);
        String senderNationName =  senderNation.getString("nationName");

        // display none if the player doesnt have a nation
        if(!(senderNation.isSet("nationName"))) {
            return completeLastWord("");
        }

        switch (args.length) {
            case 1:
                return completeLastWord("ally", "unally", "declare", "surrender");
            case 2:
                if(args[0].equals("ally")) {
                    // Get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    List<String> nationNames = nations.values().stream()
                            .map(nation -> nation.getString("nationName"))
                            .collect(Collectors.toList());
                    nationNames.remove(senderNationName);
                    return completeLastWord(nationNames.toArray(new String[0]));
                }
                if(args[0].equals("unally")) {
                    // get all allied nations
                    for (String alliedNation : senderNation.getStringList("allied_nations")) {
                        return completeLastWord(alliedNation);
                    }
                }
                if(args[0].equals("declare")) {
                    // get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    List<String> nationNames = nations.values().stream()
                            .map(nation -> nation.getString("nationName"))
                            .collect(Collectors.toList());
                    nationNames.remove(senderNationName);
                    return completeLastWord(nationNames.toArray(new String[0]));
                }
                if(args[0].equals("surrender")) {
                    // get all nations

                    return completeLastWord("The nation you're at war with");
                }


            default:
                return completeLastWord("");
        }
    }


}