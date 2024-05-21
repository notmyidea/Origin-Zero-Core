package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Messages;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NationsClaimManagerSubCommand extends SimpleSubCommand {

    public NationsClaimManagerSubCommand(SimpleCommandGroup parent) {
        super(parent, "territory");

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
                Map<UUID, NationYML> nationsMap = NationYML.getNations();
                for(NationYML nations : nationsMap.values()) {
                    if(nations.isSet("territory")) {
                        if(nations.getStringList("territory").contains(chunkStr)) {
                            tellWarn(Messages.Nation.Claim.already_claimed.replace("{nation}", nations.getString("nationName")));
                            return;
                        }
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
                if(Config.General.debugging) {
                    System.out.println("Nation [" + nation.getString("nationName") + "] has claimed chunk " + chunk.toString());
                    System.out.println("Nations current list of claimed territory: " + nation.getStringList("territory"));
                    System.out.println("Chunk has been saved to " + chunkStr + ".yml");
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
                    } else {
                        tellWarn("&fThis chunk is not claimed by your nation.");
                    }
                } else {
                    tellWarn("&fYour nation has not claimed any territories.");
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
                return completeLastWord("claim", "unclaim");
        }

        return NO_COMPLETE;
    }
}