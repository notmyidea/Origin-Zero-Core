package net.bytebond.core;

import net.bytebond.core.commands.EconomyCommand;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.data.Villager;
import net.bytebond.core.settings.Config;
import net.bytebond.core.util.NationTaxCollection;
import net.bytebond.core.util.Placeholders;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public final class Core extends SimplePlugin {

	private static Economy econ = null;
	Map<UUID, NationYML> nations = NationYML.getNations();

	@Override
	protected void onPluginStart() {
		registerCommands(new EconomyCommand());
		Common.logFramed("Nations plugin has been enabled!", "Loading Nations and Claims from files.", "Nations plugin version: 1.0.0 (WIP)");
		NationTaxCollection nationTaxCollection = new NationTaxCollection();
		nationTaxCollection.startTaxCollection();
		//checkAndCreateDirectoris();
		//Thread t1 = new Thread(new LoadItemsImpl());
		//t1.start();

		if(!setupEconomy()) {
			Common.logFramed("Vault not found! Disabling plugin!", "Please install Vault to use this plugin!");
			System.exit(0);
			getServer().getPluginManager().disablePlugin(this);

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
			new Placeholders(this).register();
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

		private void checkAndCreateDirectoris() {
			String[] directories = {"nations", "economy", "territory", "transit"};
			for(String directory : directories) {
				Path dirPath1 = Paths.get("plugins/Nations/data/" + directory);
				if(!Files.exists(dirPath1)) {
					try {
						Files.createDirectories(dirPath1);
					} catch (IOException e) {
						Common.logFramed("Error creating directory: " + dirPath1.toString() + "!", "Error: " + e.getMessage());
					}
				}
			}
		}

		//if (EconomyHandler.checkIfCurrenciesExist()) {
		//	EconomyHandler economyHandler = new EconomyHandler();
		//} else {
		//	Bukkit.getPluginManager().disablePlugin(this);
		//}


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

