package net.bytebond.core.commands.subcommands;

import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.UUID;

public class NationDeleteSubCommand extends SimpleSubCommand {

    /* Parent: ../$NationCommand.class
     * /nation delete
     */

    public NationDeleteSubCommand(SimpleCommandGroup parent) {
        super(parent, "delete");

        setDescription("Delete your Nation.");
        setUsage("confirm");
    }


    @Override
    protected void onCommand() {
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);

        if(!(nation.isSet("nationName"))) {
            tellWarn("You are not currently part of a Nation.");
            return;
        }


        if(args.length != 1) {
            tellWarn("&fYou must confirm the deletion of your Nation. &7/nation delete confirm");
            return;
        }
        String firstArg = args[0];

        if(!(firstArg.equalsIgnoreCase("confirm"))) {
            tellWarn("&fYou must confirm the deletion of your Nation. &7/nation delete confirm");
            return;
        }

        nation.deleteFile();
        tellSuccess("&fYou have successfully deleted your Nation.");


    }

}