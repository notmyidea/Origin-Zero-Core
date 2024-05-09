package net.bytebond.core.data;

import lombok.*;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class NationData extends YamlConfig {

    private static Map<UUID, NationData> nationData = new HashMap<>();
    private static AtomicInteger nextID = new AtomicInteger(1);

    private String nationName;
    private Integer nationID;
    private UUID nationOwner; // STORE AS UUID !!!!!!!!!

    private NationData(String nationName, Integer nationID, UUID nationOwner) {
        this.nationName = nationName;
        this.nationID = nationID;
        this.nationOwner = nationOwner;

        this.setHeader("Nation\nData");
        this.loadConfiguration(NO_DEFAULT, "nations/" + nationOwner + ".yml");

        this.set("nationName", nationName);
        this.set("nationID", nationID);
        this.set("nationOwner", nationOwner.toString());

        this.save();
    }

    @Override
    protected void onLoad() {
        this.nationName = this.getString("nationName");
        this.nationID = this.getInteger("nationID");
        String nationOwnerStr = this.getString("nationOwner");
        this.nationOwner = nationOwnerStr != null ? UUID.fromString(nationOwnerStr) : null;
    }

    public static NationData createNation(UUID nationOwner, String nationName) {
        int id = nextID.getAndIncrement();
        NationData nation = new NationData(nationName, id, nationOwner);
        nationData.put(nationOwner, nation);
        return nation;
    }

    public static NationData getNationData(UUID nationOwner) {
        return nationData.get(nationOwner);
    }


}
