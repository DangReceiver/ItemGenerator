package de.cruelambition.itemgenerator;

import java.util.*;
import java.util.List;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.IB;
import de.cruelambition.oo.Items;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class Generator {
	private final List<String> material, forbidden, rare, editable;
	private final List<Material> spawnEggs = new ArrayList<>();
	private BukkitTask generatorLoop, checkLoop;

	public Generator() {
		material = new ArrayList<>();
		forbidden = new ArrayList<>();

		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		rare = c.getStringList("Item.List.Rare");

		for (Material value : Material.values()) if (value.toString().contains("SPAWN_EGG")) spawnEggs.add(value);

		editable = new ArrayList<>(Arrays.asList(Material.ENCHANTED_BOOK.toString(),
				Material.POTION.toString(), Material.SPLASH_POTION.toString(),
				Material.TIPPED_ARROW.toString(), Material.LINGERING_POTION.toString(),
				"CHESTPLATE", "LEGGINGS", "BOOTS", "HELMET", "_SWORD", "_PICKAXE", "_AXE", "_SHOVEL"));

		if (rare.isEmpty()) {
			setRareItems(new ArrayList<>(Arrays.asList("_SHULKER", "NETHERITE", "DIAMOND", "BEACON", "DIRT",
					"SPAWN_EGG", "SPAWNER", "BARRIER", "BEDROCK", "ENCHANTMENT_TABLE", "_BUCKET", "ELYTRA",
					"_TRIM")));
			ItemGenerator.getItemGenerator().saveConfig();
		}

		for (int i = 0; i <= 4; i++) // Total: 6
			addMaterialToLoop(Material.POTION);
		for (int i = 0; i <= 2; i++) // Total: 4
			addMaterialToLoop(Material.SPLASH_POTION);

		for (int i = 0; i <= 1; i++) // Total: 3
			addMaterialToLoop(Material.LINGERING_POTION);
		for (int i = 0; i <= 6; i++) // Total: 8
			addMaterialToLoop(Material.ENCHANTED_BOOK);


		for (int a = 0; a <= Items.mats.size() - 1; a++)
			for (int b = 0; b <= Items.amount[a]; b++)
				addMaterialToLoop(Items.mats.get(a));

		if (getForbiddenList().contains("SPAWN_EGG")) addMaterialToLoop(Material.ALLAY_SPAWN_EGG);
	}

	public int getCustomItemAmount(Material m) {
		for (int a = 0; a <= Items.mats.size() - 1; a++)
			if (Items.mats.get(a) == m) return Items.amount[a];

		return -1;
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
//		g.listForbiddenItems();
	}

	public void addMaterialToLoop(Material m) {
		material.add(m.toString());
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

			start(this, f.get(0), f.get(1), f.get(2), f.get(3));
		}, 5);
	}

	public void restart(int csi, int cf, int gsi, int gf) {
		stopLoop();
		setFrequencies(csi, cf, gsi, gf);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> start(
				this, csi, cf, gsi, gf), 5);
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
					Material.STRUCTURE_BLOCK.toString(), "ARMOR_TRIM_SMITHING_TEMPLATE",
					"_STEM", "LEGACY_", "POTTED_")));

			ItemGenerator.getItemGenerator().saveConfig();
		}

		List<String> sl = c.getStringList("Item.List.Forbidden"),
				newForbidden = new ArrayList<>(sl);

		for (String m : newForbidden) addItemToForbiddenList(m.toString());
		removeAllForbiddenItemsFromMaterialList();
	}

	public void addItemToForbiddenList(String m) {
		if (forbidden.contains(m)) return;

		for (Material mt : Material.values())
			if (mt.toString().contains(m)) forbidden.add(m);

		Bukkit.getConsoleSender().sendMessage(Lang.PRE + String.format(
				Lang.getMessage(Lang.getServerLang(), "itemgenerator_forbiddenlist_add_item"),
				m.toLowerCase()));
	}

	public void listForbiddenItems() {
		Lang.broadcastArg("itemgenerator_forbidden_listing", getForbiddenList().toString().toLowerCase()
				.replace("[", "").replace("]", "")
				.replaceAll("_", " "));
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
//		ConsoleCommandSender cs = Bukkit.getConsoleSender();

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
		if (generatorLoop != null) generatorLoop.cancel();
	}

	public void cancelCheck() {
		if (checkLoop != null) checkLoop.cancel();
	}

	public void giveAll() {
		List<Player> wP = Bukkit.getWorld("world").getPlayers();
		if (wP == null) return;

		for (Player ap : wP) {
//			ap.sendMessage(ap.getWorld().getName() + " || " + wP.toString());

			if (ap.getGameMode() != GameMode.SURVIVAL) continue;
			ap.playSound(ap.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 0.35f, 0.8f);

			ItemStack is = new ItemStack(getRandomMaterial());
			if (isRare(is.getType().toString()) && new Random().nextInt(3) <= 1)
				new ItemStack(getRandomMaterial());
			if (isRare(is.getType().toString()) && new Random().nextInt(4) <= 2)
				new ItemStack(getRandomMaterial());

			Material type = is.getType();
			if (type == Material.ALLAY_SPAWN_EGG) rollSpawnEgg(is);
			if (new Random().nextInt(2) == 0 && canEdit(type)) edit(is);

			if (type.isBlock()) if (new Random().nextInt(2) == 0)
				is.setAmount(new Random().nextInt(2) == 0 ? (new Random().nextInt(10 + 1) - 4 >= 1
						? new Random().nextInt(10 + 1) - 4 : 1) : new Random().nextInt(10 + 1));

			moreItems(is);

			if (isCustomItem(type)) {
				int cmd = new Random().nextInt(getCustomItemAmount(type) + 1);
				if (cmd != 0) IB.cmd(is, cmd);
			}

			if (ap.getInventory().firstEmpty() != -1) ap.getInventory().addItem(is);
			else ap.getWorld().dropItemNaturally(ap.getLocation(), is);

			ap.sendActionBar(Lang.PRE + String.format(new Lang(ap).getString("generated_item"),
					type.toString().toLowerCase().replaceAll("_", " ")));
		}
	}

	public void rollSpawnEgg(ItemStack item) {
		item.setType(spawnEggs.get(new Random().nextInt(spawnEggs.size())));
	}

	public boolean isCustomItem(Material m) {
		return Items.mats.contains(m);
	}

	public boolean moreItems(ItemStack item) {
		boolean b = false;
		if (item.getType().getMaxStackSize() <= 1) return false;

		List<String> l = new ArrayList<>(Arrays.asList("ARROW", "WOOL", "DEEPSLATE", "STONE"));
		for (String s : l) if (item.getType().toString().contains(s)) b = true;

		if (b) {
			Random r = new Random();
			int i = r.nextInt(10) + 1;
			item.setAmount(i >= 6 && (r.nextInt(2) == 0) ? i / 2 : i);
		}

		return b;
	}

	public void addRareItem(String s) {
		List<String> sl = ItemGenerator.getItemGenerator().getConfig().getStringList("Item.List.Rare");

		rare.add(s);
		sl.add(s);

		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void removeRareItem(String s) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<String> sl = c.getStringList("Item.List.Rare");

		if (rare.contains(s)) rare.remove(s);
		if (sl.contains(s)) sl.remove(s);

		c.set("Item.List.Rare", sl);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void setRareItems(List<String> l) {
		ItemGenerator.getItemGenerator().getConfig().set("Item.List.Rare", l);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public boolean isRare(String s) {
		for (String ra : rare) if (s.contains(ra)) return true;
		return false;
	}

	public boolean isRareFromList(String s) {
		return rare.contains(s);
	}

	public boolean isRareFromConfig(ItemStack item) {
		for (String s : ItemGenerator.getItemGenerator().getConfig().getStringList("Item.List.Rare"))
			if (item.getType().toString().contains(s)) return true;
		return false;
	}

	public List<String> getRareItems() {
		return new ArrayList<>();
	}

	public static void effect(ItemStack item) {
		if (!(item.getItemMeta() instanceof PotionMeta pm)) return;
		Random r = new Random();

		Color col = Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		pm.setColor(col);

		int d = r.nextInt(460 * 20);
		int a = r.nextInt(5);

		PotionEffect pe = new PotionEffect(PotionEffectType.values()[r.nextInt(PotionEffectType
				.values().length)], ((d >= 160 && r.nextInt(4) == 0) || (d >= 280 &&
				r.nextInt(5) == 0) || (d >= 370 && r.nextInt(6) == 0) ? d / 3 : d),
				((a > 1 && r.nextInt(4) == 0) || (a > 3 && r.nextInt(6) == 0) ? a - 1 : a),
				true, true, true);

		pm.addCustomEffect(pe, true);
		item.setItemMeta(pm);

		IB.name(item, Lang.colorFromRGB(col.getRed(), col.getGreen(), col.getBlue())
				+ String.format(Lang.getMessage(Lang.getServerLang(), "potion"),
				pe.getType().getName().replaceAll("_", " ")));
		if (r.nextInt(4) == 0) effect(item);
	}

	public boolean canEdit(Material m) {
		for (String s : editable) if (m.toString().contains(s)) return true;
		return false;
	}

	public void edit(ItemStack item) {
		if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION ||
				item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW) effect(item);
		else enchant(item);
	}

	public static void enchant(ItemStack item) {
		Random r = new Random();
		int l = r.nextInt(5), c = r.nextInt(3);

		if (!(l - c <= 0 || l - c >= 3)) return;
		int lvl = r.nextInt(4);

		IB.ench(item, reEnch(item), (lvl == 0 ? lvl + 1 : lvl));
		if (l - c <= -1) IB.ench(item, reEnch(item), (lvl == 0 ? lvl + 1 : lvl));
	}

	public static Enchantment reEnch(ItemStack item) {
		Random r = new Random();
		@NotNull Enchantment[] v = Enchantment.values();

		int i = r.nextInt(v.length);
		Enchantment ench = v[i];

		while (!applicable(item, ench)) {
			if (i >= v.length - 1) i = -1;

			ench = v[i + 1];
			i++;
		}

		return ench;
	}

	public static boolean applicable(ItemStack item, Enchantment ench) {
		return item.getType() == Material.ENCHANTED_BOOK ||
				(ench.getItemTarget().includes(item) && ench.canEnchantItem(item));
	}
}