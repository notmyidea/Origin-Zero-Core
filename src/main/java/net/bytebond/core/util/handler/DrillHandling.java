package net.bytebond.core.util.handler;

import de.tr7zw.nbtapi.NBTBlock;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.Drill;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.util.io.Check;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DrillHandling {



    public String dataFolder = "/plugins/Core/data/drills";


    public void newDrill(Chunk chunk, Location location, Drill.DrillType drillType, NationYML nation) {
        Check.getInstance().checkDirectory("data/drills");

        String fileName = "drill_" + drillType.toString() + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + ".yml";
        File drillFile = new File(dataFolder, fileName);
        if (!drillFile.exists()) {
            try {
                drillFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration drillConfig = YamlConfiguration.loadConfiguration(drillFile);
        drillConfig.set("chunk", chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ());
        drillConfig.set("drilltype", drillType.toString());
        drillConfig.set("location", chunk.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
        drillConfig.set("nation", nation.getString("nationName"));
        drillConfig.set("type", drillType.toString());

        try {
            drillConfig.save(drillFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // save the drill to the nation's drill list
        List<String> drillList = nation.getStringList("drills");
        drillList.add(fileName);
        nation.set("drills", drillList);
        nation.save();

        // save the drill to the chunk's drill list
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(ClaimRegistry.getClaimFile(chunk));
        if(yamlConfiguration.isSet("drills")) {
            List<String> chunkDrillList = yamlConfiguration.getStringList("drills");
            chunkDrillList.add(fileName);
            yamlConfiguration.set("drills", chunkDrillList);
        } else {
            yamlConfiguration.set("drills", Arrays.asList(fileName));
        }
        Block bedrock = location.getBlock();
        if (bedrock.getType() == Material.CHEST) {
            bedrock.setType(Material.BEDROCK);
            NBTBlock nbtBlock = new NBTBlock(bedrock);
            nbtBlock.getData().setBoolean("isDrill", true);
            nbtBlock.getData().setString("drillType", drillType.toString());
            nbtBlock.getData().setString("drillOwner", nation.getString("nationName"));
            nbtBlock.getData().setString("drillSetMessage", fileName);
        }

        Block sign = location.getBlock().getRelative(BlockFace.UP);
        sign.setType(Material.OAK_SIGN);
        NBTBlock nbtBlock = new NBTBlock(sign);
        nbtBlock.getData().setBoolean("isDrillSign", true);
        nbtBlock.getData().setBoolean("isDrill", true);
        nbtBlock.getData().setString("drillType", drillType.toString());
        nbtBlock.getData().setString("drillOwner", nation.getString("nationName"));
        nbtBlock.getData().setString("drillSetMessage", fileName);

        Sign signState = (Sign) sign.getState();
        signState.setLine(0, "§6" + drillType.toString() + "§f-Drill");
        signState.setLine(1, "Line 2 text");
        signState.setLine(2, "Line 3 text");
        signState.setLine(3, "Line 4 text");
        signState.update();
        signState.setEditable(false);

    }

    public List<File> getAllDrillFiles() {
        Check.getInstance().checkDirectory("data/drills");
        File getAllDrillFilesDataFolder = new File(dataFolder);
        File[] files = getAllDrillFilesDataFolder.listFiles();
        if(files == null) {
            return null;
        }
        return Arrays.asList(files);
    }


    public File getDrill(Location location) {
        Check.getInstance().checkDirectory("data/drills");
        String locationString = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
        for (File file : getAllDrillFiles()) {
            FileConfiguration drillConfig = YamlConfiguration.loadConfiguration(file);
            if (drillConfig.getString("location").equals(locationString)) {
                return file;
            }
        }
        return null;
    }

    public Boolean checkDrillLocation(Chunk chunk, Drill.DrillType drillType) {
        // Check if the Drills.drill.xx.location == chunk.getLocation



        return null;
    }


}
