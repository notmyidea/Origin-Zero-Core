package net.bytebond.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.mineacademy.fo.annotation.AutoRegister;

//@AutoRegister
//@NoArgsConstructor(access = AccessLevel.PRIVATE) //implements Listener
public final class BlockBreakBuildEvent{

    // Modified plugin Blocks:
    // - Housing Object (Chest) (ItemID: 22222)
    // - Drill (Obsidian) (ItemID: 22223 - 22226)

    /*@EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Player player = (Player) event.getPlayer();




        }*/



    private Boolean checkNation(Player player) {
        NationPlayer nationPlayer = new NationPlayer(player);
        if(nationPlayer.inNation()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isOnNationTerritory(Player player) {
        if(checkNation(player)) {

            if (ClaimRegistry.doesClaimExist(player.getLocation().getChunk())) {
                if(ClaimRegistry.getNation(player.getLocation().getChunk()).getString("nationName") == new NationPlayer(player).getNation().getString("nationName")) {
                    return true;
                } else {
                    // not his nation
                    return false;
                }

            } else {
             // chunk isnt claimed
                return false;
            }

        }
        // not in nation
    return false;
    }



}
