package net.bytebond.core.commands;

import net.bytebond.core.commands.adminsubcommands.AdminNationManager;
import net.bytebond.core.commands.diplomaticsubcommands.NationAnnounceSubCommand;
import net.bytebond.core.commands.diplomaticsubcommands.NationArchiveSubCommand;
import net.bytebond.core.commands.diplomaticsubcommands.NationDiplomacySubCommand;
import net.bytebond.core.commands.economicsubcommands.NationTaxSubCommand;
import net.bytebond.core.commands.nationsubcommands.*;
import net.bytebond.core.data.NationYML;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AutoRegister
public final class NationCommand extends SimpleCommandGroup {

    public NationCommand() {
        super("nation||n");
    }

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new NationCreateSubCommand(this));
        registerSubcommand(new NationDeleteSubCommand(this));
        registerSubcommand(new NationInfoSubCommand(this));
        registerSubcommand(new NationSetSubCommand(this));
        registerSubcommand(new NationsClaimManagerSubCommand(this));
        registerSubcommand(new NationAlliedClaimsSubCommand(this));
        registerSubcommand(new NationTaxSubCommand(this));
        registerSubcommand(new NationAnnounceSubCommand(this));
        registerSubcommand(new NationArchiveSubCommand(this));
        registerSubcommand(new NationDiplomacySubCommand(this));
        registerSubcommand(new AdminNationManager(this));
    }


    @Override
    protected List<SimpleComponent> getNoParamsHeader() {
        if(!(sender instanceof Player)) {
            System.out.println("You must be a player to use this command.");
        }
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        int foundedYear = SimplePlugin.getInstance().getFoundedYear();
        List<String> messages = new ArrayList<>();
        messages.add("&f" + Common.chatLineSmooth());
        NationYML nation = new NationYML(UUID);

        // ******************************
        //
        // THIS FUNCTION IS **STATIC** IT CAN ONLY BE LOADED **ONCE** !!!
        // THAT MEANS THAT DYNAMIC MESSAGES WILL NOT WORK IF CHANGED RECENTLY !
        // THIS AFFECTS ALMOST EVERYTHING, INCLUDING THE NATION DESCRIPTION REMINDER, THE __NATION NAME__ ITSELF ... etc ...
        //
        // ******************************

        //if player in Nation
        if (nation.isSet("nationName")) {
            //for (String line : Messages.Nation.Static.not_in_nation) {
            // messages.addAll(Arrays.asList(line.split("\n")));
            messages.add("     &fYou are in a Nation.");
            messages.add("     &fYou can access nation related commands: &f/nation <");
            messages.add("     &7info&f, &7diplo&f, &7more&f.. &f>");
            //}
        } //else {
        //  for (String line : Messages.Nation.Static.in_nation) {
        //      messages.addAll(Arrays.asList(line.split("\n")));
        //  }
        // }
        else {
            messages.add("     &fYou are not currently part of a Nation.");
            messages.add("     &fYou can create one with &f/nation create &7<name>");
        }
        //if (Messages.Nation.Static.Notify_description.enabled && nation.isSet("nationDescription") || !(nation.getString("nationDescription").equals("&7THIS IS YOUR NATION DESCRIPTION."))) {
        //for (String line : Messages.Nation.Static.Notify_description.messages) {
        //    messages.addAll(Arrays.asList(line.split("\n")));
        // }
        //messages.add("     &7BTW: You have not added a description to your Nation yet.");
        //messages.add("     &7/nation setdescription <description>");
        //}

        messages.add("&f" + Common.chatLineSmooth());
        return Common.convert(messages, SimpleComponent::of);
    }

    public SimplePlugin getPlugin() {
        return SimplePlugin.getInstance();
    }

}


    // absolutely broken
    /*@Override
     *protected String[] getHelpHeader() {
     *   List<String> messages = new ArrayList();
     *   //messages.add("&7" + Common.chatLineSmooth());
     *   //messages.add("   &7" + SimplePlugin.getNamed() + " &7" + SimplePlugin.getVersion());
     *   //messages.add("   &7You are not currently part of a Nation.");
     *   //messages.add("   &7You can create one with &f/nation create &7<name>");
     *   //messages.add(" ");
     *   //messages.add("   &f/nation help/info &7[nation] for more information");
     *   //messages.add("&7" + Common.chatLineSmooth());
     *  messages.add("&7" + Common.chatLineSmooth() +"\n");
     *
     *   messages.add(this.getHeaderPrefix() + "  " + SimplePlugin.getNamed() + " &7" + SimplePlugin.getVersion()+"\n");
     *   messages.add(" "+"\n");
     *   messages.add("   &a[] &f= optional arguments"+"\n");
     *   messages.add("   &7<> &f= required arguments"+"\n");
     *   messages.add(" ");
     *   messages.add("   &7Subcommands: &fcreate&7, &fdelete&7, &finfo &7, &fhelp &7, &fmore "+"\n");
        messages.add("   &7More specific commands: &fdiplo&7, &fmore"+"\n");
        return messages.toString().split("\n");
    }
     */








