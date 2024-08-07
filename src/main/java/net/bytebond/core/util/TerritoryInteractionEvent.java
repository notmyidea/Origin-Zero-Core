package net.bytebond.core.util;


import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.Core;
import net.bytebond.core.data.*;
import net.bytebond.core.settings.Config;
//import org.bukkit.*;
//import org.bukkit.block.*;
//import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.remain.Remain;

import java.util.*;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TerritoryInteractionEvent implements Listener {

    @Getter
    private static final TerritoryInteractionEvent instance = new TerritoryInteractionEvent();

    public Boolean isPlayerInNation(Player player) {
        NationYML nation = new NationYML(player.getUniqueId());
        if (nation.isSet("nationName")) {
            return true;
        }
        return false;
    }


    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        if (!ClaimRegistry.doesClaimExist(chunk)) {
            return;
        }

        NationYML chunkNation = ClaimRegistry.getNation(chunk);
        if (chunkNation == null) {
            Common.tell(player, "returned because chunkNation == null");
            return;
        }

        if(chunkNation.getString("owner").equals(player.getUniqueId().toString()) && event.getBlock().getType() == Material.OAK_SIGN) {
            Block signBlock = event.getBlock();
            NBTBlock nbtBlock = new NBTBlock(signBlock);

            // Check if the sign block has the NBT data
            if (nbtBlock.getData().hasKey("isHousingSign") && nbtBlock.getData().getBoolean("isHousingSign") && !nbtBlock.getData().getBoolean("isDrillSign")) {

                Common.tellNoPrefix(player, "You have removed your housing block.");
                NationYML nation = new NationYML(player.getUniqueId());


                // Get the housing_message from the NBT data and remove it from the housing list
                String housingMessage = nbtBlock.getData().getString("housing_message");
                List<String> housingList = nation.getStringList("housing");
                if (housingList.isEmpty()) {
                    return;
                }
                if (housingList.contains(housingMessage)) {
                    housingList.remove(housingMessage);
                    nation.set("housing", housingList);
                    nation.save();
                }

                // Get the villager_message from the NBT data and remove it from the villagers list
                String villagerMessage = nbtBlock.getData().getString("villager_message");
                List<String> villagersList = nation.getStringList("villagers");
                if (villagersList.isEmpty()) {
                    //return;
                }
                if (villagersList.contains(villagerMessage)) {
                    villagersList.remove(villagerMessage);
                    nation.set("villagers", villagersList);
                    nation.save();
                    }
                }


                if(nbtBlock.getData().hasKey("villager_id") && nbtBlock.getData().hasKey("villager_name") && nbtBlock.getData().getBoolean("villager_alive")) {
                    String villagerId = nbtBlock.getData().getString("villager_id");
                    Entity entity = Bukkit.getEntity(UUID.fromString(villagerId));
                    if(entity != null) {
                        entity.remove();
                    }
                }


                if(nbtBlock.getData().getBoolean("isDrillSign") && nbtBlock.getData().getString("drillType") != null) {
                    Common.tellNoPrefix(player, "You have removed your drill.");
                    NationYML nation = new NationYML(player.getUniqueId());

                    // Get the drillSetMessage from the NBT data and remove it from the drills list
                    String drillSetMessage = nbtBlock.getData().getString("drillSetMessage");
                    List<String> drillList = nation.getStringList("drills");
                    if (drillList.isEmpty()) {
                        return;
                    }
                    if (drillList.contains(drillSetMessage)) {
                        drillList.remove(drillSetMessage);
                        nation.set("drills", drillList);
                        nation.save();
                    }
                    // Remove the drill from the chunks drill list
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(ClaimRegistry.getClaimFile(chunk));
                    if(yamlConfiguration.isSet("drills")) {
                        List<String> chunkDrillList = yamlConfiguration.getStringList("drills");
                        if (chunkDrillList.contains(drillSetMessage)) {
                            chunkDrillList.remove(drillSetMessage);
                            yamlConfiguration.set("drills", chunkDrillList);
                        }
                    }



                    ItemManager.giveDrill(player, Drill.DrillType.valueOf(nbtBlock.getData().getString("drillType")), true);
                } else {

                    NationPlayer nationPlayer = new NationPlayer(player);
                    NationYML nation = nationPlayer.getNation();

                    // remove the "villager" from the nations list of villagers
                    NBTBlock bedrock = new NBTBlock(event.getBlock().getRelative(BlockFace.DOWN));

                    String villagerLocationStringWithName = bedrock.getData().getString("villager_message");
                    List<String> villagersList = nationPlayer.getNation().getStringList("villagers");
                    villagersList.remove(villagerLocationStringWithName);
                    nation.set("villagers", villagersList);
                    nation.save();

                    ItemManager.giveHousingObject(player, true);
                }






                signBlock.setType(Material.AIR);
                signBlock.getRelative(BlockFace.DOWN).setType(Material.AIR);


                //nation.set("villagers", villagersList);
                //nation.save();

                /*if (villagerName != null) {
                    Villager villager = Villager.findVillager(chunk, nation);
                    if (villager != null && villager.getVillagerName().equals(villagerName)) {
                        villager.removeVillagerInChunk(chunk, nation);
                    }
                }*/

            }

        if (!isPlayerInNation(player)) {
            event.setCancelled(true);
            //player.sendMessage("§cYou cannot break blocks here, this chunk is claimed.");
            return;
        }
    }

    //@EventHandler
    public void onBlockBuildEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();
        Block block = event.getBlock();

        if (!ClaimRegistry.doesClaimExist(chunk)) {
            return;
        }

        NationYML chunkNation = ClaimRegistry.getNation(chunk);
        if (chunkNation == null) {
            return;
        }

        if (!isPlayerInNation(player)) {
            event.setCancelled(true);
            //player.sendMessage("§cYou cannot build here, this chunk is claimed.");
            return;
        }

        NationYML playerNation = new NationYML(player.getUniqueId());

        if (chunkNation.getStringList("allied_nations").contains(playerNation.getString("nationName"))) {
            if (chunkNation.getBoolean("allyPermissions")) {
                return;
            }
            event.setCancelled(true);
        }

        if (!chunkNation.getString("owner").equals(player.getUniqueId().toString())) {
            event.setCancelled(true);
            //player.sendMessage("§cYou cannot build here. This territory belongs to another nation.");
        }

        //event.setCancelled(true);
        //player.sendMessage("§cYou cannot build here. This territory belongs to another nation.");
    }


    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        UUID UUID = player.getUniqueId();


        if (itemInHand.hasItemMeta()) {
            ItemMeta meta = itemInHand.getItemMeta();
            // 22222 == housing block
            assert meta != null;
            if (!(meta.hasCustomModelData())) {
                return;
            }

            //if (meta.getCustomModelData() == 22222) {
            if(meta.getCustomModelData() == 1111) {
                Chunk chunk = event.getBlock().getChunk();
                NationPlayer nationPlayer = new NationPlayer(player);

                if (!(nationPlayer.inNation())) {
                    event.setCancelled(true);
                    Common.tellNoPrefix(player, "You are not in a nation.");
                    return;
                }

                if (!(nationPlayer.inNation())) {
                    event.setCancelled(true);
                    Common.tellNoPrefix(player, "You are not in a nation.");
                    return;
                }


                if (!(ClaimRegistry.doesClaimExist(chunk))) {
                    event.setCancelled(true);
                    Common.tellTimedNoPrefix(5, player, "You can only place a housing block in a chunk owned by your nation!");
                    return;
                }
                NationYML nation = new NationYML(UUID);

                if (ClaimRegistry.getOwnerOfChunk(chunk) == null) {
                    event.setCancelled(true);
                    Common.tellTimedNoPrefix(5, player, "You can only place a housing block in a chunk owned by your nation!");
                    return;
                }

                if (Objects.equals(ClaimRegistry.getOwnerOfChunk(chunk), nationPlayer.getNation().getString("owner"))) {

                    if (checkForHousingBlockInChunk(chunk, nation)) {
                        event.setCancelled(true);
                        Common.tellTimedNoPrefix(5, player, "You already reached the maximum amount of housing blocks in this chunk!");
                        //Common.tellNoPrefix(player, "There is already a housing block in this chunk!");
                        return;
                    }
                    HousingObjectManager housingObjectManager = new HousingObjectManager();
                    housingObjectManager.runHousingObjectManager(chunk, event.getBlock(), nation, player, event.getBlock().getRelative(BlockFace.DOWN));


                    Chunk blockChunk = event.getBlock().getChunk();
                    Block block = event.getBlock();
                    String blockString = block.getX() + "," + block.getY() + "," + block.getZ();
                    String chunkString = blockChunk.getWorld().getName() + "," + blockChunk.getX() + "," + blockChunk.getZ() + "," + blockString;


                    List<String> housingList = nationPlayer.getNation().getStringList("housing");
                    housingList.add(chunkString);
                    nation.set("housing", housingList);
                    nation.save();

                    Block bedrockBlock = event.getBlock();
                    bedrockBlock.setType(Material.BEDROCK);
                    bedrockBlock.getRelative(BlockFace.UP).setType(Material.BEDROCK);
                    Block signBlock = bedrockBlock.getRelative(BlockFace.UP);

                    // Apply NBT data to the sign block
                    NBTBlock nbtBlock = new NBTBlock(signBlock);
                    nbtBlock.getData().setBoolean("isHousingSign", true);
                    nbtBlock.getData().setString("housing_message", chunkString);

                    signBlock.setType(Material.OAK_SIGN);
                    Sign sign = (Sign) signBlock.getState();
                    sign.setLine(0, "Housing Block");
                    sign.setLine(1, "Owner: " + ChatColor.GRAY + nation.getString("TAG"));
                    sign.setLine(2, "Villagers: " + ChatColor.GRAY + "0");
                    sign.setEditable(false);
                    sign.update();
                    Common.tellNoPrefix(player, "You have placed a housing block.");
                    Villager villager = new Villager(nation, chunk, bedrockBlock);


                    new BukkitRunnable() {
                        int counter = 1 * 60; // 4 minutes in seconds
                        int bugCounter = 0;

                        @Override
                        public void run() {
                            if (counter > 0) {
                                if (bedrockBlock.getType() == Material.AIR || signBlock.getType() == Material.AIR) {
                                    this.cancel();
                                    Core.getInstance().debugLog("Cancelled the villager spawn because the bedrock or sign block was removed.");
                                    //ItemManager.giveHousingObject(player, true);
                                    bugCounter++;
                                    return;
                                }
                                Sign sign = (Sign) signBlock.getState();
                                sign.setLine(3, "Spawning in: " + counter + " seconds");
                                sign.update();
                                counter--;
                            } else {
                                if (bedrockBlock.getType() == Material.AIR || signBlock.getType() == Material.AIR) {
                                    this.cancel();
                                    Core.getInstance().debugLog("Cancelled the villager spawn because the bedrock or sign block was removed.");
                                    //ItemManager.giveHousingObject(player, true);
                                    if (bugCounter != 0) {
                                        this.cancel();
                                        return;
                                    }
                                }
                                int villager_spawns = 0;
                                if (villager_spawns == 0) {
                                    villager.runVillagerSpawn();
                                    villager_spawns++;
                                }
                                NBTBlock nbtBlock1 = new NBTBlock(bedrockBlock);
                                nbtBlock1.getData().setString("villager_id", villager.getVillager().getUniqueId().toString());

                                NBTEntity nbtEntity = new NBTEntity(villager.getVillager());
                                nbtEntity.setString("bedrock_coords", bedrockBlock.getX() + "," + bedrockBlock.getY() + "," + bedrockBlock.getZ());
                                nbtEntity.setString("nation", nation.getString("nationName"));
                                nbtEntity.setString("owner_uuid", nation.getString("owner"));
                                nbtEntity.setString("chunk", chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ());
                                nbtEntity.setString("villager_name", villager.getVillagerName());
                                nbtEntity.setInteger("happiness", 73);

                                if (bedrockBlock.getType() == Material.AIR || signBlock.getType() == Material.AIR) {
                                    this.cancel();
                                    Core.getInstance().debugLog("Cancelled the villager spawn because the bedrock or sign block was removed.");
                                    //ItemManager.giveHousingObject(player, true);
                                    return;
                                }
                                if (signBlock == null) {
                                    this.cancel();
                                    Core.getInstance().debugLog("Cancelled the villager spawn because the sign block was removed.");
                                    return;
                                }
                                Sign sign = (Sign) signBlock.getState();
                                sign.setLine(1, "Villager: ");
                                String villagerName = villager.getVillagerName();
                                sign.setLine(2, "'" + ChatColor.GREEN + villagerName + ChatColor.BLACK + "'");
                                sign.setLine(3, "Happiness" + ChatColor.GREEN + " 73%");
                                sign.update();

                                String villagerLocationString = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
                                String villagerLocationStringWithName = villager.getVillagerName() + "," + villagerLocationString;
                                List<String> villagersList = nationPlayer.getNation().getStringList("villagers");
                                villagersList.add(villagerLocationStringWithName);
                                nbtEntity.setString("villager_message", villagerLocationStringWithName);
                                nation.set("villagers", villagersList);
                                nation.save();
                                NBTBlock nbtBlock = new NBTBlock(sign.getBlock());
                                nbtBlock.getData().setString("villager_message", villagerLocationStringWithName);

                                    /*Villager villager = Villager
                                    if (villager != null) {
                                        Common.tell(player, villager.getVillagerName());
                                    } else {
                                        Common.tell(player, "null");
                                    }*/

                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Core.getInstance(), 0, 20); // run every second (20 ticks = 1 second)
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(block == null || block.getType() == Material.AIR) {
            return;
        }

        // Check if the player has interacted with a chest or any kind of sign
        if ((block.getType() == Material.CHEST) || (block.getType() == Material.TRAPPED_CHEST) || (block.getType() == Material.FURNACE) || (block.getType() == Material.BARREL)
                || (block.getType() == Material.OAK_SIGN)
                || (block.getType() == Material.OAK_WALL_SIGN)
                || (block.getType() == Material.BAMBOO_SIGN)
                || (block.getType() == Material.BAMBOO_WALL_SIGN)
                || (block.getType() == Material.ACACIA_SIGN)
                || (block.getType() == Material.ACACIA_WALL_SIGN)
                || (block.getType() == Material.BIRCH_SIGN)
                || (block.getType() == Material.BIRCH_WALL_SIGN)
                || (block.getType() == Material.CRIMSON_SIGN)
                || (block.getType() == Material.CRIMSON_WALL_SIGN)
                || (block.getType() == Material.DARK_OAK_SIGN)
                || (block.getType() == Material.DARK_OAK_WALL_SIGN)
                || (block.getType() == Material.JUNGLE_SIGN)
                || (block.getType() == Material.JUNGLE_WALL_SIGN)
                || (block.getType() == Material.SPRUCE_SIGN)
                || (block.getType() == Material.SPRUCE_WALL_SIGN)
                || (block.getType() == Material.WARPED_SIGN)
                || (block.getType() == Material.WARPED_WALL_SIGN)
        ){
            Chunk chunk = block.getChunk();

            // Check if the chunk is claimed
            if (!ClaimRegistry.doesClaimExist(chunk)) {
                return;
            }

            NationYML chunkNation = ClaimRegistry.getNation(chunk);
            if (chunkNation == null) {
                return;
            }

            // Check if the player is in a nation
            if (!isPlayerInNation(player)) {
                event.setCancelled(true);
                sendYouCannotxHere(player, "interact", true, true);
                return;
            }

            NationYML playerNation = new NationYML(player.getUniqueId());

            // Check if the player is in the nation that owns the chunk
            if (chunkNation.getString("nationName").equals(playerNation.getString("nationName"))) {
                return;
            }

            event.setCancelled(true);
            sendYouCannotxHere(player, "interact", true, true);

        }
    }

    public void sendYouCannotxHere(Player player, String action, Boolean message, Boolean isOwned) {

        if(message) {
            Common.tellTimedNoPrefix(5, player, "&cYou cannot " + action + " here!" + (isOwned ? " This chunk is owned by someone else." : ""));
        } else {
            Remain.sendActionBar(player, "&cYou cannot " + action + " here!");
        }

    }

    public Boolean checkForHousingBlockInChunk(Chunk chunk, NationYML nation) {
        List<String> housingList = nation.getStringList("housing");
        int count = 0;
        for (String housing : housingList) {
            String[] split = housing.split(",");
            if (split[0].equals(chunk.getWorld().getName()) && Integer.parseInt(split[1]) == chunk.getX() && Integer.parseInt(split[2]) == chunk.getZ()) {
                count++;
            }
            if(count >= Config.Housing.max_houses) {
                return false;
            }
        }
        return (!(count < Config.Housing.max_housing_per_chunk));
    }


    @Deprecated
    public Integer getHousingBlocksInChunk(Chunk chunk, NationYML nation) {
        List<String> housingList = nation.getStringList("housing");
        Integer count = 0;
        for (String housing : housingList) {
            String[] split = housing.split(",");
            if (split[0].equals(chunk.getWorld().getName()) && Integer.parseInt(split[1]) == chunk.getX() && Integer.parseInt(split[2]) == chunk.getZ()) {
                count++;
            }
        }
        return count;
    }



}
