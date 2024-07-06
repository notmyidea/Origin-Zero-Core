package net.bytebond.core.commands.economicsubcommands;

import net.bytebond.core.commands.EconomyHandler;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EconomySendCommand extends SimpleSubCommand {

    private String nation;
    private EconomyHandler.Currency currency;
    private int amount;


    public EconomySendCommand(SimpleCommandGroup parent) {
        super(parent, "send");

        setDescription("Deposit currency in another nations treasury");
        setUsage("/economy send <nation> <currency> <amount>");
    }


    @Override
    protected void onCommand() {
        Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);

        if(!(nationPlayer.inNation())) {
            tellError("You are not in a nation.");
            return;
        }

        if(args.length < 3) {
            tellInfo("Usage: /economy send <nation> <currency> <amount>");
            return;
        }

        if(nationPlayer.getNation().getString("nationName").equals(args[0])) {
            tellError("You cannot send currency to your own nation.");
            return;
        }

        if(!(NationYML.doesNationExist((args[0])))) {
            tellError("Nation not found.");
            return;
        }

        try {
            currency = EconomyHandler.Currency.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            tellError("Invalid currency. Please enter one of the following: " + Arrays.toString(EconomyHandler.Currency.values()));
            return;
        }

        if(amount < 0) {
            tellError("Invalid amount.");
            return;
        }

        if (args.length != 3) {
            tellError("Invalid arguments. Usage: /economy send <nation> <currency> <amount>");
            return;
        }

        nation = args[0];
        String currencyString = args[1].toUpperCase();
        EconomyHandler.Currency currency;
        try {
            currency = EconomyHandler.Currency.valueOf(currencyString);
        } catch (IllegalArgumentException e) {
            tellError("Invalid currency.");
            return;
        }

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            tellError("Invalid amount.");
            return;
        }

        if(amount < 1) {
            tellError("You cannot send less than $1.");
            return;
        }

        if(EconomyHandler.checkAvailableEconomy(nationPlayer.getNation(), currency, amount)) {
            EconomyHandler.sendEconomy(nationPlayer.getNation(), NationYML.getNationsByName(nation).get(0), currency, amount);
            tellSuccess("&aCurrency sent successfully.");


        } else {
            tellError("Not enough currency.");
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (!isPlayer()) {
            return new ArrayList<>();
        }

        final Player player = (Player) sender;
        NationPlayer nationPlayer = new NationPlayer(player);
        if(args.length == 1) {
            String playerNationName = nationPlayer.getNation().getString("nationName");
            return completeLastWord(NationYML.getNations().values().stream()
                    .map(nation -> nation.getString("nationName"))
                    .filter(nationName -> !nationName.equals(playerNationName))
                    .collect(Collectors.toList()));
        }
        if(args.length == 2) {
            return completeLastWord(Arrays.asList(EconomyHandler.Currency.values()));
        }
        if(args.length == 3) {
            return completeLastWord("0-" + EconomyHandler.GetEconomy(nationPlayer.getNation(), currency));
        }

        return NO_COMPLETE;
    }

}
