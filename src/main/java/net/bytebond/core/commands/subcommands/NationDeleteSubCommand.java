package net.bytebond.core.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

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
        tellInfo("Deleted your Nation");
    }


}