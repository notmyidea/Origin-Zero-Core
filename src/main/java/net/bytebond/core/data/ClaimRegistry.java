package net.bytebond.core.data;

import org.bukkit.Chunk;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClaimRegistry extends YamlConfig {

    private static final Map<Chunk, ClaimRegistry> claims = new HashMap<>();

    private String chunk;
    private String owner;

    public ClaimRegistry(Chunk chunk, UUID owner) {


        this.chunk = this.getString("chunk");
        this.owner = this.getString("owner");
        
    }

}