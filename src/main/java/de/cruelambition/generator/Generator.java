package de.cruelambition.generator;

import java.util.*;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

public class Generator {

	private List<String> material, forbidden;
	private BukkitTask generatorLoop, checkLoop;

	public Generator() {
		material = new ArrayList<>();
		forbidden = new ArrayList<>();
	}

	public List<String> getForbiddenList() {
		return forbidden;
	}

	public static void start(Generator g, int csi, int cf, int gsi, int gf) {
		g.fillList();
		g.syncForbiddenItems();

		g.removeAllForbiddenItemsFromMaterialList();
		g.checkForForbiddenItemsLoop(csi, cf);

		g.startGeneratorLoop(gsi, gf);
		g.listForbiddenItems();
	}

	public List<Integer> getFrequencies() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		int csi = c.getInt("Loops.Check.StartIn", 60),
				cf = c.getInt("Loops.Check.Frequency", 80),
				gsi = c.getInt("Loops.Generator.StartIn", 6),
				gf = c.getInt("Loops.Generator.Frequency", 30);

		return new ArrayList<>(Arrays.asList(csi, cf, gsi, gf));
	}

	public void setFrequencies(int csi, int cf, int gsi, int gf) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		c.set("Loops.Check.StartIn", csi);

		c.set("Loops.Check.Frequency", cf);
		c.set("Loops.Generator.StartIn", gsi);

		c.set("Loops.Generator.Frequency", gf);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void stopLoop() {
		cancelGenerator();
		cancelCheck();
	}

	public void restart() {
		stopLoop();

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
			List<Integer> f = getFrequencies();
			start(new Generator(), f.get(0), f.get(1), f.get(2), f.get(3));
		}, 5);
	}

	public void restart(int csi, int cf, int gsi, int gf) {
		stopLoop();
		setFrequencies(csi, cf, gsi, gf);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () ->
				start(new Generator(), csi, cf, gsi, gf), 5);
	}

	public void fillList() {
		for (Material value : Material.values()) material.add(value.toString());
	}

	public void removeItemFromForbiddenList(Material m) {
		forbidden.remove(m.toString());
	}

	public void removeForbiddenItemFromPlayer(Player p, Material m) {
		if (!isForbiddenItem(m)) return;
		for (ItemStack c : p.getInventory().getContents())
			if (c != null && c.getType() == m) c.setType(Material.AIR);
	}

	public void removeAllForbiddenItemsFromPlayer(Player p) {
		for (ItemStack c : p.getInventory().getContents())
			if (c != null && forbidden.contains(c.getType().toString())) c.setType(Material.AIR);
	}

	public void removeAllForbiddenItemsFromAllPlayers() {
		for (Player ap : Bukkit.getOnlinePlayers())
			for (ItemStack c : ap.getInventory().getContents())
				if (c != null && forbidden.contains(c.getType().toString())) c.setType(Material.AIR);
	}

	public void syncForbiddenItems() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

		if (!c.isSet("Item.List.Forbidden")) {
			c.set("Item.List.Forbidden", new ArrayList<>(List.of(Material.AIR.toString(),
					Material.COMMAND_BLOCK.toString(), Material.JIGSAW.toString(),
					Material.STRUCTURE_BLOCK.toString())));
			ItemGenerator.getItemGenerator().saveConfig();
		}

		List<String> sl = c.getStringList("Item.List.Forbidden");
		List<String> newForbidden = new ArrayList<>(sl);

		for (String m : newForbidden) addItemToForbiddenList(m.toString());
		removeAllForbiddenItemsFromMaterialList();
	}

	public void addItemToForbiddenList(String m) {
		if (forbidden.contains(m)) return;

		for (Material mt : Material.values())
			if (mt.toString().contains(m)) forbidden.add(m);

		Bukkit.getConsoleSender().sendMessage(Lang.PRE + String.format(Lang.getMessage(Lang.getServerLang(),
				"itemgenerator_forbiddenlist_add_item"), m.toLowerCase()));
	}

	public void listForbiddenItems() {
		Lang.broadcastArg("itemgenerator_forbidden_listing", getForbiddenList().toString()
				.replace("[", "").replace("]", ""));
	}

	public void addItemToPermanentForbiddenList(Material m) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<String> sl = c.getStringList("Item.List.Forbidden");

		sl.add(m.toString());
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void removeItemFromPermanentForbiddenList(Material m) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<String> sl = c.getStringList("Item.List.Forbidden");

		sl.remove(m.toString());
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public List<String> getItemsFromPermanentForbiddenList() {
		return ItemGenerator.getItemGenerator().getConfig().getStringList("Item.List.Forbidden");
	}

	public void removeAllForbiddenItemsFromMaterialList() {
		if (material.isEmpty() || material == null) return;

		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		for (Material m : Material.values())
			for (String s : forbidden)
				if (m.toString().contains(s) && material.contains(m.toString())) {
//					cs.sendMessage( "§9Removed Item: " + m);
					removeItemFromList(m);
				}
	}

	public void removeItemFromList(Material m) {
		material.remove(m.toString());
	}

	public boolean isForbiddenItem(Material m) {
		return forbidden.contains(m.toString());
	}

	public Material getRandomMaterial() {
		return Material.valueOf(material.get(new Random().nextInt(material.size() - 1)));
	}

	public Material getMaterialFromInt(int i) {
		return Material.valueOf(material.get(i));
	}

	public List<String> getMaterialList() {
		return this.material;
	}

	public void startGeneratorLoop(int startIn, int frequency) {
		this.generatorLoop = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(),
				this::giveAll, 20L * startIn, 20L * frequency);
	}

	public void checkForForbiddenItemsLoop(int startIn, int frequency) {
		this.checkLoop = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(),
				this::removeAllForbiddenItemsFromAllPlayers, 20L * startIn, 20L * frequency);
	}

	public void cancelGenerator() {
		if (generatorLoop != null)
			generatorLoop.cancel();
	}

	public void cancelCheck() {
		if (checkLoop != null)
			checkLoop.cancel();
	}

	public void giveAll() {
		for (Player ap : Bukkit.getOnlinePlayers()) {
			if (ap.getGameMode() != GameMode.SURVIVAL) continue;
			ap.playSound(ap.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.2f, 1.6f);

			if (ap.getInventory().firstEmpty() != -1) ap.getInventory().addItem(new ItemStack(getRandomMaterial()));
			else ap.getWorld().dropItemNaturally(ap.getLocation(), new ItemStack(getRandomMaterial()));
		}
	}
}
