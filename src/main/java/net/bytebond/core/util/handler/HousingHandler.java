package net.bytebond.core.util.handler;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTEntity;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.util.List;

public class HousingHandler {

    private static HousingHandler instance;

    public HousingHandler() {
    }


    public void runHousingHandler(Player player, Chunk chunk, Block block, NationYML nation) {
        NationPlayer nationPlayer = new NationPlayer(player);

        String blockString = block.getX() + "," + block.getY() + "," + block.getZ();
        String chunkString = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ() + "," + blockString;

        // Add the chunk to the nation's housing list
        List<String> housingList = nationPlayer.getNation().getStringList("housing");
        housingList.add(chunkString);
        nation.set("housing", housingList);
        nation.save();

        // Get the position of the sign
        block.setType(Material.BEDROCK);
        block.getRelative(BlockFace.UP).setType(Material.BEDROCK);
        Block signBlock = block.getRelative(BlockFace.UP);

        // Apply NBT data to the sign block
        NBTBlock nbtBlock = new NBTBlock(signBlock);
        nbtBlock.getData().setBoolean("isHousingSign", true);
        nbtBlock.getData().setString("housing_message", chunkString);

        signBlock.setType(Material.OAK_SIGN);
        Sign sign = (Sign) signBlock.getState();
        sign.setLine(0, "Housing Block");
        sign.setLine(1, "Owner: " + ChatColor.GREEN + nation.getString("TAG"));
        sign.setLine(2, "Happiness: " + ChatColor.GREEN + "73%");
        sign.setEditable(false);
        sign.update();
        Common.tellNoPrefix(player, "You have placed a housing block.");
        Villager villager = new Villager(nation, chunk, block);
        villager.runVillagerSpawn();

        nbtBlock.getData().setString("villager_id", villager.getVillager().getUniqueId().toString());
        nbtBlock.getData().setString("villager_name", villager.getVillagerName());
        nbtBlock.getData().setBoolean("villager_alive", true);

        NBTEntity nbtEntity = new NBTEntity(villager.getVillager());
        nbtEntity.setString("bedrock_coords", block.getX() + "," + block.getY() + "," + block.getZ());
        nbtEntity.setString("nation", nation.getString("nationName"));
        nbtEntity.setString("owner_uuid", nation.getString("owner"));
        nbtEntity.setString("chunk", chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ());
        nbtEntity.setString("villager_name", villager.getVillagerName());
        nbtEntity.setInteger("happiness", 73);

        sign.setLine(0, ChatColor.GREEN + nbtBlock.getData().getString("villager_name"));
        sign.update();

        // add the villager to the nation's list of villagers
        String villagerLocationString = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
        String villagerLocationStringWithName = villager.getVillagerName() + "," + villagerLocationString;
        List<String> villagersList = nationPlayer.getNation().getStringList("villagers");
        villagersList.add(villagerLocationStringWithName);
        nbtEntity.setString("villager_message", villagerLocationStringWithName);
        nation.set("villagers", villagersList);
        nation.save();

        NBTBlock nbtblock = new NBTBlock(block);
        nbtblock.getData().setString("villager_id", villager.getVillager().getUniqueId().toString());
        nbtblock.getData().setString("villager_message", villagerLocationStringWithName);

    }


}
