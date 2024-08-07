package net.bytebond.core.data;

import net.bytebond.core.Core;

import java.util.*;

public class HashMan {

    public HashMap<NationYML, String> nationMap = new HashMap<>();
    public HashMap<NationYML, String> allNationMap = new HashMap<>();

    private static HashMan instance;
    private HashMan hashMan;

    public HashMan() {
        this.hashMan = this;
    }

    public static HashMan getInstance() {
        if (instance == null) {
            instance = new HashMan();
        }
        return instance;
    }

    public void addNation(NationYML nation, UUID owner) {
        try {
            nationMap.put(nation, owner.toString());
            Core.getInstance().debugLog("NationMap size: " + nationMap.size());
            Core.getInstance().debugLog("NationMap: " + nationMap.toString());
        } catch (Exception e) {
            Core.getInstance().debugLog("Could not add Nation to internal caching system: [" + e.getMessage() + "]");
        }
    }

    public void removeNation(NationYML nation) {
        nationMap.remove(nation, nation.getString("nationName"));
    }

    public HashMap<NationYML, String> getNationMap() {
        return nationMap;
    }

    public HashMap<NationYML, String> getAllNationMap() {
        return allNationMap;
    }

    public List<String> getAllNationNames() {
        Core.getInstance().debugLog("NationMap size before getting all nation names: " + getInstance().nationMap.size() + " 2: " + getNationMap().size());
        Core.getInstance().debugLog("NationMap before getting all nation names: " + getInstance().nationMap.toString() + "  2: " + getNationMap().toString());
        List<String> nationNames = new ArrayList<>();
        for (NationYML nation : nationMap.keySet()) {
            nationNames.add(nation.getString("nationName"));
        }
        return nationNames;
    }


    public void log(int i, String nationName) {
        switch (i){
            case 1:
                Core.getInstance().debugLog("Added Nation to internal caching system");
                Core.getInstance().debugLog("Nation: " + nationName);
                Core.getInstance().debugLog("Action: " + "PlayerJoinLeaveEvent");
                break;
            case 2:
                Core.getInstance().debugLog("Removed Nation from internal caching system");
                Core.getInstance().debugLog("Nation: " + nationName);
                Core.getInstance().debugLog("Action: " + "PlayerJoinLeaveEvent");
                break;
            case 3:
                Core.getInstance().debugLog("Added Nation to internal caching system");
                Core.getInstance().debugLog("Nation: " + nationName);
                Core.getInstance().debugLog("Action: " + "Nation Creation");
                break;
            case 4:
                Core.getInstance().debugLog("Removed Nation from internal caching system");
                Core.getInstance().debugLog("Nation: " + nationName);
                Core.getInstance().debugLog("Action: " + "Nation Removal");
                break;
        }
    }

    public void repopulateAllNationMap() {
        getAllNationMap().forEach((k, v) -> allNationMap.remove(k, v));
        try {
            for (NationYML nation : NationYML.getNations().values()) {
                getAllNationMap().put(nation, nation.getString("nationName"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeNationFromAllNationMap(NationYML nation) {
        getAllNationMap().remove(nation, nation.getString("nationName"));
    }

    public void addNationToAllNationMap(NationYML nation) {
        getAllNationMap().put(nation, nation.getString("nationName"));
    }

    public ArrayList<String> getAllNationNamesFromAllNationMap() {
        ArrayList<String> nationNames = new ArrayList<>();
        for (NationYML nation : allNationMap.keySet()) {
            nationNames.add(nation.getString("nationName"));
        }
        return nationNames;
    }

}
