package net.bytebond.core.commands.nationsubcommands;

import net.bytebond.core.Core;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.util.integrations.DynmapAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.dynmap.markers.AreaMarker;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NationDeleteSubCommand extends SimpleSubCommand {

    /* Parent: ../$NationCommand.class
     * /nation delete
     */

    public NationDeleteSubCommand(SimpleCommandGroup parent) {
        super(parent, "delete");

        setPermission("nation.player");
        setDescription("Delete your Nation.");
        setUsage("confirm");
    }


    @Override
    protected void onCommand() {
        Player player = (Player) sender;
        UUID UUID = player.getUniqueId();
        NationYML nation = new NationYML(UUID);

        if(!(nation.isSet("nationName"))) {
            tellWarn("You are not currently part of a Nation.");
            return;
        }


        if(args.length != 1) {
            tellWarn("&fYou must confirm the deletion of your Nation. &7/nation delete confirm");
            return;
        }
        String firstArg = args[0];

        if(!(firstArg.equalsIgnoreCase("confirm"))) {
            tellWarn("&fYou must confirm the deletion of your Nation. &7/nation delete confirm");
            return;
        }
        // cleanup-process for claims
        try {
            String nationName = nation.getString("nationName");
            AtomicInteger deletedFilesCount = new AtomicInteger();

            Files.walk(Paths.get("plugins/Core/data/claims"))
                    .forEach(path -> {
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(path.toFile());
                        if (Objects.equals(yaml.getString("ownerNationName"), nation.getString("nationName")) || yaml.get("ownerUUID") == nation.getString("owner")) {
                            try {
                                Files.delete(path);
                                deletedFilesCount.getAndIncrement();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Core.getInstance().debugLog("DELETE ACTION: Deleted " + deletedFilesCount + " claim registrants for nation " + nationName );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // cleanup-process for housing objects



        tellSuccess("&fYou have successfully deleted your Nation.");
        List<String> territories = nation.getStringList("territory");
        DynmapAPI dynmapIntegration = new DynmapAPI(Core.getInstance());

        for (String territory : territories) {
            // Remove the unclaimed territory from the dynmap
            String id = nation.getString("nationName") + "_" + territory;
            AreaMarker marker = dynmapIntegration.getMarkerSet().findAreaMarker(id);
            if (marker != null) {
                marker.deleteMarker();
            }
        }
        nation.deleteFile();
        HashMan.getInstance().removeNation(nation);
        HashMan.getInstance().removeNationFromAllNationMap(nation);

    }

    @Override
    protected List<String> tabComplete() {

        if (!isPlayer())
            return new ArrayList<>();

        final Player player = (Player) sender;

        switch (args.length) {
            case 1:
                return completeLastWord("confirm");
            default:
                return NO_COMPLETE;

        }
    }


}