package net.bytebond.core.util.listeners;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerBlockEvents implements Listener {

    @Getter
    private static final PlayerBlockEvents instance = new PlayerBlockEvents();

    /*
     *  PlayerBlockEvents
     *  Handling the Place & Break events of Players
     *
     *  Comes before the regular Events, priority = highest
     *  Check for permissions (can place? is claimed?) here
     */


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        NationYML nation = new NationYML(player.getUniqueId());

        if(ClaimRegistry.getOwnerOfChunk(chunk) == null) {
            return;
        }

        Block targetBlock = event.getBlock();
        Chunk targetChunk = targetBlock.getChunk();
        NationYML targetNation = ClaimRegistry.getNation(targetChunk);

        if(ClaimRegistry.getOwnerOfChunk(chunk).equals(player.getUniqueId()) || nation.getStringList("territory").contains(targetChunk.getWorld().getName() + "," + targetChunk.getX() + "," + targetChunk.getZ())) {
            return;
        }
        /*********************
         * allyPermissions might need to be changed to allyBuilding in the near future ** !
         *********************/
        if(targetNation.getStringList("allied_nations").contains(nation.getString("nationName")) && targetNation.getBoolean("allyPermissions")) {
            return;
        }

        sendYouCannotxHere(player, "build", true, true);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        NationYML nation = new NationYML(player.getUniqueId());

        if(ClaimRegistry.getOwnerOfChunk(chunk) == null) {
            return;
        }

        Block targetBlock = event.getBlock();
        Chunk targetChunk = targetBlock.getChunk();
        NationYML targetNation = ClaimRegistry.getNation(targetChunk);

        if(ClaimRegistry.getOwnerOfChunk(chunk).equals(player.getUniqueId()) || nation.getStringList("territory").contains(targetChunk.getWorld().getName() + "," + targetChunk.getX() + "," + targetChunk.getZ())) {
            return;
        }
        /*********************
         * allyPermissions might need to be changed to allyBuilding in the near future ** !
         *********************/
        if(targetNation.getStringList("allied_nations").contains(nation.getString("nationName")) && targetNation.getBoolean("allyPermissions")) {
            return;
        }


        sendYouCannotxHere(player, "interact", true, true);
        event.setCancelled(true);

    }




    public void sendYouCannotxHere(Player player, String action, Boolean message, Boolean isOwned) {

       if(message) {
           Common.tellTimedNoPrefix(5, player, "&cYou cannot " + action + " here!" + (isOwned ? " This chunk is owned by someone else." : ""));
       } else {
           Remain.sendActionBar(player, "&cYou cannot " + action + " here!");
       }

    }

    public Boolean isAllied(NationYML nation, NationYML target) {
        return nation.getStringList("allied_nations").contains(target.getString("nationName"));
    }

    public Boolean allowsAllyBuild(NationYML nation, NationYML target) {
        return nation.getStringList("allied_nations").contains(target.getString("nationName")) && target.getBoolean("allyBuilding");
    }

}
