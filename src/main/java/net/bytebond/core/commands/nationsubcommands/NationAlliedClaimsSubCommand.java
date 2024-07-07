package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NationAlliedClaimsSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public NationAlliedClaimsSubCommand(SimpleCommandGroup parent) {
        super(parent, "alliedclaims");
        setPermission("nation.player");
        setDescription("Toggles if allies can build, interact and move armies through your nations territory");
        setUsage("/nation alliedclaims <true/false>");
    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);

        if(!(nation.isSet("nationName"))) {
            tellWarn("&fYou are not currently part of a Nation.");
            return;
        }

        if (args.length != 1) {
            tellWarn("&fYou must specify a boolean value. &7/nation alliedclaims <true/false>");
            return;
        }

        switch (args[0]) {
            case "true":
                if(nation.getBoolean("allyPermissions") != null && nation.getBoolean("allyPermissions")) {
                    tellWarn("&fAllies are already able to interact inside of your territories.");
                    return;
                } else {
                    tellWarn("&fAllies can now interact inside of your territories.");
                    nation.set("allyPermissions", true);
                    nation.save();
                }
                break;
            case "false":
                if(nation.getBoolean("allyPermissions")) {
                    tellWarn("&fAllies can no longer interact inside of your territories.");
                    nation.set("allyPermissions", false);
                    nation.save();
                } else {
                    tellWarn("&fAllies are already unable to interact inside of your territories.");
                }
                break;
            default:
                tellWarn("&fYou must specify a boolean value. &7/nation alliedclaims <true/false>");
                break;
        }
    }

    @Override
    protected List<String> tabComplete() {

        if(!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        if (args.length == 1) {
            return completeLastWord("true", "false");
        }

        return NO_COMPLETE;
    }


}