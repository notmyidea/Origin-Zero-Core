package net.bytebond.core.commands.economicsubcommands;

import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NationTaxSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public NationTaxSubCommand(SimpleCommandGroup parent) {
        super(parent, "tax");

        setDescription("Manage your Nations taxes");
        setUsage("/nation tax <increase/decrease> ");
    }


    Integer old;
    Integer now;

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

        if(args.length != 1) {
            tellWarn("&fYou must specify a valid argument. &7/nation tax <increase/decrease>");
            return;
        }

        switch(args[0]) {
            case "increase":
                if(nation.getInteger("taxRate") == 100) {
                    tellWarn("&fYou cannot increase the tax rate any further.");
                    return;
                }
                old = nation.getInteger("taxRate");
                now = old + 1;
                nation.set("taxRate", nation.getInteger("taxRate") + 1);
                nation.save();
                tellSuccess("&fYou have increased the tax rate of your nation from &7" + old + "&f to &7" + now);
                break;
            case "decrease":
                if(nation.getInteger("taxRate") == 0) {
                    tellWarn("&fYou cannot decrease the tax rate any further.");
                    return;
                }
                old = nation.getInteger("taxRate");
                now = old - 1;
                nation.set("taxRate", nation.getInteger("taxRate") - 1);
                nation.save();
                tellSuccess("&fYou have decreased the tax rate of your nation from &7" + old + "&f to &7" + now);
                break;
            default:
                tellWarn("&fYou must specify a valid argument. &7/nation tax <increase/decrease>");
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
                return completeLastWord("increase", "decrease");
            default:
                return NO_COMPLETE;
        }
    }
}