package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.Core;
import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationPlayer;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Messages;
import net.bytebond.core.util.integrations.DynmapAPI;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.dynmap.markers.AreaMarker;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class NationsClaimManagerSubCommand extends SimpleSubCommand {

    public NationsClaimManagerSubCommand(SimpleCommandGroup parent) {
        super(parent, "territory");
        setPermission("nation.player");
        setDescription("Claim or unclaim a chunk of land for your faction");
        setUsage("<claim|unclaim>");
    }

    @Override
    protected void onCommand() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatDateTime = now.format(formatter);
        checkConsole();
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);
        Chunk chunk = player.getLocation().getChunk();
        String chunkStr = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();

        if(!(nation.isSet("nationName"))) {
            tellWarn(Messages.Nation.Creation.not_in_nation);
            return;
        }

        if(!(Config.Territory.Claiming.enabled)) {
            tellWarn(Messages.Nation.Claim.disabled);
            return;
        }

        //if(args.length == 0) {
            //returnTell(Messages.Nation.Claim.invalid_argument);
        //}
        if(args.length == 0 ) {
            tellInfo("&fYou must provide an argument. &7/nation territory <claim|unclaim>");
            return;
        }

        String firstArg = args[0];

        switch (firstArg) {
            case "claim":
                if(nation.isSet("territory")) {
                    if(nation.getStringList("territory").size() >= nation.getInteger("max_territory")) {
                        tellWarn(Messages.Nation.Claim.over_limit);
                        return;
                    }
                }
                /*Map<UUID, NationYML> nationsMap = NationYML.getNations();
                for(NationYML nations : nationsMap.values()) {
                    if(nations.isSet("territory")) {
                        if(nations.getStringList("territory").contains(chunkStr)) {
                            tellWarn(Messages.Nation.Claim.already_claimed.replace("{nation}", nations.getString("nationName")));
                            return;
                        }
                    }
                }*/

                Map<NationYML, String> nationsMap = HashMan.getInstance().getNationMap();
                for(NationYML nations : nationsMap.keySet()) {
                    if(nations.isSet("territory")) {
                        if(nations.getStringList("territory").contains(chunkStr)) {
                            tellWarn(Messages.Nation.Claim.already_claimed.replace("{nation}", nations.getString("nationName")));
                            return;
                        }
                    }
                }

                if(Config.Territory.Claiming.cost != 0) {
                    Integer cost = Config.Territory.Claiming.cost;
                        if(Core.getEconomy().getBalance(player) >= cost) {
                            Core.getEconomy().withdrawPlayer(player, cost);
                        } else {
                            tellWarn("&fYou do not have enough money to claim this chunk. &7$" + Config.Territory.Claiming.cost + " are required to claim this chunk.");
                            return;
                        }
                }


                // 1/2 Claim the chunk using the Nation territory method
                if(nation.isSet("territory")) {
                    List<String> territory = nation.getStringList("territory");
                    territory.add(chunkStr);
                    nation.set("territory", territory);
                } else {
                    List<String> territory = new ArrayList<>();
                    territory.add(chunkStr);
                    nation.set("territory", territory);
                }
                // 2/2 Claim the chunk using the ClaimRegistry method
                ClaimRegistry claim = new ClaimRegistry(chunk, UUID, nation.getString("nationName"), player.getName());
                claim.addHistory("&7" + player.getName() + "&f claimed this chunk for &7" + nation.getString("nationName") + "&f at &7"  + formatDateTime);
                claim.saveData();

                nation.save();
                tellSuccess(Messages.Nation.Claim.success);

                Core.getInstance().debugLog("Nation [" + nation.getString("nationName") + "] has claimed chunk " + chunk.toString());
                Core.getInstance().debugLog("Nations current list of claimed territory: " + nation.getStringList("territory"));
                Core.getInstance().debugLog("Chunk has been saved to " + chunkStr + ".yml");
                String colorName = nation.getString("MainColor");
                DynmapAPI.MainColor nationColor;
                if (colorName != null) {
                    nationColor = DynmapAPI.MainColor.valueOf(colorName);
                } else {
                    nationColor = DynmapAPI.MainColor.WHITE;
                }


                // Add the newly claimed territory to the Dynmap
                String[] split = chunkStr.split(",");
                String worldName = split[0];
                double x = Double.parseDouble(split[1]);
                double z = Double.parseDouble(split[2]);

                String id = nation.getString("nationName") + "_" + chunkStr;
                String label = "Territory of " + nation.getString("nationName");

                DynmapAPI dynmapIntegration = new DynmapAPI(Core.getInstance());
                dynmapIntegration.addClaimToDynmap(id, label, x, z, worldName);
                AreaMarker marker = dynmapIntegration.getMarkerSet().findAreaMarker(id);
                if (marker != null) {
                    // Convert the hex color to an RGB color and mask it to get a positive value
                    int rgbColor = Color.decode(nationColor.getHexValue()).getRGB() & 0xFFFFFF;
                    marker.setFillStyle(0.30, rgbColor);
                    marker.setLineStyle(0, 1.0, 0xFF0000);
                }
                break;
            case "unclaim":
                if(nation.isSet("territory")) {
                    List<String> territory = nation.getStringList("territory");
                    if(territory.contains(chunkStr)) {
                        territory.remove(chunkStr);
                        nation.set("territory", territory);
                        ClaimRegistry unclaim = new ClaimRegistry(chunk, null, null, player.getName());
                        if(unclaim != null) {
                            unclaim.addHistory("&7" + player.getName() + "&f unclaimed this chunk from &7" + nation.getString("nationName") + "&f at &7"  + formatDateTime);
                            unclaim.setOwner(null, null);
                            unclaim.saveData();
                        }
                        nation.save();
                        tellSuccess("&fChunk has been successfully unclaimed.");

                        // Remove the unclaimed territory from the Dynmap
                        String unclaimId = nation.getString("nationName") + "_" + chunkStr;
                        DynmapAPI unclaimDynmapIntegration = new DynmapAPI(Core.getInstance());
                        AreaMarker unclaimMarker = unclaimDynmapIntegration.getMarkerSet().findAreaMarker(unclaimId);
                        if (unclaimMarker != null) {
                            unclaimMarker.deleteMarker();
                        }
                    } else {
                        tellWarn("&fThis chunk is not claimed by your nation.");
                    }
                } else {
                    tellWarn("&fYour nation has not claimed any territories.");
                }
            case "transfer":
                if(nation.isSet("territory")) {
                    Chunk chunkRn = player.getLocation().getChunk();
                    NationYML nationRn = new NationYML(UUID);
                    if(args.length != 2) {
                        tellWarn("&fYou must specify a valid argument. &7/nation territory transfer <nation>");
                        return;
                    }
                    if(args[1].equalsIgnoreCase(nationRn.getString("nationName"))) {
                        tellWarn("&fYou cannot transfer a chunk to your own nation.");
                        return;
                    }
                    // Get the NationYML object of the new owner
                    NationYML newOwnerNation = null;
                    for (NationYML n : NationYML.getNations().values()) {
                        if (n.getString("nationName").equalsIgnoreCase(args[1])) {
                            newOwnerNation = n;
                            break;
                        }
                    }

                    if (newOwnerNation == null) {
                        tellWarn("&fNo nation found with the name " + args[1]);
                        return;
                    }

                    if(ClaimRegistry.doesClaimExist(chunkRn) || Objects.equals(ClaimRegistry.getOwnerOfChunk(chunk), nationRn.getString("owner"))) {
                        try {
                            ClaimRegistry.transferChunkOwnership(chunkRn, newOwnerNation, false, "Transferred to " + args[1]);
                            tellSuccess("&fChunk has been successfully transferred to &7" + args[1]);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                    } else {
                        tellWarn("&fYou do not own this chunk.");
                    }
                }
                break;
            default:
                tellWarn("&fInvalid argument, refer to the usage");
        }
    }

    @Override
    protected List<String> tabComplete() {
        if(!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                return completeLastWord("claim", "unclaim", "transfer");
            case 2:
                if(args[0].equalsIgnoreCase("transfer")) {
                    Map<UUID, NationYML> nations = NationYML.getNations();
                    List<String> nationNames = nations.values().stream()
                            .map(nation -> nation.getString("nationName"))
                            .collect(Collectors.toList());
                    NationYML nation = new NationYML(player.getUniqueId());
                    NationPlayer nationPlayer = new NationPlayer(player);
                    if(nationPlayer.inNation()) {
                        nationNames.remove(nation.getString("nationName"));
                    }
                    return completeLastWord(nationNames.toArray(new String[0]));
                }
        }

        return NO_COMPLETE;
    }
}