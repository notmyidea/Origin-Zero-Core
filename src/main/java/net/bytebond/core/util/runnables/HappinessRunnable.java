package net.bytebond.core.util.runnables;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import org.mineacademy.fo.model.SimpleRunnable;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class HappinessRunnable extends SimpleRunnable {

    Boolean didStartup = false;
    Integer happiness = 0;

    private static final HappinessRunnable instance = new HappinessRunnable();

    public static HappinessRunnable getInstance() {
        return instance;
    }

    @Override
    public void run() {

        if (!didStartup) {
            didStartup = true;
            Core.getInstance().debugLog("Starting HappinessRunnable task pre-checks...");

        }
            Core.getInstance().debugLog("Starting HappinessRunnable task...");
            Map<NationYML, String> nationMap = HashMan.getInstance().getAllNationMap();
            Integer i = 0;
            for (NationYML nation : nationMap.keySet()) {
                int last_happiness = 0;
                List<String> villagers = nation.getStringList("villagers");
                int population = villagers.size();
                // update konform :))
                last_happiness = nation.getInteger("happiness");
                int taxRate = nation.getInteger("taxRate");
                int claims = nation.getStringList("territory").size();
                nation.set("last_happiness", happiness);
                // Calculate happiness OLD:
                //happiness = 1/3 * 1 - taxRate/100 + 1/3 * population/claims * 50;

                // NEW:
                double T = taxRate;
                double M = 20.0;
                double P = population;
                double HP = population;
                double C = claims;
                double happiness = (1.0/3.0) * (1 - T/M) + (1.0/3.0) * (HP/P) + (1.0/3.0) * (1 - P/C * 0.5);

                Integer happinessInt = (int) (happiness * 100);
                Core.getInstance().debugLog("Happiness for " + nation.getString("nationName") + " is " + happinessInt);
                nation.set("happiness", happinessInt);
                nation.set("happiness_decline", last_happiness - happinessInt);
                nation.set("last_happiness_check", DateTimeFormatter.ofPattern("MM/dd/yyyy").format(java.time.LocalDateTime.now()));
                nation.save();
                i++;
            }
            Core.getInstance().debugLog("HappinessRunnable task has been completed for " + i + " nations -> running DynamicSignRunnable task next !");
            try {
                DynamicSignRunnable dynamicSignRunnable = new DynamicSignRunnable();
                dynamicSignRunnable.startTask(Config.Runnables.dynamic_sign_updater_interval);
            } catch (Exception e) {
                Core.getInstance().debugLog("Error running DynamicSignRunnable task: " + e.getMessage());
            }

    }

    public void startTask(long hours) {
        //this.runTaskTimerAsynchronously(Core.getInstance(), 0L, hours * 60 * 60 * 20);
        if(Config.Runnables.start_on_server_start) {
            this.runTaskTimerAsynchronously(Core.getInstance(), 0L, hours * 60 * 60 * 20);
        }else {
            this.runTaskTimerAsynchronously(Core.getInstance(), hours * 60 * 60 * 20, hours * 60 * 60 * 20);
        }
    }

    public void stopTask() {
        this.cancel();
    }

    public Integer getHappiness(NationYML nation) {
        return 0;
    }

    public Integer getPopulationDensity(NationYML nation) {
        int population = nation.getStringList("villagers").size();
        int claims = nation.getStringList("territory").size();
        int housing_density = (int) (population / claims * 0.50);
        //Core.getInstance().debugLog("Population density for " + nation.getString("nationName") + " is " + housing_density + " a: " + population + " c: " + claims);
        return housing_density;
    }


}




