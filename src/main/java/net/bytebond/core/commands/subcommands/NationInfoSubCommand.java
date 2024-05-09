package net.bytebond.core.commands.subcommands;

import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class NationInfoSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    protected NationInfoSubCommand(SimpleCommandGroup parent) {
        super(parent, "info");

        setDescription("Get information about a nation.");
        setUsage("[name]");
    }


    @Override
    protected void onCommand() {
        tellInfo("Get information about a nation");
    }
}