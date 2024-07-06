package net.bytebond.core.util;


import de.tr7zw.nbtapi.plugin.NBTAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.Core;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Material.CHEST;

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
            return;
        }


        if (!isPlayerInNation(player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot break blocks here, this chunk is claimed.");
            return;
        }

        NationYML playerNation = new NationYML(player.getUniqueId());

        if (chunkNation.getString("nationName").equals(playerNation.getString("nationName"))) {
            return;
        }

        if (chunkNation.getStringList("allied_nations").contains(playerNation.getString("nationName"))) {
            if (chunkNation.getBoolean("allyPermissions")) {
                return;
            }
            event.setCancelled(true);
        }

        event.setCancelled(true);
        player.sendMessage("§cYou cannot break blocks here. This territory belongs to another nation.");
    }

    @EventHandler
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
            player.sendMessage("§cYou cannot build here, this chunk is claimed.");
            return;
        }

        NationYML playerNation = new NationYML(player.getUniqueId());

        if (chunkNation.getString("nationName").equals(playerNation.getString("nationName"))) {
            return;
        }

        if (chunkNation.getStringList("allied_nations").contains(playerNation.getString("nationName"))) {
            if (chunkNation.getBoolean("allyPermissions")) {
                return;
            }
            event.setCancelled(true);
        }

        event.setCancelled(true);
        player.sendMessage("§cYou cannot build here. This territory belongs to another nation.");
    }


    @EventHandler
    public void onBlockInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        // Check if the player is interacting with a block
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            // Check if the block is a chest
            if (block.getType() == CHEST) {
                // Get the chest
                Chest chest = (Chest) block.getState();
                // Wrap it in an NBTItem
                NBTItem nbtItem = new NBTItem(new ItemStack(block.getType()));

                // Check the NBT data and handle the interaction accordingly
                if (nbtItem.hasKey("drill")) {
                    // Handle drill chest interaction
                } else if (nbtItem.hasKey("tradingchest") && !player.isSneaking()) {
                    // Handle trading chest interaction
                }
            }
        }
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
            if(!(meta.hasCustomModelData())) {
                return;
            }

            if(meta.getCustomModelData() == 22222) {
                Chunk chunk = event.getBlock().getChunk();
                NationPlayer nationPlayer = new NationPlayer(player);

                if(!(nationPlayer.inNation())) {
                    event.setCancelled(true);
                    Common.tellNoPrefix(player, "You are not in a nation.");
                    return;
                }

                // Check if the chunk is claimed
                if (ClaimRegistry.doesClaimExist(chunk)) {
                    NationYML nation = new NationYML(UUID);

                    if(ClaimRegistry.getOwnerOfChunk(chunk) == null) {
                        event.setCancelled(true);Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");
                        Common.tell(player, "null");
                        return;
                    }

                    /*if(ClaimRegistry.getOwnerOfChunk(chunk).toString() != player.getUniqueId().toString()) {
                        event.setCancelled(true);Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");
                        Common.tell(player, "null 2");
                        return;
                    }*/

                    /*if(!(ClaimRegistry.getOwnerOfChunk(chunk).equals(nation.getString("owner")))) {
                        event.setCancelled(true);
                        Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");
                        return;
                    }*/

                    //if(ClaimRegistry.getOwnerOfChunk(chunk) == nation.getString("owner")) {
                    if(Objects.equals(ClaimRegistry.getOwnerOfChunk(chunk), nationPlayer.getNation().getString("owner"))) {

                        if (checkForHousingBlockInChunk(chunk, nation)) {
                            event.setCancelled(true);
                            Common.tellNoPrefix(player, "There is already a housing block in this chunk!");
                            return;
                        }

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
                        signBlock.setType(Material.OAK_SIGN);
                        Sign sign = (Sign) signBlock.getState();
                        sign.setLine(0, "Housing Block");
                        sign.setLine(1, "Owner: " + ChatColor.GRAY  + nation.getString("TAG"));
                        sign.setLine(2, "Villagers: " + ChatColor.GRAY  +  "0");
                        sign.setEditable(false);
                        sign.update();
                        Core.getInstance().debugLog("Started the process of spawning a villager in 4 minutes in:");
                        Core.getInstance().debugLog("World: " + chunk.getWorld().getName() + ", X: " + chunk.getX() + ", Z: " + chunk.getZ());
                        Core.getInstance().debugLog("Location: " + block.getX() + ", " + block.getY() + ", " + block.getZ());
                        Core.getInstance().debugLog("Nation: " + nation.getString("nationName"));

                        Villager villager = new Villager(nation, chunk);


                        new BukkitRunnable() {
                            int counter = 1 * 60; // 4 minutes in seconds

                            @Override
                            public void run() {
                                if (counter > 0) {
                                    Sign sign = (Sign) signBlock.getState();
                                    sign.setLine(3, "Spawning in: " + counter + " seconds");
                                    sign.update();

                                    counter--;
                                } else {

                                    villager.spawnVillager();
                                    Sign sign = (Sign) signBlock.getState();
                                    if(sign == null) {
                                        this.cancel();
                                        return;
                                    }
                                    sign.setLine(1, "Villager: ");
                                    String villagerName = villager.getVillagerName();
                                    sign.setLine(2, "'" + ChatColor.GREEN + villagerName + ChatColor.BLACK + "'");
                                    sign.setLine(3, "Happiness" + ChatColor.GREEN + " 73%");
                                    sign.update();
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Core.getInstance(), 0, 20); // run every second (20 ticks = 1 second)

                        }
                    event.setCancelled(true);Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");return;
                    } else {
                    event.setCancelled(true);Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");return;
                    }
                } else {
                    event.setCancelled(true);
                    Common.tellNoPrefix(player, "You can only place a housing block in a chunk owned by your nation!");
                    return;
                }
            }

        }

    @Deprecated
    public Boolean checkForHousingBlockInChunk(Chunk chunk, NationYML nation) {
        List<String> housingList = nation.getStringList("housing");
        for (String housing : housingList) {
            String[] split = housing.split(",");
            if (split[0].equals(chunk.getWorld().getName()) && Integer.parseInt(split[1]) == chunk.getX() && Integer.parseInt(split[2]) == chunk.getZ()) {
                return true;
            }
        }
        return false;
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
