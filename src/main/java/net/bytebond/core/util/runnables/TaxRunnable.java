package net.bytebond.core.util.runnables;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.mineacademy.fo.model.SimpleRunnable;

import java.util.Map;

public class TaxRunnable extends SimpleRunnable {



    @Override
    public void run() {
        Core.getInstance().debugLog("Starting TaxCollection (2.0) task...");
        Map<NationYML, String> nationMap = HashMan.getInstance().getAllNationMap();
        Integer i = 0;

        for (NationYML nation : nationMap.keySet()) {
            int population = nation.getStringList("villagers").size();
            int taxRate = nation.getInteger("taxRate");
            int interval = Config.Tax.tax_collection_interval;
            int happiness = nation.getInteger("happiness");

            // Multiply Population by Taxrate to get the gross income
            double grossIncome = (double) population * taxRate;

            int reductionFactor = happiness < 70 ? 70 - happiness : 0;
            grossIncome = (double) population * taxRate * (1 - reductionFactor / 100.0);
            // calculate hourly income
            double hourlyIncome = grossIncome / interval;
            // times 12 for income per 12 hours
            double incomePer24Hours = hourlyIncome * 24;

            int hourlyIncomeInt = (int) hourlyIncome;
            int incomePer24HoursInt = (int) incomePer24Hours;

            Core.getInstance().debugLog("Tax collection for " + nation.getString("nationName") + " is " + incomePer24Hours + "/" + hourlyIncome + " and the tax rate is " + taxRate + " and the population is " + population + " and the happiness is " + happiness);
            OfflinePlayer offlinePlayer = Core.getInstance().getServer().getOfflinePlayer(nation.getString("owner"));
            Core.getEconomy().depositPlayer(offlinePlayer, incomePer24HoursInt);
        }
    }

    public void startTask(long hours) {
        if(Config.Runnables.start_on_server_start) {
            this.runTaskTimerAsynchronously(Core.getInstance(), 0L, hours * 60 * 60 * 20);
        }else {
            this.runTaskTimerAsynchronously(Core.getInstance(), hours * 60 * 60 * 20, hours * 60 * 60 * 20);
        }
    }


    public Integer getTaxEfficiency(NationYML nation) {
        int population = nation.getStringList("villagers").size();
        int taxRate = nation.getInteger("taxRate");
        int interval = 0;
        int efficiency = 0;


        //Core.getInstance().debugLog("Tax efficiency for " + nation.getString("nationName") + " is " + efficiency);
        return efficiency;
    }

}
