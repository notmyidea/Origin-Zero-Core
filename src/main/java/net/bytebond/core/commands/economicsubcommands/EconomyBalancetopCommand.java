package net.bytebond.core.commands.economicsubcommands;

import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class EconomyBalancetopCommand extends SimpleSubCommand {

    protected EconomyBalancetopCommand(SimpleCommandGroup parent) {
        super(parent, "balancetop");

        setDescription("View the top 10 nations by treasury balance");
        setUsage("/economy balancetop");
        setPermission("nation.player");
    }


    @Override
    protected void onCommand() {
        tellInfo("This command is currently disabled. Please try again later.");
    }
}