package net.bytebond.core.commands.economicsubcommands;

import com.palmergames.bukkit.towny.object.Nation;
import net.bytebond.core.data.NationPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.SimpleComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EconomyHelpCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public EconomyHelpCommand(SimpleCommandGroup parent) {
        super(parent, "help");
        setDescription("View the usage of the economy system");
        setUsage("/economy help");
        setPermission("nation.player");
    }


    @Override
    protected void onCommand() {
        Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);
        List<String> messages = new ArrayList<>();

        messages.add("&f--- Nations Economy Overview ---");
        if(!(nationPlayer.inNation())) {
            messages.add("   &cNote: &fThese features are only accessible to nations.");
            messages.add("   &fYou can create one by using &7/nation create <name>");
        }
        messages.add("&fAll available commands: ");
        messages.add("   &6/economy &ahelp &f- View this help message");
        messages.add("   &6/economy &abalance &f- View your nation's treasury balance");
        messages.add("   &6/economy &abalancetop &f- View the top 10 economies");
        messages.add("   &6/economy &asend <currency> <amount> <nation>&f- Deposit   currency to another nations treasury");
        messages.add("&f" + Common.chatLineSmooth());
        Common.tellNoPrefix(player, messages);
        //return Common.convert(messages, SimpleComponent::of);

    }
}