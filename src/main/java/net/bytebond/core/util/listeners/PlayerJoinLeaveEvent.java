package net.bytebond.core.util.listeners;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class PlayerJoinLeaveEvent implements Listener {

    private PlayerJoinLeaveEvent instance;
    private HashMan hashMan = HashMan.getInstance();
    public PlayerJoinLeaveEvent() {
    }

    public PlayerJoinLeaveEvent getInstance() {
        return instance;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
       Player player = event.getPlayer();
       NationPlayer nationPlayer = new NationPlayer(player);

       if(nationPlayer.inNation()) {
           hashMan.addNation(nationPlayer.getNation(), player.getUniqueId());
           hashMan.log(1, nationPlayer.getNation().getString("nationName"));
           return;
       }
       Core.getInstance().debugLog("Could not add Nation to internal caching system. Player is not in a Nation.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
       Player player = event.getPlayer();
       NationPlayer nationPlayer = new NationPlayer(player);

       if(nationPlayer.inNation()) {
              hashMan.removeNation(nationPlayer.getNation());
              hashMan.log(2, nationPlayer.getNation().getString("nationName"));
              return;
       }
        Core.getInstance().debugLog("Could not remove Nation from internal caching system. Player is not in a Nation.");
    }



}
