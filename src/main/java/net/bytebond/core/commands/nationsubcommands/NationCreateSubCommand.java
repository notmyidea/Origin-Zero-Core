package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.HashManager;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.util.integrations.DynmapAPI;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NationCreateSubCommand extends SimpleSubCommand {
    private static final Logger log = LoggerFactory.getLogger(NationCreateSubCommand.class);
    /* Parent: ../$NationCommand.class
     * /nation create <name>
     */

    public NationCreateSubCommand(SimpleCommandGroup parent) {
        super(parent, "create");
        setPermission("nation.player");
        setDescription("Create a new Nation.");
        setUsage("<name>");


    }



    public void fixTellWarn(String message, Player player) {
        tellWarn(message);
    }
    public void fixTellInfo(String messsage, Player player) {
        tellInfo(messsage);
    }



    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formatDateTime = now.format(formatter);
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



       // check if any of the nation files have the same nationName as firstArg in a for each loop
        // Get the name the user wants to use
        String desiredName = args[0];

        // Get all nations
        Map<UUID, NationYML> nations = NationYML.getNations();

        // Iterate over all nations
        for (NationYML nation1 : nations.values()) {
            // Get the name of the current nation
            String nationName = nation1.getString("nationName");

            if(nationName == null) {
                continue;
            }

            // Check if the name is already taken
            if (nationName.equals(desiredName)) {
                // Inform the user that the name is already taken
                tellWarn("&fA Nation with that name already exists.");
                return;
            }
        }

        if(!(Config.Nations.Creation.cost == 0)) {

            double cost = Config.Nations.Creation.cost;
            if (!Core.getEconomy().has(player, cost)) {
                tellWarn("You do not have enough money to create a nation.");
                return;
            }

            Core.getEconomy().withdrawPlayer(player, cost);
        }

       //if(NationYML.getNations().containsKey(firstArg)) {
       //    tellWarn("A Nation with that name already exists.");
       //    return;
       //}

       String nationHash = HashManager.generateHash(firstArg, UUID);

       nation.set("nationName", firstArg);
       nation.set("nationHash", nationHash);
       nation.set("owner", UUID.toString());
       nation.set("nationDescription", "&7THIS IS YOUR NATION DESCRIPTION.");
       nation.set("TAG", firstArg.substring(0, Config.Nations.Creation.Tags.max_characters).toUpperCase());
            DynmapAPI.MainColor[] colors = DynmapAPI.MainColor.values();
            int randomIndex = new Random().nextInt(colors.length);
            DynmapAPI.MainColor randomColor = colors[randomIndex];
       nation.set("MainColor", randomColor.name());
       nation.set("treasury", 0);
       nation.set("wood", Config.Nations.Creation.starting_resources);
       nation.set("stone", Config.Nations.Creation.starting_resources);
       nation.set("brick", Config.Nations.Creation.starting_resources);
       nation.set("darkstone", Config.Nations.Creation.starting_resources);
       nation.set("obsidian", Config.Nations.Creation.starting_resources);
       nation.set("taxRate", 10);
       nation.set("happiness", 70);
       nation.set("happiness_decline", 0);
       nation.set("last_happiness_check", formatDateTime);
       nation.set("housing", new ArrayList<String>());
            //List<String> housing_messages = nation.getStringList("housing");
            //nation.set("housing", housing_messages);
       nation.set("villagers", new ArrayList<String>());
       nation.set("population", 0);
       nation.set("drills", new ArrayList<String>());
            //List<String> drills = nation.getStringList("drills");
            //nation.set("drills", drills);
       nation.set("allyBuilding", false);
       nation.set("max_territory", Config.Territory.Claiming.max);
       nation.set("min_territory", Config.Territory.Claiming.min);
       nation.set("allyPermissions", false);
       nation.set("archive_messages", new ArrayList<String>());
            List<String> archive_messages = nation.getStringList("archive_messages");
            archive_messages.add("Creation on " + formatDateTime + " by §7" + player.getName() + " §fwith the name of §7" + firstArg + "§f.");
            nation.set("archive_messages", archive_messages);

       nation.set("allied_nations", new ArrayList<String>());
       nation.set("pending_allied_nations", new ArrayList<String>());
       nation.set("enemy_nations", new ArrayList<String>());
       nation.set("lastOnline", formatDateTime);

       nation.set("warscore", 0);
       nation.set("warswon", 0);
       nation.set("warslost", 0);
       nation.set("troops", 0);

       nation.save();

       // CREATE A ULCON ACCOUNT FOR THE NATION
       //EconomyHandler economyHandler = new EconomyHandler();
       //economyHandler.generateNationBankAccount(UUID);
       Common.log("Nation Bank Account created for " + firstArg + " with the owner of " + UUID.toString() + ".");
       String joinText = "&fYou have created a Nation named &7" + firstArg + "&f.";
       Core.getEconomy().createPlayerAccount(player);
       tellSuccess(joinText);

       HashMan.getInstance().addNation(nation, UUID);
       HashMan.getInstance().addNationToAllNationMap(nation);

    }

    @Override
    protected List<String> tabComplete() {

        if (!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                return completeLastWord("Nation name");
            default:
                return NO_COMPLETE;

        }
    }


}