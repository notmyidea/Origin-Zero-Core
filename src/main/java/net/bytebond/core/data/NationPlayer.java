package net.bytebond.core.data;

import net.bytebond.core.settings.Messages;
import org.bukkit.entity.Player;

import java.util.Objects;

public class NationPlayer {
    private Player player;

    public NationPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean inNation() {

        if(NationYML.getNations().isEmpty()) {
            return false;
        }

        return NationYML.getNations().values().stream()
                .filter(Objects::nonNull)
                .anyMatch(nation -> {
                    String owner = nation.getString("owner");
                    return owner != null && owner.equals(player.getUniqueId().toString());
                });
    }

    public NationYML getNation() {
        return NationYML.getNations().values().stream()
                .filter(nation -> {
                    String owner = nation.getString("owner");
                    return owner != null && owner.equals(player.getUniqueId().toString());
                })
                .findFirst()
                .orElse(null);
    }

}
