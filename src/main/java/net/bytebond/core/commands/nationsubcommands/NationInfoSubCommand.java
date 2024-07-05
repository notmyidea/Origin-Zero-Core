package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.*;

public class NationInfoSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public NationInfoSubCommand(SimpleCommandGroup parent) {
        super(parent, "info");

        setDescription("Get information about your Nation.");
    }


    @Override
    protected void onCommand() {
        checkConsole();
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        List<String> messages = new ArrayList();
        NationYML nation = new NationYML(UUID);
        int amountOfDrills;
        if(nation.getDrills().contains("None")) {
            amountOfDrills = 0;
        } else {
            amountOfDrills = nation.getDrills().size();
        }

        // check if the player is in a Nation
        if(!(nation.isSet("nationName"))) {
            messages.add("&f" + Common.chatLineSmooth());
            messages.add("&fYou are not currently part of a Nation.");
            messages.add("&fYou can create one with &7/nation create <name>");
            messages.add("&f/nation help/info &7for more information");
            messages.add("&f" + Common.chatLineSmooth());
            tellNoPrefix(messages);
            return;
        }



            if (args.length == 0) {
                messages.add("&f" + Common.chatLineSmooth());
                if(!(nation.isSet("nationName"))) {
                    messages.add("&fYou are not currently part of a Nation.");
                    messages.add("&fYou can create one with &7/nation create <name>");
                    messages.add(" ");
                    messages.add("&f/nation help/info &7for more information");
                } else {
                    if (nation.isSet("nationDescription") || !(nation.getString("nationDescription").isEmpty())) {
                        messages.add("   &f" + nation.getString("nationDescription"));
                    }

                    if(nation.isSet("territory") && (!(nation.getStringList("territory").isEmpty()))) {
                        messages.add("   &fYou are the owner of &7" + nation.getString("nationName") + " &7(&f" + nation.getString("TAG") + "&7) (&c" + nation.getStringList("territory").size() + "&7)");
                    } else {
                        messages.add("   &fYou are the owner of &7" + nation.getString("nationName") + " &7(&f" + nation.getString("TAG") + "&7)&f");
                    }
                    messages.add("   &fWarscore: &a★&f" + nation.getInteger("warscore"));
                    messages.add("   &fEconomic Information:       Tax: &c" + nation.getInteger("taxRate")+ "&f%, Happiness: " + "&aCalc.%");
                    messages.add("");
                    messages.add("   &fWood: &7" + nation.getString("wood"));
                    messages.add("   &fStone: &7" + nation.getString("stone"));
                    messages.add("   &fBrick: &7" + nation.getString("brick"));
                    messages.add("   &fDarkstone: &7" + nation.getString("darkstone"));
                    messages.add("   &fObsidian: &7" + nation.getString("obsidian") + "&f. You have &7" + amountOfDrills + " &fdrills.");
                    messages.add(" ");
                    messages.add("   &fCategories: &7economy&f, &7demographics&f, &7infrastructure&f,");
                    messages.add("   &7trade&f, &7territory&f, &7diplomacy&f");
                    messages.add("   &fMore: &7/nation info chunk &f - Chunk information");
                    messages.add("   &f- ");
                }
                messages.add("&f" + Common.chatLineSmooth());
                tell(messages);
                return;
            }
            String firstArg = args[0];

            if(args.length > 1) {
                String secondArg = args[1];
            }
            if(args.length > 2) {
                String thirdArg = args[2];
            }

            switch (firstArg) {
                case "economy":
                    List<String> economyMessage = new ArrayList<>();
                    economyMessage.add("&f" + Common.chatLineSmooth());
                    economyMessage.add("   &fEconomic Information &7(&f" + nation.getString("nationName") + "&7)");
                    economyMessage.add("   &fTax Rate: &c" + nation.getInteger("taxRate") + "&f%");
                    economyMessage.add("   &fTreasury: &7" + nation.getString("treasury"));
                    economyMessage.add("   &fWood: &7" + nation.getString("wood"));
                    economyMessage.add("   &fStone: &7" + nation.getString("stone"));
                    economyMessage.add("   &fBrick: &7" + nation.getString("brick"));
                    economyMessage.add("   &fDarkstone: &7" + nation.getString("darkstone"));
                    economyMessage.add("   &fObsidian: &7" + nation.getString("obsidian"));
                    economyMessage.add("&f" + Common.chatLineSmooth());
                    tellNoPrefix(economyMessage);
                    break;
                case "infrastructure":
                    tellInfo("Command is currently disabled.");
                    break;
                case "trade":
                    tellInfo("Command is currently disabled.");
                    break;
                case "diplomacy":
                    List<String> diplomacyMessage = new ArrayList<>();
                    diplomacyMessage.add("&f" + Common.chatLineSmooth());
                    diplomacyMessage.add("   &fDiplomatic Information &7(&f" + nation.getString("nationName") + "&7)");
                    diplomacyMessage.add("   &fAllied Nations: ");
                    // get all allied nations
                    if(nation.getStringList("allied_nations") != null) {
                        for(String alliedNation : nation.getStringList("allied_nations")) {
                            diplomacyMessage.add("   &f- &7" + alliedNation);
                        }
                    } else {
                        diplomacyMessage.add("   &fNone.");
                    }
                    diplomacyMessage.add("   &fPending Allied Nations: ");
                    // get all pending allied nations
                    if(nation.getStringList("pending_allied_nations") != null) {
                        for(String pendingAlliedNation : nation.getStringList("pending_allied_nations")) {
                            diplomacyMessage.add("   &f- &7" + pendingAlliedNation);
                        }
                    } else {
                        diplomacyMessage.add("   &fNone.");
                    }
                    diplomacyMessage.add("   &fWars: &a" + nation.getInteger("warswon") + "&f/&c" + nation.getInteger("warslost"));
                    diplomacyMessage.add("   &fTroops: &7" + nation.getInteger("troops"));
                    diplomacyMessage.add("&f" + Common.chatLineSmooth());
                    tellNoPrefix(diplomacyMessage);
                    break;
                case "chunk":
                    List<String> chunkMessage = new ArrayList();
                    chunkMessage.add("&f" + Common.chatLineSmooth());
                    chunkMessage.add("   &fChunk Information &7(&f" + player.getLocation().getChunk().getX() + "&7, &f" + player.getLocation().getChunk().getZ() + "&7)");
                    Map<UUID, NationYML> nationsMap = NationYML.getNations();
                    String chunkStr = player.getLocation().getWorld().getName() + "," + player.getLocation().getChunk().getX() + "," + player.getLocation().getChunk().getZ();
                    ClaimRegistry claim = new ClaimRegistry(player.getLocation().getChunk(), null, null, player.getName());
                    for(NationYML nations : nationsMap.values()) {
                        if(nations.isSet("territory")) {
                            if(nations.getStringList("territory").contains(chunkStr)) {
                                if(nations.getString("nationName").equals(nation.getString("nationName"))) {
                                    chunkMessage.add("   &fClaimed by: &7" + nations.getString("nationName") + " &f(you)");
                                } else {
                                    chunkMessage.add("   &fClaimed by: &7" + nations.getString("nationName"));
                                }

                                chunkMessage.add("   &fInfrastructure: &70");
                                chunkMessage.add("   &fHousing: &7(&a0&7/0&7)");
                                chunkMessage.add("   &fDrills: &70");
                                chunkMessage.add("   &fHistory: ");
                                if(claim.getStringList("history") != null) {
                                    for(String history : claim.getStringList("history")) {
                                        chunkMessage.add("   &f- " + history);
                                    }
                                }
                            } else {
                                if(claim.getStringList("history") != null) {
                                    for(String history : claim.getStringList("history")) {
                                        chunkMessage.add("   &f- " + history);
                                    }
                                } else {
                                    chunkMessage.add("   &fUnclaimed.");
                                }
                            }
                        } else if(claim.getString("ownerUUID") != null && !claim.getString("ownerNationName").isEmpty()) {
                            //chunkMessage.add("   &fThis chunk was claimed by &7" + claim.getString("ownerNationName") + "&f but has since been deleted.");
                        }
                    }

                    chunkMessage.add("&f" + Common.chatLineSmooth());
                    tellNoPrefix(chunkMessage);
                    break;

            }

        }



        @Override
        protected List<String> tabComplete() {

            if(!isPlayer())
                return new ArrayList<>();

            final Player player = (Player) sender;

            switch (args.length) {
                case 1:
                    return completeLastWord("chunk", "economy", "diplomacy"); // "trade" , "infrastructure"
                case 2:
                    return completeLastWord("");

            }

            return NO_COMPLETE;
        }


}





