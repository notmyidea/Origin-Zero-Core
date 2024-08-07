package net.bytebond.core.util.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebond.core.Core;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.remain.Remain;

import java.util.Objects;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerMoveEvents implements Listener {

    @Getter
    private static final PlayerMoveEvents instance = new PlayerMoveEvents();

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk newChunk = event.getTo().getChunk();
        Chunk oldChunk = event.getFrom().getChunk();

        if(newChunk.equals(oldChunk)) {
            return;
        }

        NationPlayer nationPlayer = new NationPlayer(player);
        boolean inNation = nationPlayer.inNation();

        NationYML newNation = ClaimRegistry.getNation(newChunk);
        NationYML oldNation = ClaimRegistry.getNation(oldChunk);

        if(newNation == null && oldNation == null) {
            return;
        }

        if(newNation == null) {
            Remain.sendActionBar(player, "Welcome to Wilderness");
            return;
        }

        if(oldNation != null && newNation != null) {
            if(Objects.equals(newNation.getString("owner"), oldNation.getString("owner"))) {
                return;
            }
        }

        if(!inNation) {
            Remain.sendActionBar(player, "Welcome to &f" + newNation.getString("nationName"));
            return;
        }

        if(newNation.getStringList("allied_nations").contains(nationPlayer.getNation().getString("nationName"))) {
            Remain.sendActionBar(player, "Welcome to &a" + newNation.getString("nationName") + " &f(&aAlly&f)");
            return;
        }

        if(newNation.getStringList("enemy_nations").contains(nationPlayer.getNation().getString("nationName"))) {
            Remain.sendActionBar(player, "Welcome to &c" + newNation.getString("nationName") + " &f(&cEnemy&f)");
            return;
        }

        if(Objects.equals(newNation.getString("owner"), player.getUniqueId().toString())) {
            if(oldNation != null && Objects.equals(oldNation.getString("owner"), player.getUniqueId().toString())) {
                // Player is moving within their own territory, do not display a welcome message
                return;
            }
            Remain.sendActionBar(player, "Welcome to &a" + newNation.getString("nationName") + " &f(&aYou&f)");
            player.addAttachment(Core.getInstance(), "nation.inclaim", true);
            return;
        }

        if(oldNation != null && Objects.equals(newNation.getString("owner"), oldNation.getString("owner"))) {
            return;
        }

        if(oldNation != null && Objects.equals(oldNation.getString("owner"), player.getUniqueId().toString())) {
            PermissionAttachment attachment = player.addAttachment(Core.getInstance());
            attachment.unsetPermission("nation.inclaim");
        }

        Remain.sendActionBar(player, "Welcome to &f" + newNation.getString("nationName"));
    }
}