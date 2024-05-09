package net.bytebond.core.commands;

import net.bytebond.core.commands.subcommands.NationCreateSubCommand;
import net.bytebond.core.commands.subcommands.NationDeleteSubCommand;
import org.bukkit.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public final class NationCommand extends SimpleCommandGroup {

    public NationCommand() {
        super("nation");
    }

    @Override
    protected void registerSubcommands() {
        registerSubcommand(new NationCreateSubCommand(this));
        registerSubcommand(new NationDeleteSubCommand(this));
    }

    @Override
    protected List<SimpleComponent> getNoParamsHeader() {
        int foundedYear = SimplePlugin.getInstance().getFoundedYear();
        List<String> messages = new ArrayList();
        messages.add("&7" + Common.chatLineSmooth());
       // messages.add(this.getHeaderPrefix() + "  " + SimplePlugin.getNamed() + " &7" + SimplePlugin.getVersion());
        messages.add("   &7You are not currently part of a Nation.");
        messages.add("   &7You can create one with &f/nation create &7<name>");
        messages.add(" ");
        //String credits = String.join(", ", SimplePlugin.getInstance().getDescription().getAuthors());



        messages.add("   &f/nation help/info &7[nation] for more information");
        messages.add("&7" + Common.chatLineSmooth());
        return Common.convert(messages, SimpleComponent::of);
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







}
