package net.bytebond.core.commands.subcommands;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Messages;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NationClaimSubCommand extends SimpleSubCommand {

    /* Parent:
     * /nation
     */

    public NationClaimSubCommand(SimpleCommandGroup parent) {
        super(parent, "claim");

        setDescription("Claim a chunk of land for your faction");
        setUsage("");
    }


    @Override
    protected void onCommand() {
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
        // check if the world is allowed to be claimed ? World in Config.Territory.Claiming.worlds maybe?


        // check if over the nation limit
        if(nation.isSet("territory")) {
            if(nation.getStringList("territory").size() >= nation.getInteger("max_territory")) {
                tellWarn(Messages.Nation.Claim.over_limit);
                return;
            }
        }

        // check balance ?



       //does any nation own this chunk in their String list territory

       // Map<UUID, NationYML> nationsMap = NationYML.getNations();
       // for(NationYML nations : nationsMap.values()) {
       //     if(nations.isSet("territory")) {
       //         if(nations.getStringList("territory").contains(chunk.toString())) {
       //             tellWarn(Messages.Nation.Claim.already_claimed);
       //             return;
       //         }
       //     }
       // }

        Map<UUID, NationYML> nationsMap = NationYML.getNations();
        for(NationYML nations : nationsMap.values()) {
            if(nations.isSet("territory")) {
                if(nations.getStringList("territory").contains(chunkStr)) { // use chunkStr instead of chunk.toString()
                    tellWarn(Messages.Nation.Claim.already_claimed.replace("{nation}", nations.getString("nationName")));
                    return;
                }
            }
        }


        if(nation.isSet("territory")) {
            List<String> territory = nation.getStringList("territory");
            territory.add(chunkStr);
            nation.set("territory", territory);
        } else {
            List<String> territory = new ArrayList<>();
            territory.add(chunkStr);
            nation.set("territory", territory);
        }
        nation.save();
        tellSuccess(Messages.Nation.Claim.success);
        if(Config.General.debugging) {
            System.out.println("Nation [" + nation.getString("nationName") + "] has claimed chunk " + chunk.toString());
            System.out.println("Nations current list of claimed territory: " + nation.getStringList("territory"));
        }
    }
}