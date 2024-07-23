package net.bytebond.core.util.integrations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.bytebond.core.Core;
import net.bytebond.core.data.NationYML;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final Core plugin;

    public PlaceholderAPI(Core plugin) {
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
        return "Core";
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

        // If the placeholder is "nation_tag", return the nation tag of the player
        if ("nation_tag".equals(identifier)) {
            return playerNation != null ? playerNation.getString("TAG") : "";
        }

        // If the placeholder is "nation_population", return the population of the nation
        if ("nation_population".equals(identifier)) {
            return playerNation != null ? String.valueOf(playerNation.getStringList("villagers").size()) : "";
        }

        // If the placeholder is "nation_happiness", return the happiness of the nation
        if ("nation_happiness".equals(identifier)) {
            return playerNation != null ? String.valueOf(playerNation.getInteger("happiness")) : "";
        }

        // If the placeholder is "nation_tax", return the tax of the nation
        if ("nation_tax".equals(identifier)) {
            return playerNation != null ? String.valueOf(playerNation.getInteger("taxRate")) : "";
        }

        // If the placeholder is "nation_resource", return the resource of the nation
        if (identifier.startsWith("nation_resource_")) {
            String resource = identifier.split("_")[2];
            return playerNation != null ? String.valueOf(playerNation.getInteger(resource)) : "";
        }

        // Other conditions...

        return null;
    }

}