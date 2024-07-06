package net.bytebond.core.commands;

import net.bytebond.core.commands.economicsubcommands.EconomyBalanceSubCommand;
import net.bytebond.core.commands.economicsubcommands.EconomyHelpCommand;
import net.bytebond.core.commands.economicsubcommands.EconomySendCommand;
import net.bytebond.core.data.NationPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;

public final class EconomyCommand extends SimpleCommandGroup {

    public EconomyCommand() {
        super("economy||eco");
    }

    /*
            checkConsole();
        Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);
        UUID UUID = player.getUniqueId();
        List<String> messages = new ArrayList();
        NationYML nation = new NationYML(UUID);
     */

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new EconomyBalanceSubCommand(this));
        registerSubcommand(new EconomyHelpCommand(this));
        registerSubcommand(new EconomySendCommand(this));
    }

    @Override
    protected List<SimpleComponent> getNoParamsHeader() {
        Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);
        List<String> messages = new ArrayList<>();

        messages.add(Common.colorize("&f--- Nations Overview ---"));
        if (nationPlayer.inNation()) {
            messages.add(Common.colorize("&fWelcome to the Nations plugin! Here, you can manage your nation's wealth and resources."));
            messages.add(Common.colorize("&fUse /economy help to get started."));
            messages.add(Common.colorize("&fCurrencies: &6Treasury&f, &6Wood&f, &6Stone&f, &6Brick&f, &6Darkstone&f, &6Obsidian&f (Hover)"));
        } else {
            messages.add(Common.colorize("&cNote: &fThese features are only accessible to nations."));
        }
        messages.add(Common.chatLineSmooth());
        return Common.convert(messages, SimpleComponent::of);

    }

}
