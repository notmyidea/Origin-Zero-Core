package net.bytebond.core.commands.subcommands;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public class NationCreateSubCommand extends SimpleSubCommand {
    private static final Logger log = LoggerFactory.getLogger(NationCreateSubCommand.class);

    /* Parent: ../$NationCommand.class
     * /nation create <name>
     */

    public NationCreateSubCommand(SimpleCommandGroup parent) {
        super(parent, "create");

        setDescription("Create a new Nation.");
        setUsage("<name>");

        if(!Objects.equals(Config.Nations.Creation.requiredPermission, "null")) {
            setPermission(Config.Nations.Creation.requiredPermission);
        }

    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);


        // Check if the Player is in a Nation already


        // Nations.Creation.enabled = "true" !"false"
        if(!(Config.Nations.Creation.enabled)) {
            tellWarn("&fNation creation is disabled.");
            return;
        }

        if(nation.isSet("nationName")) {
            tellWarn("&fYou are already part of a Nation.");
            return;
        }

        if(args.length != 1) {
            tellWarn("&fYou must provide a name for your Nation. &7/nation create <name>");
            return;
        }
        // Out of Bounds exception e
        String firstArg = args[0];

        // check if the nation name is taken?

        // check if args[0] contains anything but characters
        if(!firstArg.matches("[a-zA-Z]+")) {
            tellWarn("&fYour Nation name must only contain letters.");
            return;
        }

        if(!(Config.Nations.Creation.Naming.max_characters == 0)) {
            if(firstArg.length() > Config.Nations.Creation.Naming.max_characters) {
                tellWarn("&fYour Nation name must be shorter than &7" + Config.Nations.Creation.Naming.max_characters + "&f characters.");
                return;
            }
        }

        if(!(Config.Nations.Creation.Naming.min_characters == 0)) {
            if(firstArg.length() < Config.Nations.Creation.Naming.min_characters) {
                tellWarn("&fYour Nation name must be longer than &7" + Config.Nations.Creation.Naming.min_characters + "&f characters.");
                return;
            }
        }

       if(!(Config.Nations.Creation.Naming.blacklist.length == 0)) {
           for (String s : Config.Nations.Creation.Naming.blacklist) {
               if(firstArg.contains(s)) {
                   tellWarn("&fYour Nation name contains a blacklisted word.");
                   return;
               }
           }
       }

       if(!(Config.Nations.Creation.cost == 0) && (!Objects.equals(Config.Economy.provider, "null"))) {
           //if(!(Behavior.hasMoney(player, Config.Nations.Creation.cost))) {
             //   tellWarn("You do not have enough money to create a nation.");
             //   return;
             // }
       }

       nation.set("nationName", firstArg);
       nation.set("owner", UUID.toString());
       nation.set("nationDescription", "&7THIS IS YOUR NATION DESCRIPTION.");
       nation.set("TAG", firstArg.substring(0, Config.Nations.Creation.Tags.max_characters).toUpperCase());
       nation.set("vaultBalance", 0.0);
       nation.set("UEConWood", 0.0);
       nation.set("UEConStone", 0.0);
       nation.set("UEConBrick", 0.0);
       nation.set("UEConDarkstone", 0.0);
       nation.set("UEConObsidian", 0.0);
       nation.set("taxRate", 0.0);
       nation.set("population", 0);
       nation.set("drills", "None");
       nation.set("allyBuilding", false);
       nation.set("max_territory", Config.Territory.Claiming.max);
       nation.set("min_territory", Config.Territory.Claiming.min);
       //nation.set("territory", );

       nation.save();

       tellSuccess("&fYou have created a Nation named &7" + firstArg + "&f.");








    }


}
