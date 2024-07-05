package net.bytebond.core.commands.adminsubcommands;

import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Messages;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if(args.length < 1) {
            tellWarn("&fYou must specify a subcommand. &7/nation admin <delete/rename/list/removechunk/endwar/givetroop/give> <nation/mob/block>");
            return;
        }

        String firstArg = args[0];

        switch (firstArg) {
            case "delete":
                // Delete a nation
                if(args.length != 2) {
                    tellWarn("&fYou must specify a nation to delete. &7/nation admin delete <nation>");
                    return;
                }
                String secondArg = args[1];
                Map<UUID, NationYML> nations = NationYML.getNations();
                NationYML nationToDelete = null;
                for (NationYML nation1 : nations.values()) {
                    if (nation1.getString("nationName").equals(secondArg)) {
                        nationToDelete = nation1;
                        break;
                    }
                }
                if (nationToDelete != null) {
                    nationToDelete.deleteFile();
                    tellSuccess("&rSuccessfully deleted the nation: &7" + secondArg);
                } else {
                    tellWarn("&rNo nation found with the name: &7" + secondArg);
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
                if(nationToRename == null) {
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

                if(args.length != 2) {
                    tellInfo("Usage: /nation admin give <drill/**/***> <resource> <amount>");
                }

                switch (args[1]) {
                    case "drill":
                        tellWarn("unimplemented");
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
        if(!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                return completeLastWord("delete", "rename", "list" ,"removechunk", "endwar", "givetroop", "give");
            case 2:
                if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rename")) {
                    // Get all nations
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    // Extract nation names
                    List<String> nationNames = nations.values().stream()
                            .map(nation -> nation.getString("nationName"))
                            .collect(Collectors.toList());
                    return completeLastWord(nationNames.toArray(new String[0]));
                }

        }

        return NO_COMPLETE;
    }




}