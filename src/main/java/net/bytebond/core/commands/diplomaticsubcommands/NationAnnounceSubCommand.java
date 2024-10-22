package net.bytebond.core.commands.diplomaticsubcommands;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NationAnnounceSubCommand extends SimpleSubCommand {

    /* Parent:
     * /
     */

    private HashMap<UUID, Long> cooldowns = new HashMap<>();


    public NationAnnounceSubCommand(SimpleCommandGroup parent) {
        super(parent, "announce");

        setPermission("nation.player");
        setDescription("Announce a message into the chat");
        setUsage("/nation announce <message>");
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
        checkPerm("nation.player");

        if (!(nation.isSet("nationName"))) {
            tellWarn("&fYou are not currently part of a Nation.");
            return;
        }

        if(args.length == 0) {
            tellWarn("&fYou must specify a message to announce. &7/nation announce <message>");
            return;
        }

        if(cooldowns.containsKey(UUID)) {
            long secondsLeft = ((cooldowns.get(UUID)/1000) + Config.Nations.announcement_cooldown) - (System.currentTimeMillis()/1000);
            if(secondsLeft > 0) {
                tellWarn("&fYou must wait " + secondsLeft + " seconds before announcing again.");
                return;
            }
        }

        String message = String.join(" ", args);
        List<String> messages = new ArrayList<>();
        String announcementChatLineSmoothNew = "§r§m----------------§r§7 Announcement §r§m----------------§r";

        messages.add(announcementChatLineSmoothNew);
        messages.add(" §fThe Nation of §7" + nation.getString("nationName") + " §fannounces:");
        messages.add(" " + message.replace("&", "§"));
        messages.add(announcementChatLineSmoothNew);

        String formattedMessage = Common.join(messages, "\n");

        for (Player allPlayer : Bukkit.getOnlinePlayers()) {
            allPlayer.sendMessage(formattedMessage);
        }
        cooldowns.put(UUID, System.currentTimeMillis());

        List<String> archive_messages = nation.getStringList("archive_messages");
        archive_messages.add(" Announcement: (" + formatDateTime + ") : " + message.replace("&", "§"));
        //String archive_message = "Announcement: " + message.replace("&", "§");
        nation.set("archive_messages", archive_messages);
        nation.save();


        //nation.set("archive_messages", new ArrayList<String>());
        //List<String> archive_messages = nation.getStringList("archive_messages");
        //archive_messages.add("Creation on " + formatDateTime);
        //nation.set("archive_messages", archive_messages);


    }
}
