package net.bytebond.core.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.bytebond.core.Core;
import net.bytebond.core.data.NationYML;
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
        // please wrk
        System.out.println("point 1");
        // If the placeholder is "nation_name", return the nation name of the player
        if ("nation_name".equals(identifier)) {
            UUID playerUUID = player.getUniqueId();
            Map<UUID, NationYML> nationsMap = NationYML.getNations();

            String nationName = nationsMap.values().stream()
                    .filter(nation -> playerUUID.equals(nation.getString("owner")))
                    .map(NationYML::getNationName)
                    .findFirst()
                    .orElse("");
            // please work
            System.out.println("point 2 -> " + nationName);
            return nationName;
        }

        return null;
    }

}