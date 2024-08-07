package net.bytebond.core;

import net.bytebond.core.commands.EconomyCommand;
import net.bytebond.core.data.HashMan;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import net.bytebond.core.settings.Config;
import net.bytebond.core.util.integrations.DynmapAPI;
import net.bytebond.core.util.NationTaxCollection;
import net.bytebond.core.util.integrations.PlaceholderAPI;
import net.bytebond.core.util.runnables.HappinessRunnable;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.dynmap.markers.AreaMarker;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public final class Core extends SimplePlugin {
	private static Economy econ = null;
	Map<UUID, NationYML> nations = NationYML.getNations();

	@Override
	protected void onPluginStart() {
		registerCommands(new EconomyCommand());
		Common.logFramed(ChatColor.GREEN + "Nations plugin has been enabled!", ChatColor.GREEN + "Loading Nations and Claims from files.", ChatColor.GREEN + "Nations plugin version: 0.9.8");
		//NationTaxCollection nationTaxCollection = new NationTaxCollection();
		//nationTaxCollection.startTaxCollection();
		//checkAndCreateDirectoris();
		//Thread t1 = new Thread(new LoadItemsImpl());
		//t1.start();

		HashMan.getInstance().repopulateAllNationMap();
		this.debugLog("All nation map size: " + HashMan.getInstance().allNationMap.size());

		// Start the HappinessRunnable task
		HappinessRunnable happinessRunnable = new HappinessRunnable();
		happinessRunnable.startTask(Config.Runnables.happiness_calculator_interval);


		if (!setupEconomy()) {
			Common.logFramed("Vault not found! Disabling plugin!", "Please install Vault to use this plugin!");
			System.exit(0);
			getServer().getPluginManager().disablePlugin(this);
		}

		DynmapAPI dynmapIntegration = new DynmapAPI(this);
		File nationsDirectory = new File("plugins/Core/data/");
		for (File nationFile : Objects.requireNonNull(nationsDirectory.listFiles())) {
			YamlConfiguration nationConfig = YamlConfiguration.loadConfiguration(nationFile);
			String colorName = nationConfig.getString("MainColor");
			DynmapAPI.MainColor nationColor;
			if (colorName != null) {
				nationColor = DynmapAPI.MainColor.valueOf(colorName);
			} else {
				nationColor = DynmapAPI.MainColor.WHITE;
			}

			List<String> claimedTerritories = nationConfig.getStringList("territory");
			for (String territory : claimedTerritories) {
				String[] split = territory.split(",");
				String worldName = split[0];
				double chunkX = Double.parseDouble(split[1]);
				double chunkZ = Double.parseDouble(split[2]);

				// Calculate the coordinates of the center of the chunk
				// Or just not he-he
				double x = chunkX; //* 16 + 8;
				double z = chunkZ; //* 16 + 8;

				// Get the nation's name
				String nationName = nationConfig.getString("nationName");

				String id = nationName + "_" + territory;
				String label = "Territory of " + nationName;

				dynmapIntegration.addClaimToDynmap(id, label, x, z, worldName);
				AreaMarker marker = dynmapIntegration.getMarkerSet().findAreaMarker(id);
				if (marker != null) {
					// Convert the hex color to an RGB color and mask it to get a positive value
					int rgbColor = Color.decode(nationColor.getHexValue()).getRGB() & 0xFFFFFF;
					marker.setFillStyle(0.30, rgbColor);
					marker.setLineStyle(0, 1.0, 0xFF0000);
				}
			}
		}
	}

	private Boolean setupEconomy() {
		if(getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;

	}

	@Override
	protected void onReloadablesStart() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlaceholderAPI(this).register();
		} else {
			Bukkit.getPluginManager().disablePlugin(this);
			System.exit(1);
		}

		Path dataFolder = Paths.get("plugins/Core/data");
		if (Files.notExists(dataFolder)) {
			try {
				Files.createDirectory(dataFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// bStats
	@Override
	public int getMetricsPluginId() {
		return 22047;
	}

	@Override
	protected void onPluginPreReload() {
	}

	@Override
	protected void onPluginStop() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity.getType() == EntityType.VILLAGER) {
					String customName = entity.getCustomName();
					if (customName != null && Arrays.stream(Villager.VillagerNames.values()).anyMatch(v -> v.name().equals(customName))) {
						entity.remove();
					}
				}
			}
		}
	}

	public static Core getInstance() {
		return (Core) SimplePlugin.getInstance();
	}

	public void logFramedCore(String message) {
		Common.logFramed(message);
	}

	public void debugLog(String message) {
		if (Config.General.debugging) {
			Common.log("DEBUG: " + message);
		}
	}

	public static Economy getEconomy() {
		return econ;
	}

}

