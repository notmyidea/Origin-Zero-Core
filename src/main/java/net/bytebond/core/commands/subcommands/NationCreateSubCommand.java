package net.bytebond.core.commands.subcommands;

import net.bytebond.core.data.NationData;
import net.bytebond.core.settings.Behavior;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NationCreateSubCommand extends SimpleSubCommand {

    /* Parent: ../$NationCommand.class
     * /nation create <name>
     */

    public NationCreateSubCommand(SimpleCommandGroup parent) {
        super(parent, "create");

        setDescription("Create a new nation.");
        setUsage("<name>");
    }


    @Override
    protected void onCommand() {
        Player player = (Player) sender;

        // /nation create
        checkConsole();
        if(args.length == 0) {
            tellError("&7Please specify a name for your nation.");
            // WATCH OUT FOR NATION CREATION LIMITS AND MAXIMS IN settings.yml
            return;
        }

        try {
            // /nation create <name>
            NationData.createNation(player.getUniqueId(), args[0]);
            System.out.println("Nation created: " + args[0] + " by " + player.getName() + " with ID: " + NationData.getNationData(player.getUniqueId()).getNationID() + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String firstArg = args[0];
        Common.tellNoPrefix(sender, "&7" + firstArg + " has been created.");

        if (Behavior.Nation.sendFirstInfoMessage) {
            List<String> firstInfoMessage = new ArrayList<>();
            for (String message : Behavior.Nation.firstInfoMessage) {
                Common.colorize(message.replace("{nation}", firstArg).replace("{player}", sender.getName()));
                firstInfoMessage.add(message);
            }
            Common.tellNoPrefix(sender, firstInfoMessage);


        }
        if (Behavior.Nation.broadcastCreation.enabled) {
            for(String message : Behavior.Nation.broadcastCreation.message) {

                // get all online Players except the sender
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    if (player1 != sender) {
                        Common.colorize(message);
                        player1.sendMessage(message.replace("{nation}", firstArg).replace("{player}", sender.getName()));
                    }
                }
                //Common.colorize(message);
                //Bukkit.broadcastMessage(message.replace("{nation}", firstArg).replace("{player}", sender.getName()));
            }

        }




    }
}
