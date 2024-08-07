package net.bytebond.core.util;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompBarColor;
import org.mineacademy.fo.remain.CompBarStyle;
import org.mineacademy.fo.remain.Remain;

public class HousingObjectManager {

    private static HousingObjectManager instance;

    public HousingObjectManager() {
    }

    public void runHousingObjectManager(Chunk chunk, Block block, NationYML nation, Player player, Block bedrock) {
        Villager villager = new Villager(nation, chunk, bedrock);
        //Remain.sendBossbarTimed(player, "Spawning '" + villager.getVillagerName() + "'", 60, CompBarColor.GREEN, CompBarStyle.SEGMENTED_10);

    }




}
