package de.cruelambition.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class Generator {

	private List<Material> material, forbidden;
	private BukkitTask generatorLoop, checkLoop;

	public void fillList() {
		material.addAll(Arrays.asList(Material.values()));
	}

	public void removeItemFromForbiddenList(Material m) {
		forbidden.remove(m);
	}

	public void removeForbiddenItemFromPlayer(Player p, Material m) {
		if (!isForbiddenItem(m)) return;
		for (ItemStack c : p.getInventory().getContents())
			if (c != null && c.getType() == m) c.setType(Material.AIR);
	}

	public void removeAllForbiddenItemsFromPlayer(Player p) {
		for (ItemStack c : p.getInventory().getContents())
			if (c != null && forbidden.contains(c.getType())) c.setType(Material.AIR);
	}

	public void removeAllForbiddenItemsFromAllPlayers() {
		for (Player ap : Bukkit.getOnlinePlayers())
			for (ItemStack c : ap.getInventory().getContents())
				if (c != null && forbidden.contains(c.getType())) c.setType(Material.AIR);
	}

	public void syncForbiddenItems() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<Material> newForbidden = new ArrayList<>();

		if (!c.isSet("Item.List.Forbidden"))
			c.set("Item.List.Forbidden", new ArrayList<>(List.of(Material.AIR.toString(), Material.COMMAND_BLOCK,
					Material.COMMAND_BLOCK_MINECART, Material.REPEATING_COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK)));
		List<String> sl = c.getStringList("Item.List.Forbidden");

		for (String s : sl) newForbidden.add(Material.valueOf(s));
		for (Material m : newForbidden) addItemToForbiddenList(m);

		removeAllForbiddenItemsFromMaterialList();
	}

	public void addItemToForbiddenList(Material m) {
		if (!forbidden.contains(m)) {
			forbidden.add(m);
			System.out.println(new Lang(null)
					.getString("itemgenerator_forbiddenlist_add_item"));
		}
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


	public List<String> getItemsFromPermanentForbiddenList(Material m) {
		return ItemGenerator.getItemGenerator().getConfig().getStringList("Item.List.Forbidden");
	}

	public void removeAllForbiddenItemsFromMaterialList() {
		if (material.isEmpty()) return;

		for (Material m : material)
			if (forbidden.contains(m)) removeItemFromList(m);
	}

	public void removeItemFromList(Material m) {
		material.remove(m);
	}

	public boolean isForbiddenItem(Material m) {
		return forbidden.contains(m);
	}

	public Material getRandomMaterial() {
		return material.get(new Random().nextInt(material.size() - 1));
	}

	public Material getMaterialFromInt(int i) {
		return material.get(i);
	}

	public List<Material> getMaterialList() {
		return this.material;
	}

	public void startGeneratorLoop(int startIn, int frequency) {
		BukkitTask gl = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(),
				() -> giveAll(getRandomMaterial()), 20L * startIn, 20L * frequency);
		this.generatorLoop = gl;
	}

	public void checkForForbiddenItemsLoop(int startIn, int frequency) {
		BukkitTask cl = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(),
				this::removeAllForbiddenItemsFromAllPlayers, 20L * startIn, 20L * frequency);
		this.checkLoop = cl;
	}

	public void cancelGenerator() {
		if (generatorLoop != null)
			generatorLoop.cancel();
	}

	public void cancelCheck() {
		if (checkLoop != null)
			checkLoop.cancel();
	}

	public void giveAll(Material m) {
		for (Player p : Bukkit.getOnlinePlayers())
			p.getInventory().addItem(new ItemStack(m));
	}
}
