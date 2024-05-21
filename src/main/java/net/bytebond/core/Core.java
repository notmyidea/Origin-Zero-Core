package net.bytebond.core;

import net.bytebond.core.data.ClaimRegistry;
import net.bytebond.core.data.NationYML;
import net.bytebond.core.settings.Config;
import net.bytebond.core.settings.Drills;
import net.bytebond.core.util.Placeholders;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.stream.Collectors;

import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;

/**
 * PluginTemplate is a simple template you can use every time you make
 * a new plugin. This will save you time because you no longer have to
 * recreate the same skeleton and features each time.
 *
 * It uses Foundation for fast and efficient development process.
 */

public final class Core extends SimplePlugin {

	protected Integer papi = 0;

	/**
	* Automatically perform login ONCE when the plugin starts.
	*/
	@Override
	protected void onPluginStart() {
	}



	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {
		Common.setLogPrefix("Nations");
		Common.setTellPrefix("Nations");
		// You can check for necessary plugins and disable loading if they are missing
		//Valid.checkBoolean(HookManager.isVaultLoaded(), "You need to install Vault so that we can work with packets, offline player data, prefixes and groups.");

		// Uncomment to load variables
		// Variable.loadVariables();

		//
		// Add your own plugin parts to load automatically here
		// Please see @AutoRegister for parts you do not have to register manually
		//

		//Messenger.

		//NationsDB.getInstance().connect(Data.storage.mysql.hostname, Integer.parseInt(Data.storage.mysql.port), Data.storage.mysql.database, Data.storage.mysql.username, Data.storage.mysql.password);
		this.registerEvents(this);
		// managed by @AutoRegister in the class
		//registerCommands(new NationCommand());

		Common.logFramed("Loading Nations " + this.getDescription().getVersion() + "\n https://github.com/notmyidea/nations");
		// Connect to DB
		// load all data from the database into the cache
		// check all Nations
		// check all Players
		// Run

		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			papi = 1;
			Common.log("PlaceholderAPI found, enabling placeholders");
			new Placeholders(this).register();
		}


		if (Config.General.debugging) {
			Common.log("Debugging is enabled");
			Common.log("loading configuration files . . .");
			Common.log("Economy provider is set to " + Config.Economy.provider);
			Common.log("Current prices are set to: ");
			Common.log("Nation-Creation: " + Config.Nations.Creation.cost);
			Common.log("International-Market prices: ");


			Common.log("All current Nations and their leaders are: ");
			//for (Nation nation : Nations.getNations()) {
			//	Common.log(nation.getName() + " - " + nation.getOwner().getName());
			//}


			//if (Drills.Drill.enabled) {
				Common.log("Drills are enabled");
				Common.log("Wood-Drill rate per hour: " + Drills.Drill.Wood.rate_per_hour);
				Common.log("Stone-Drill rate per hour: " + Drills.Drill.Stone.rate_per_hour);
				Common.log("Brick-Drill rate per hour: " + Drills.Drill.Brick.rate_per_hour);
				Common.log("Darkstone-Drill rate per hour: " + Drills.Drill.Darkstone.rate_per_hour);
				Common.log("Obsidian-Drill rate per hour: " + Drills.Drill.Obsidian.rate_per_hour);
			//} else {
				//Common.log("Drills are disabled");
			//}

		}



	}



	@Override
	protected void onPluginPreReload() {

		// Close your database here if you use one
		//YourDatabase.getInstance().close();
	}

	/* ------------------------------------------------------------------------------- */
	/* Events */
	/* ------------------------------------------------------------------------------- */

	/**
	 * An example event that checks if the right clicked entity is a cow, and makes an explosion.
	 * You can write your events to your main class without having to register a listener.
	 *
	 * @param event
	 */
	//@EventHandler
	//public void onRightClick(final PlayerInteractEntityEvent event) {
	//	if (event.getRightClicked().getType() == EntityType.COW)
	//		event.getRightClicked().getWorld().createExplosion(event.getRightClicked().getLocation(), 5);
	//}

	/*
	 * THIS HAS BROKEN MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
	 */



	/* ------------------------------------------------------------------------------- */
	/* Static */
	/* ------------------------------------------------------------------------------- */

	/**
	 * Return the instance of this plugin, which simply refers to a static
	 * field already created for you in SimplePlugin but casts it to your
	 * specific plugin instance for your convenience.
	 *
	 * @return
	 */
	public static Core getInstance() {
		return (Core) SimplePlugin.getInstance();
	}
}
