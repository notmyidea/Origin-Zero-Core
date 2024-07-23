package net.bytebond.core.util.holograms;

import net.bytebond.core.data.Drill;
import net.bytebond.core.data.NationYML;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.mineacademy.fo.Common;

import java.awt.event.HierarchyListener;

public class HologramHandler {

    private static HologramHandler instance;

    public HologramHandler() {
    }

    public void createHologram(Block block, String message) {
        Hologram hologram = new Hologram(message.split("\n"));
        hologram.spawn(block.getLocation());
    }

    public void createHologramHousing(Block bedrock, NationYML nation) {

    }

    public void createHologramDrill(Drill.DrillType drillType, Block chest, NationYML nation) {

        if(drillType == Drill.DrillType.WOOD) {

        }

        if(drillType == Drill.DrillType.BRICK) {

        }


        if(drillType == Drill.DrillType.STONE) {

        }

        if(drillType == Drill.DrillType.OBSIDIAN) {

        }

        if(drillType == Drill.DrillType.DARKSTONE) {

        }

    }

    class Hologram {

        private final String[] lines;

        Hologram(String... lines) {
            this.lines = lines;
        }

        public void spawn(Location originLocation) {

            for (String line : lines) {
                ArmorStand stand = originLocation.getWorld().spawn(originLocation, ArmorStand.class);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setInvisible(true);
                stand.setCustomNameVisible(true);
                stand.setCustomName(Common.colorize(line));
                originLocation.subtract(0, 0.25, 0);
            }
        }
    }

    public void getHologram() {

    }


}
