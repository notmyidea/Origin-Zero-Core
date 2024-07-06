package net.bytebond.core.commands;

import net.bytebond.core.data.NationYML;

public class EconomyHandler {

    public enum Currency {
        WOOD,
        STONE,
        BRICK,
        DARKSTONE,
        OBSIDIAN,
        TREASURY
    }

    public static void SetEconomy(NationYML nation, Currency currency, int newAmount) {
        switch (currency) {
            case WOOD:
                nation.set("wood", newAmount);
                nation.save();
                break;
            case STONE:
                nation.set("stone", newAmount);
                nation.save();
                break;
                case BRICK:
                nation.set("brick", newAmount);
                nation.save();
                break;
            case DARKSTONE:
                nation.set("darkstone", newAmount);
                nation.save();
                break;
            case OBSIDIAN:
                nation.set("obsidian", newAmount);
                nation.save();
                break;
            case TREASURY:
                nation.set("treasury", newAmount);
                nation.save();
                break;
        }
    }

    public static int GetEconomy(NationYML nation, Currency currency) {
        switch (currency) {
            case WOOD:
                return nation.getInteger("wood");
            case STONE:
                return nation.getInteger("stone");
            case BRICK:
                return nation.getInteger("brick");
            case DARKSTONE:
                return nation.getInteger("darkstone");
            case OBSIDIAN:
                return nation.getInteger("obsidian");
            case TREASURY:
                return nation.getInteger("treasury");
        }
        return 0;
    }

    public static void AddEconomy(NationYML nation, Currency currency, int amount) {
        int currentAmount = GetEconomy(nation, currency);
        SetEconomy(nation, currency, currentAmount + amount);
    }

    public static void SubtractEconomy(NationYML nation, Currency currency, int amount) {
        int currentAmount = GetEconomy(nation, currency);
        SetEconomy(nation, currency, currentAmount - amount);
    }

    public static void sendEconomy(NationYML sender, NationYML receiver, Currency currency, int amount) {
        SubtractEconomy(sender, currency, amount);
        AddEconomy(receiver, currency, amount);
    }

    public static Boolean checkAvailableEconomy(NationYML nation, Currency currency, int amount) {

        if(!(NationYML.doesNationExist(nation.getString("nationName")))) {
            return false;
        }

        if(GetEconomy(nation, currency) < amount) {
            return false;
        }
        return true;
    }

}
