package net.bytebond.core.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.bytebond.core.Core;
import net.bytebond.core.data.NationYML;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class Placeholders extends PlaceholderExpansion {

    private final Core plugin;

    public Placeholders(Core plugin) {
        this.plugin = plugin;
    }


    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); //
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "Nations";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        Map<UUID, NationYML> nationsMap = NationYML.getNations();
        UUID playerUUID = player.getUniqueId();
        NationYML playerNation = nationsMap.get(playerUUID);

        // If the placeholder is "nation_name", return the nation name of the player
        if ("nation_name".equals(identifier)) {
            return playerNation != null ? playerNation.getString("nationName") : "";
        }
        if ("nation_tag".equals(identifier)) {
            return playerNation != null ? playerNation.getString("TAG") : "";
        }

        // If the placeholder is "isAllied" or "isEnemy", return whether the player is allied or enemy with the target player
        if (identifier.startsWith("isAllied_") || identifier.startsWith("isEnemy_")) {
            String targetPlayerName = identifier.split("_")[1];
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetPlayerName);
            if (targetPlayer == null) {
                return "";
            }

            UUID targetPlayerUUID = targetPlayer.getUniqueId();
            NationYML targetPlayerNation = nationsMap.get(targetPlayerUUID);
            if (targetPlayerNation == null) {
                return "";
            }

            String targetNationName = targetPlayerNation.getString("nationName");

            if ("isAllied_".concat(targetPlayerName).equals(identifier)) {
                return playerNation.getStringList("allied_nations").contains(targetNationName) ? "Yes" : "No";
            }

            if ("isEnemy_".concat(targetPlayerName).equals(identifier)) {
                return playerNation.getStringList("enemy_nations").contains(targetNationName) ? "Yes" : "No";
            }
        }

        return null;
    }

}