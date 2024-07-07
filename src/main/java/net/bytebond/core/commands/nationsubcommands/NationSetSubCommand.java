package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.util.DynmapIntegration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import net.bytebond.core.settings.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NationSetSubCommand extends SimpleSubCommand {

    public NationSetSubCommand(SimpleCommandGroup parent) {
        super("set");
        setPermission("nation.player");
        setDescription("Change your Nation's settings.");
        setUsage("<option> <value>");
    }

    @Override
    protected void onCommand() {
        checkConsole(); // Console cannot have a Nation
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);
        List<String> messages = new ArrayList<>();
        // return if the sender is not in a Nation
        if (!(nation.isSet("nationName"))) {
            tellWarn(Messages.Nation.Creation.not_in_nation);
            return;
        }

        if (args.length == 1 || (args.length < 2)) {
            tellWarn(Messages.Nation.Set.missing_arg);
            return;
        }

        String firstArg = args[0];
        switch (firstArg) {
            case "description":
                String descArg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if(!(Config.Nations.Creation.Description.color_coding_permission.equals("null")) && descArg.contains("&")) {
                    tellWarn(Messages.Nation.Set.color_coding.replace("{setting}", "description"));
                    return;
                }
                if (descArg.length() > Config.Nations.Creation.Description.max_characters) {
                    tellWarn(Messages.Nation.Set.max_characters.replace("{max}", String.valueOf(Config.Nations.Creation.Description.max_characters)).replace( "{setting}", "description"));
                    return;
                }
                if(descArg.length() < Config.Nations.Creation.Description.min_characters) {
                    tellWarn(Messages.Nation.Set.min_characters.replace("{min}", String.valueOf(Config.Nations.Creation.Description.min_characters)).replace("{setting}", "description"));
                    return;
                }
                if(Arrays.stream(Config.Nations.Creation.Description.blacklist).anyMatch(descArg::contains)) {
                    tellWarn(Messages.Nation.Set.blacklisted.replace("{setting}", firstArg).replace("{value}", "description"));
                    return;
                }
                nation.set("nationDescription", descArg);
                nation.save();
                //nation.set("nationDescription", description);
                tellSuccess(Messages.Nation.Set.set_setting_success.replace("{setting}", firstArg).replace("{value}", descArg));
                break;
            case "tag":
                String tagArg = args[1];
                if (tagArg.length() > Config.Nations.Creation.Tags.max_characters) {
                    tellWarn(Messages.Nation.Set.max_characters.replace("{max}", String.valueOf(Config.Nations.Creation.Tags.max_characters)).replace( "{setting}", "tag"));
                    return;
                }
                if (tagArg.length() < Config.Nations.Creation.Tags.min_characters) {
                    tellWarn(Messages.Nation.Set.min_characters.replace("{min}", String.valueOf(Config.Nations.Creation.Tags.min_characters)).replace( "{setting}", "tag"));
                    return;
                }
                if(Arrays.stream(Config.Nations.Creation.Tags.blacklist).anyMatch(tagArg::contains)) {
                    tellWarn(Messages.Nation.Set.blacklisted.replace("{setting}", tagArg).replace("{value}", "tag"));
                    return;
                }
                nation.set("TAG", tagArg);
                nation.save();
                //nation.set("nationTag", tagArg);
                tellSuccess(Messages.Nation.Set.set_setting_success.replace("{setting}", firstArg).replace("{value}", tagArg));
                break;
            case "name":
                String nameArg = args[1];
                if(!(Config.Nations.Creation.Naming.color_coding_permission.equals("null")) && nameArg.contains("&")) {
                    tellWarn(Messages.Nation.Set.color_coding.replace("{setting}", "name"));
                    return;
                }
                if (nameArg.length() > Config.Nations.Creation.Naming.max_characters) {
                    tellWarn(Messages.Nation.Set.max_characters.replace("{max}", String.valueOf(Config.Nations.Creation.Naming.max_characters)).replace( "{setting}", "name"));
                    return;
                }
                if (nameArg.length() < Config.Nations.Creation.Naming.min_characters) {
                    tellWarn(Messages.Nation.Set.min_characters.replace("{min}", String.valueOf(Config.Nations.Creation.Naming.min_characters)).replace( "{setting}", "name"));
                    return;
                }
                if(Arrays.stream(Config.Nations.Creation.Naming.blacklist).anyMatch(nameArg::contains)) {
                    tellWarn(Messages.Nation.Set.blacklisted.replace("{setting}", nameArg).replace("{value}", "name"));
                    return;
                }
                nation.set("nationName", nameArg);
                nation.save();
                //nation.set("nationName", nameArg);
                tellSuccess(Messages.Nation.Set.set_setting_success.replace("{setting}", firstArg).replace("{value}", nameArg));
                break;
            case "taxrate":
                tellWarn("&fThis setting is not yet implemented.");
                break;
            case "color":
                String colorArg = args[1].toUpperCase();
                try {
                    DynmapIntegration.MainColor selectedColor = DynmapIntegration.MainColor.valueOf(colorArg);
                    nation.set("MainColor", selectedColor.name());
                    nation.save();
                    tellSuccess(Messages.Nation.Set.set_setting_success.replace("{setting}", firstArg).replace("{value}", colorArg));
                } catch (IllegalArgumentException e) {
                    tellWarn("Invalid color. Please choose a color from the following: " + Arrays.toString(DynmapIntegration.MainColor.values()));
                }
                break;

            default:
                messages.add("&f" + Common.chatLineSmooth());
                messages.add("   &fYou can change the following settings:");
                messages.add("   &f- &7description&f, - &7tag&f, - &7name");
                messages.add("&f" + Common.chatLineSmooth());
                tellNoPrefix(messages);
                break;
        }
    }


        @Override
        protected List<String> tabComplete() {

            if(!isPlayer())
                return new ArrayList<>();

            final Player player = (Player) sender;

            switch (args.length) {
                case 1:
                    return completeLastWord("description", "tag", "name", "color");
                case 2:
                    if (args[0].equals("description")) {
                        return completeLastWord("value can be multiple");
                    }
                    if(args[0].equals("tag")) {
                        return completeLastWord("value in 3 letters");
                    }
                    if(args[0].equals("name")) {
                        return completeLastWord("value in 16 characters");
                    }
                    if(args[0].equals("color")) {
                        return completeLastWord("RED", "GREEN", "BLUE", "YELLOW", "CYAN", "MAGENTA", "WHITE", "BLACK");
                    }

            }
            return NO_COMPLETE;
        }

}
