package net.bytebond.core.util;

import net.bytebond.core.Core;
import net.bytebond.core.data.NationYML;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.Common;

import java.util.*;

public class NationTaxCollection {

    private static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000; // 24 hours in milliseconds
    private final HashMap<String, Integer> recentTaxPayouts = new HashMap<>(); // Store recent tax payouts for each nation


    public void startTaxCollection() {
        Timer timer = new Timer();
        NationTaxCollection thisInstance = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Collection<NationYML> nations = NationYML.getNations().values();
                if (nations.isEmpty()) {
                    return; // If there are no nations do nothing
                }
                if(nations == null) {
                    return;
                }
                nations.forEach(thisInstance::collectTax);
            }
        }, 0, DAY_IN_MILLIS); // start immediately and run every 24 hours
    }

    public void collectTax(NationYML nation) {
        OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(nation.getString("owner")));
        List<String> villagersList = nation.getStringList("villagers");
        int population = villagersList.size();
        double taxRate = nation.getInteger("taxRate") / 100.0; // convert taxRate to double because 0.1 is not acceptable
        double happiness = nation.getInteger("happiness") / 100.0; // also ^^^

        Core.getInstance().debugLog("Population for " + nation.getString("nationName") + " is " + population);
        Core.getInstance().debugLog("Tax rate for " + nation.getString("nationName") + " is " + taxRate);
        Core.getInstance().debugLog("Happiness for " + nation.getString("nationName") + " is " + happiness);
        Core.getInstance().debugLog("Calculating tax collection for " + nation.getString("nationName"));
        Integer IntTaxCollection = (int) (population * 100 * taxRate * happiness);
        Core.getInstance().debugLog(recentTaxPayouts.get(nation.getString("nationName")) + " is the recent tax payout for " + nation.getString("nationName") + " and the new tax collection is " + IntTaxCollection);
        double taxCollection = (population * 100 * taxRate * happiness);
        recentTaxPayouts.put(nation.getString("nationName"), IntTaxCollection);


        Core.getInstance().debugLog("Tax collection for " + nation.getString("nationName") + " is " + taxCollection);
        //EconomyHandler.AddEconomy(nation, EconomyHandler.Currency.TREASURY, taxCollection);
        EconomyResponse r = Core.getEconomy().depositPlayer(owner, taxCollection);
        if (r.transactionSuccess()) {
            if(owner.isOnline()) {
               Common.tellNoPrefix(owner.getPlayer(), "&fYour nation has collected &a$" + taxCollection + " &ftoday.") ;
            }
            Core.getInstance().debugLog("IRS deposited " + taxCollection + " to " + owner.getName());
        } else {
            throw new IllegalArgumentException("IRS could not deposit tax money to " + owner.getName());
       }

    }

    public Integer getRecentTaxPayout(String nationName) {
        if(recentTaxPayouts.get(nationName) == null) {
            return 0;
        }
        return recentTaxPayouts.get(nationName);
    }

}