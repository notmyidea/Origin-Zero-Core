package net.bytebond.core.util.runnables;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import net.bytebond.core.util.holograms.HologramHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.mineacademy.fo.remain.CompBarColor;
import org.mineacademy.fo.remain.CompBarStyle;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class HousingObjectManager {

    private static HousingObjectManager instance;

    public HousingObjectManager() {
    }

    public void runHousingObjectManager(Chunk chunk, Block block, NationYML nation, Player player, Block bedrock) {
        Villager villager = new Villager(nation, chunk, bedrock);
        Remain.sendBossbarTimed(player, "Spawning '" + villager.getVillagerName() + "'", 60, CompBarColor.GREEN, CompBarStyle.SEGMENTED_10);

    }




}
