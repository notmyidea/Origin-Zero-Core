package net.bytebond.core.util.handler;

import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.Core;
import net.bytebond.core.data.NationYML;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Map;
import java.util.UUID;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityHandler implements Listener{

    @Getter
    private static final EntityHandler instance = new EntityHandler();

        @EventHandler
        public void onEntityDeath(@NotNull EntityDeathEvent event) {
            Entity entity = event.getEntity();
            if(entity.getType() == EntityType.VILLAGER){
                NBTEntity nbtEntity = new NBTEntity(entity);
                if(nbtEntity.hasKey("nation_owner")){
                    event.setDroppedExp(100);
                    event.getDrops().clear();
                    Core.getInstance().debugLog("Villager died");
                    UUID ownerUuid = UUID.fromString(nbtEntity.getString("nation_owner"));

                    Map<UUID, NationYML> nationsMap = NationYML.getNations();
                    for (Map.Entry<UUID, NationYML> entry : nationsMap.entrySet()) {
                        if (entry.getKey().equals(ownerUuid)) {
                            NationYML nation = entry.getValue();
                            if(nation.getStringList("villagers").contains(nbtEntity.getString("villager_message"))){
                                nation.getStringList("villagers").remove(nbtEntity.getString("villager_message"));
                            } else {
                                return;
                            }
                            Player player = Bukkit.getPlayer(ownerUuid);
                            Player killer = event.getEntity().getKiller();
                            Common.tellNoPrefix(player, "&cYour villager has died!");
                            if(killer != player) {
                                Common.tellNoPrefix(killer, "&aYou have killed a villager of the nation of " + nbtEntity.getString("nation") + "!");
                            }
                            if (nbtEntity.hasKey("bedrock_coords")) {
                                String bedrockCoords = nbtEntity.getString("bedrock_coords");
                                String[] coords = bedrockCoords.split(",");
                                Location bedrockLocation = new Location(entity.getWorld(), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
                                ItemStack bedrockStack = new ItemStack((ItemStack) bedrockLocation.getBlock());
                                NBTItem nbtItem = new NBTItem(bedrockStack);
                                nbtItem.setBoolean("villager_dead", true);
                                Block signBlock = bedrockLocation.getBlock().getRelative(BlockFace.UP);
                                Sign sign = (Sign) signBlock.getState();
                                String villagerName = sign.getLine(2);
                                sign.setLine(2, "'" + ChatColor.RED + villagerName + ChatColor.BLACK + "'");
                                sign.setLine(3 , "DEAD");
                                sign.update();
                                // Now you can use the 'nbtItem' object for further processing
                            }



                            break;
                        }
                    }

                    // Now you can use the 'nation' object for further processing
                }
            }
            return;
        }



}
