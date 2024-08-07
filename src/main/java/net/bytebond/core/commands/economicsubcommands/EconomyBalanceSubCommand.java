package net.bytebond.core.commands.economicsubcommands;

import net.bytebond.core.Core;
import net.bytebond.core.commands.EconomyHandler;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EconomyBalanceSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public EconomyBalanceSubCommand(SimpleCommandGroup parent) {
        super(parent, "balance");
        setPermission("nation.player");
        setDescription("View your nation's treasury balance");
        setUsage("/economy balance");
    }


    @Override
    protected void onCommand() {
        checkConsole();
        Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);
        UUID UUID = player.getUniqueId();
        List<String> messages = new ArrayList();

        messages.add("&f--- Nations Economy Overview ---");
        if(!(nationPlayer.inNation())) {
            messages.add("&cNote: &fThese features are only accessible to nations.");
            messages.add("&fYou can create one by using &7/nation create <name>");
            messages.add("&f" + Common.chatLineSmooth());
            return;
        }
        NationYML nation = nationPlayer.getNation();
        messages.add("   &fNation: &7" + nationPlayer.getNation().getString("nationName") + " (" + nationPlayer.getNation().getString("TAG") + ")");
        //messages.add("   &fMoney: &a$&7" + Core.getEconomy().getBalance(player));  // Shows x amount of .0 at the end
        messages.add("   &fMoney: &a$&7" + String.format("%.1f", Core.getEconomy().getBalance(player)));
        messages.add("   &fWood: &7" + EconomyHandler.GetEconomy(nation, EconomyHandler.Currency.WOOD) + "   &fStone: &7" + EconomyHandler.GetEconomy(nation, EconomyHandler.Currency.STONE));
        messages.add("   &fBrick: &7" + EconomyHandler.GetEconomy(nation, EconomyHandler.Currency.BRICK) + "   &fDarkstone: &7" + EconomyHandler.GetEconomy(nation, EconomyHandler.Currency.DARKSTONE));
        messages.add("   &fObsidian: &7" + EconomyHandler.GetEconomy(nation, EconomyHandler.Currency.OBSIDIAN));
        messages.add("   &fOwned Miners: &7()" + "   &c&mOwned Trade-chests:&r &7()" );
        messages.add("&f" + Common.chatLineSmooth());
        Common.tellNoPrefix(player, messages);
    }

    }
