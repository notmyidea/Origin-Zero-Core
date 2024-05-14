package net.bytebond.core.commands.subcommands;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Messages;
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
                    messages.add("   &fWarscore: &a★&f" + nation.getVaultBalance());
                    messages.add("   &fEconomic Information: ");
                    messages.add("");
                    messages.add("   &fWood: &7" + nation.getUEConWood().intValue());
                    messages.add("   &fStone: &7" + nation.getUEConStone().intValue());
                    messages.add("   &fBrick: &7" + nation.getUEConBrick().intValue());
                    messages.add("   &fDarkstone: &7" + nation.getUEConDarkstone().intValue());
                    messages.add("   &fObsidian: &7" + nation.getUEConObsidian().intValue() + "&f. You have &7" + amountOfDrills + " &fdrills.");
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
                    tellInfo("building");
                    break;
                case "demographics":
                    tellInfo("demographics");
                    break;
                case "infrastructure":
                    tellInfo("infrastructure");
                    break;
                case "trade":
                    tellInfo("trade");
                    break;
                case "territory":
                    tellInfo("territory");
                    break;
                case "diplomacy":
                    tellInfo("diplomacy");
                    break;
                case "chunk":
                    List<String> chunkMessage = new ArrayList();
                    chunkMessage.add("&f" + Common.chatLineSmooth());
                    chunkMessage.add("   &fChunk Information &7(&f" + player.getLocation().getChunk().getX() + "&7, &f" + player.getLocation().getChunk().getZ() + "&7)");
                    Map<UUID, NationYML> nationsMap = NationYML.getNations(); String chunkStr = player.getLocation().getWorld().getName() + "," + player.getLocation().getChunk().getX() + "," + player.getLocation().getChunk().getZ();
                    for(NationYML nations : nationsMap.values()) {
                        if(nations.isSet("territory")) {
                            if(nations.getStringList("territory").contains(chunkStr)) { // use chunkStr instead of chunk.toString()
                                chunkMessage.add("   &fClaimed by: &7" + nations.getString("nationName"));
                                chunkMessage.add("   &fInfrastructure: &70");
                                chunkMessage.add("   &fHousing: &7(&a0&7/0&7)");
                                chunkMessage.add("   &fDrills: &70");
                            } else {
                                chunkMessage.add("   &fUnclaimed.");
                            }
                        }
                    }



                    //chunkMessage.add("   &fRessource availability:  &7" + resourceStr.toString().replace(",", "&f,&7"));
                    //chunkMessage.add("   &f->   " + valueStr.toString());

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
                    return completeLastWord("economy", "demographics", "infrastructure", "trade", "territory", "diplomacy", "chunk");
                case 2:
                    return completeLastWord("");

            }

            return NO_COMPLETE;
        }


}





