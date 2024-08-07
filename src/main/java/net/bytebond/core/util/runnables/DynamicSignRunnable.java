package net.bytebond.core.util.runnables;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.mineacademy.fo.model.SimpleRunnable;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DynamicSignRunnable extends SimpleRunnable {

    Boolean didStartup = false;


    @Override
    public void run() {
        if (!didStartup) {
            didStartup = true;
        }
        Core.getInstance().debugLog("Starting DynamicSignRunnable task...");
        Map<NationYML, String> nationMap = HashMan.getInstance().getNationMap();
        Integer i = 0;

        for (NationYML nation : nationMap.keySet()) {
            List<String> housingList = nation.getStringList("housing");
            Block bedrock = null;
            for (String housing : housingList) {
                String[] housingSplit = housing.split(",");
                String world = housingSplit[0];
                int x = Integer.parseInt(housingSplit[1]);
                int y = Integer.parseInt(housingSplit[2]);
                int z = Integer.parseInt(housingSplit[3]);
                // Get the block
                bedrock = Bukkit.getWorld(world).getBlockAt(x, y, z);
                if (bedrock.getType() != org.bukkit.Material.BEDROCK) {
                    Core.getInstance().debugLog("Error getting bedrock block for housing sign update.");
                    housingList.remove(housing);
                    return;
                }
                Sign sign = (Sign) bedrock.getRelative(BlockFace.UP).getState();
                sign.setLine(2, "Happiness: " + ChatColor.GREEN + nation.getInteger("happiness") + "%");
                sign.setLine(3, "Updated: " + ChatColor.GREEN + DateTimeFormatter.ofPattern("MM/dd/yyyy").format(java.time.LocalDateTime.now()));
                i++;
            }


        }
        Core.getInstance().debugLog("DynamicSignRunnable task has been completed for " + i + " housing signs.");
        TaxRunnable taxRunnable = new TaxRunnable();
        taxRunnable.startTask(24);
    }

    public void startTask(long hours) {
        //this.runTaskTimerAsynchronously(Core.getInstance(), 0L, hours * 60 * 60 * 20);
        if(Config.Runnables.start_on_server_start) {
            this.runTaskTimerAsynchronously(Core.getInstance(), 0L, hours * 60 * 60 * 20);
        } else {
            this.runTaskTimerAsynchronously(Core.getInstance(), hours * 60 * 60 * 20, hours * 60 * 60 * 20);
        }
    }

}
