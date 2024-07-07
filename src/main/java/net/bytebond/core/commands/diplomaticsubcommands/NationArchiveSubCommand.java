package net.bytebond.core.commands.diplomaticsubcommands;

import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NationArchiveSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    public NationArchiveSubCommand(SimpleCommandGroup parent) {
        super(parent, "archive");

        setPermission("nation.player");
        setDescription("View the archived announcement and actions of that Nation");
        setUsage("/nation archive <nation>");
    }


    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot create a nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        List<String> messages = new ArrayList<>();
        // I AM SCHIZOPHRENIC AAHHHHH

        if (args.length != 1) {
            tellWarn("&fYou must specify a Nation to view the archive of. &7/nation archive <name>");
            return;
        }

        String nationName = args[0];
        Map<UUID, NationYML> nations = NationYML.getNations();
        for (NationYML nation : nations.values()) {
            if (nation.getString("nationName").equals(nationName)) {
                List<String> archive_messages = nation.getStringList("archive_messages");

                // print them right to the player because I am lazy as fuck
                String chatLineSmooth = "§r§m------------------§r§7 Archive §r§m-------------------";
                List<String> formattedMessages = new ArrayList<>();
                formattedMessages.add(chatLineSmooth);
                formattedMessages.addAll(archive_messages);
                formattedMessages.add(chatLineSmooth);

                for (String formattedMessage : formattedMessages) {
                    player.sendMessage(formattedMessage.replace("&","§"));
                }
                return;
            }
        }

        tellWarn("&fNo nation found with the name: " + nationName);
    }




    @Override
    protected List<String> tabComplete() {
        if(!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                // Get all nations
                Map<UUID, NationYML> nations = NationYML.getNations();
                // Extract nation names
                List<String> nationNames = nations.values().stream()
                        .map(nation -> nation.getString("nationName"))
                        .collect(Collectors.toList());
                return completeLastWord(nationNames.toArray(new String[0]));
            case 2:
                return completeLastWord("");
        }

        return NO_COMPLETE;
    }

}

