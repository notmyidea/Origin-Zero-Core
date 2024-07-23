package net.bytebond.core.util.integrations;

import net.bytebond.core.Core;
import org.bukkit.plugin.Plugin;
import org.dynmap.markers.MarkerAPI; // Import MarkerAPI
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import java.util.Arrays;

public class DynmapAPI {

    private final org.dynmap.DynmapAPI dynmapAPI;
    private MarkerSet markerSet; // Remove final keyword

    public DynmapAPI(Plugin plugin) {
        this.dynmapAPI = (org.dynmap.DynmapAPI) plugin.getServer().getPluginManager().getPlugin("dynmap");
        MarkerAPI markerAPI = dynmapAPI.getMarkerAPI(); // Replace core with dynmapAPI
        if (markerAPI != null) {
            this.markerSet = markerAPI.getMarkerSet("nations.markerset");
            if (this.markerSet == null) {
                this.markerSet = markerAPI.createMarkerSet("nations.markerset", "Nations", null, false);
            }
        }
    }

    public void addClaimToDynmap(String id, String label, double chunkX, double chunkZ, String worldName) {
        Core.getInstance().debugLog("Adding claim to Dynmap: " + id);
        Core.getInstance().debugLog("Chunk coordinates: x=" + chunkX + ", z=" + chunkZ);

        // Calculate the coordinates of the four corners of the chunk
        double[] x = {chunkX * 16, chunkX * 16, (chunkX + 1) * 16, (chunkX + 1) * 16};
        double[] z = {chunkZ * 16, (chunkZ + 1) * 16, (chunkZ + 1) * 16, chunkZ * 16};

        Core.getInstance().debugLog("Area coordinates: x=" + Arrays.toString(x) + ", z=" + Arrays.toString(z));

        if (markerSet == null) {
            Core.getInstance().debugLog("markerSet is null");
        } else {
            AreaMarker marker = markerSet.createAreaMarker(id, label, false, worldName, x, z, false);
            if (marker != null) {
                marker.setFillStyle(0.35, 0x00FF00);
                marker.setLineStyle(3, 1.0, 0xFF0000);
            } else {
                Core.getInstance().debugLog("Failed to create marker");
            }
        }
    }

    public void removeClaimFromDynmap(String id) {
        AreaMarker marker = markerSet.findAreaMarker(id);
        if (marker != null) {
            marker.deleteMarker();
        }
    }

    public MarkerSet getMarkerSet() {
        return this.markerSet;
    }



    public enum MainColor {
        RED("#FF0000"),
        GREEN("#00FF00"),
        BLUE("#0000FF"),
        YELLOW("#FFFF00"),
        CYAN("#00FFFF"),
        MAGENTA("#FF00FF"),
        WHITE("#FFFFFF"),
        BLACK("#000000");

        private final String hexValue;

        MainColor(String hexValue) {
            this.hexValue = hexValue;
        }

        public String getHexValue() {
            return hexValue;
        }

        @Override
        public String toString() {
            return this.name() + "(" + hexValue + ")";
        }

}


}