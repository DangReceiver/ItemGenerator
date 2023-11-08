package de.cruelambition.itemgenerator;

import java.util.*;
import java.util.List;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.IB;
import de.cruelambition.oo.Items;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
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
	private final List<String> material, forbidden, editable;
	private final List<Material> spawnEggs = new ArrayList<>();
	private List<String> common;
	private BukkitTask generatorLoop, checkLoop;

	public Generator() {
		material = new ArrayList<>();
		forbidden = new ArrayList<>();
		common = new ArrayList<>();

		fillList();
		initiateCommonItems();
		syncForbiddenItems();

		editable = new ArrayList<>(Arrays.asList(Material.ENCHANTED_BOOK.toString(),
				Material.POTION.toString(), Material.SPLASH_POTION.toString(),
				Material.TIPPED_ARROW.toString(), Material.LINGERING_POTION.toString(),
				"CHESTPLATE", "LEGGINGS", "BOOTS", "HELMET", "_SWORD", "_PICKAXE", "_AXE", "_SHOVEL",
				Material.ALLAY_SPAWN_EGG.toString()));

		for (int a = 0; a <= Items.mats.size() - 1; a++)
			for (int b = 0; b <= Items.amount[a]; b++)
				addMaterialToLoop(Items.mats.get(a));
	}

	public List<String> invertRareList() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

		List<String> rare = c.getStringList("Item.List.Rare");
		List<String> base = new ArrayList<>();

//		Bukkit.getConsoleSender().sendMessage(material.toString() + " || " + (material == null));
		for (Material val : Material.values()) {
			for (String st : rare) {

				if (!val.toString().contains(st) && !common.contains(val.toString()))
					addCommonItem(val.toString(), null);

				if (val.toString().contains("SPAWN_EGG")) {
					if (!spawnEggs.contains(val)) spawnEggs.add(val);

					if (!val.toString().contains("ALLAY")) {
						removeItemFromCommonList(val);
						removeItemFromList(val);
					}
//					Bukkit.getConsoleSender().sendMessage("Adding " + val.toString() + " to spawn egg list");
				}
			}
		}

		addCommonItem(Material.ALLAY_SPAWN_EGG.toString(), null);
//		Bukkit.getConsoleSender().sendMessage(base.toString() + " || " + base.size());

		return common;
	}

	public List<String> setupCommonList() {
		List<String> rare = Arrays.asList("_SHULKER", "NETHERITE", "DIAMOND", "BEACON", "DIRT", "SPAWN_EGG",
				"SPAWNER", "BARRIER", "BEDROCK", "ENCHANTMENT_TABLE", "_BUCKET", "ELYTRA", "_TRIM", "EMERALD",
				"NETHER", "BEACON", "FRAME", "OBSIDIAN", "STRUCTURE", "ENDER", "CAMPFIRE", "TABLE", "DRAGON",
				"HEAD", "SKULL");

		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<String> base = c.getStringList("Item.List.Rare");

		for (String val : material) {
			for (String st : rare) {

				if (!val.contains(st) && !common.contains(val.toString()))
					addCommonItem(val, null);

				if (val.contains("SPAWN_EGG")) {
					if (!spawnEggs.contains(Material.valueOf(val))) spawnEggs.add(Material.valueOf(val));

					if (!val.contains("ALLAY")) {
						removeItemFromList(Material.valueOf(val));
						removeItemFromCommonList(Material.valueOf(val));
					}
					Bukkit.getConsoleSender().sendMessage("Adding " + val + " to spawn egg list");
				}
			}
		}

		addCommonItem(Material.ALLAY_SPAWN_EGG.toString(), null);
//		Bukkit.getConsoleSender().sendMessage(base.toString() + " || " + base.size());

		c.set("Item.List.Rare", base);
		ItemGenerator.getItemGenerator().saveConfig();
		return common;
	}

	public int getCustomItemAmount(Material m) {
		for (int a = 0; a <= Items.mats.size() - 1; a++)
			if (Items.mats.get(a) == m) return Items.amount[a];

		return -1;
	}

	public List<String> getForbiddenList() {
		return forbidden;
	}

	public static void initiate(Generator g, int csi, int cf, int gsi, int gf) {
		g.more();
		g.removeAllForbiddenItemsFromMaterialList();

//		Bukkit.getConsoleSender().sendMessage(g.material.toString());
		g.startGeneratorLoop(gsi, gf);

//		g.checkForForbiddenItemsLoop(csi, cf);
//		g.listForbiddenItems();
	}

	public void initiateCommonItems() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		common = c.isSet("Item.List.Rare") ? invertRareList() : setupCommonList();
	}

	public void more() {
		for (int i = 0; i <= 4; i++) // Total: 6
			addMaterialToLoop(Material.POTION);
		for (int i = 0; i <= 2; i++) // Total: 4
			addMaterialToLoop(Material.SPLASH_POTION);

		for (int i = 0; i <= 1; i++) // Total: 3
			addMaterialToLoop(Material.LINGERING_POTION);
		for (int i = 0; i <= 1; i++) // Total: 3
			addMaterialToLoop(Material.TIPPED_ARROW);

		for (int i = 0; i <= 4; i++) // Total: 6
			addMaterialToLoop(Material.ENCHANTED_BOOK);
		for (int i = 0; i <= 4; i++) // Total: 6 || 1=>3
			addMaterialToLoop(Material.ALLAY_SPAWN_EGG);
	}

	public void addMaterialToLoop(Material m) {
		material.add(m.toString());
	}

	public List<Integer> getFrequencies() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		int csi = c.getInt("Loops.Check.StartIn", 60),
				cf = c.getInt("Loops.Check.Frequency", 80),
				gsi = c.getInt("Loops.Generator.StartIn", 12),
				gf = c.getInt("Loops.Generator.Frequency", 25);

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

	public void restart(int csi, int cf, int gsi, int gf) {
		stopLoop();
		setFrequencies(csi, cf, gsi, gf);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> initiate(
				this, csi, cf, gsi, gf), 5);
	}

	public void fillList() {
		for (Material value : Material.values()) material.add(value.toString());
	}

	public void removeItemFromForbiddenList(Material m) {
		forbidden.remove(m.toString());
	}

	public void removeAllForbiddenItemsFromPlayer(Player p) {
		for (ItemStack c : p.getInventory().getContents())
			if (c != null && forbidden.contains(c.getType().toString())) c.setType(Material.AIR);
	}

	public void removeAllForbiddenItemsFromAllPlayers() {
		for (Player ap : Bukkit.getOnlinePlayers())
			removeAllForbiddenItemsFromPlayer(ap);
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

		List<String> sl = c.getStringList("Item.List.Forbidden");
		for (String m : sl) addItemToForbiddenList(m.toString());

		removeAllForbiddenItemsFromMaterialList();
//		publiciseForbiddenItems();
	}

	public void publiciseForbiddenItems() {
		Lang.broadcastArg(String.format(Lang.getMessage(Lang.getServerLang(),
				"itemgenerator_forbiddenlist_add_item"), getForbiddenList()));

		Bukkit.getConsoleSender().sendMessage(Lang.PRE + String.format(Lang.getMessage(
				Lang.getServerLang(), "itemgenerator_forbiddenlist_add_item"), getForbiddenList()));
	}

	public void addItemToForbiddenList(String m) {
		if (forbidden.contains(m)) return;

		for (Material mt : Material.values())
			if (mt.toString().contains(m)) forbidden.add(m);
	}

	public void removeAllForbiddenItemsFromMaterialList() {
		if (material.isEmpty() || material == null) return;
//		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		for (Material m : Material.values())
			for (String s : forbidden) {
				if (m.toString().contains(s) && material.contains(m.toString())) {
					removeItemFromList(m);
				}
				if (m.toString().contains(s) && common.contains(m.toString())) {
					removeItemFromCommonList(m);
				}
			}
	}

	public void removeItemFromList(Material m) {
		material.remove(m.toString());
	}

	public void removeItemFromCommonList(Material m) {
		if (!common.contains(m.toString())) common.remove(m.toString());
	}

	public boolean isForbiddenItem(Material m) {
		return forbidden.contains(m.toString());
	}

	public Material getRandomMaterial() {
		return Material.valueOf(material.get(new Random().nextInt(material.size() - 1)));
	}

	public Material getRandomCommonMaterial() {
		return Material.valueOf(common.get(new Random().nextInt(common.size() - 1)));
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
			ap.playSound(ap.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 0.45f, 0.8f);

			ItemStack is;
			Random r = new Random();

			if (r.nextInt(5) == 0) {
				is = new ItemStack(getRandomMaterial());
				ap.sendMessage("from rare: " + is.getType());
			} else {
				is = new ItemStack(getRandomCommonMaterial());
				ap.sendMessage("from common: " + is.getType());
			}

			Material type = is.getType();
			if (type.toString().contains("SPAWN_EGG")) {
				edit(is);
			}

			if (r.nextInt(2) == 0 && canEdit(type)) edit(is);

			if (type.isBlock()) if (r.nextInt(2) == 0)
				is.setAmount(r.nextInt(2) == 0 ? (Math.max((r.nextInt(8) + 1) - 4, 1))
						: r.nextInt(10) + 1);

			if (isCustomItem(type)) {
				ap.sendMessage("§5§kkk §5Receiving custom item... §5§kkk");
				int cmd;

				is = Items.getCustomItem(type, cmd = r.nextInt(getCustomItemAmount(type) + 1) + 1);
				type = is.getType();

				ap.sendMessage("cmd: " + cmd + " || type: " + type);
				ap.sendMessage(String.format("§5§kkk §5Received custom item %s §5§kkk", is.getItemMeta().getDisplayName()));

			} else moreItems(is);

//			ap.sendMessage("amount:" + is.getAmount());
			if (ap.getInventory().firstEmpty() != -1) {

				ap.getInventory().addItem(is);
				ap.sendActionBar(Lang.PRE + String.format(new Lang(ap).getString("generated_item_inv"),
						type.toString().toLowerCase().replaceAll("_", " ")));

			} else {
				ap.getWorld().dropItemNaturally(ap.getLocation(), is);
				ap.sendActionBar(Lang.PRE + String.format(new Lang(ap).getString("generated_item_drop"),
						type.toString().toLowerCase().replaceAll("_", " ")));
			}
		}
	}

	public void rollSpawnEgg(ItemStack item) {
		Bukkit.getConsoleSender().sendMessage("§6Rolling Spawn egg... ");
		Material type = spawnEggs.get(new Random().nextInt(spawnEggs.size()));
		item.setType(type);
		Bukkit.getConsoleSender().sendMessage("§6Rolled Spawn egg: " + type);
	}

	public boolean isCustomItem(Material m) {
		return Items.mats.contains(m);
	}

	public boolean moreItems(ItemStack item) {
		if (item.getType().getMaxStackSize() <= 1) return false;
		boolean b = false;

		List<String> l = new ArrayList<>(Arrays.asList("ARROW", "WOOL", "DEEPSLATE", "STONE", "BRICK",
				"CLAY", "GLASS", "PLANKS", "LOG"));
		for (String s : l) if (item.getType().toString().contains(s)) b = true;

		if (b) {
			Random r = new Random();
			int i = r.nextInt(8) + 1;
			item.setAmount(i >= 5 && (r.nextInt(2) == 0) ? i / 2 : i);
		}

		return b;
	}

	public void addCommonItem(String s, List<String> base) {
		for (String string : forbidden)
			if (string.contains(s)) return;

		common.add(s);
		if (base != null) base.add(s);
	}

	public void removeCommonItem(String s) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		List<String> sl = c.getStringList("Item.List.Common");

		if (common.contains(s)) common.remove(s);
		if (sl.contains(s)) sl.remove(s);

		c.set("Item.List.Common", sl);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void setCommonItems(List<String> l) {
		ItemGenerator.getItemGenerator().getConfig().set("Item.List.Common", l);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public boolean isCommon(String s) {
		for (String ra : common) if (s.contains(ra)) return true;
		return false;
	}

	public boolean isCommonFromList(String s) {
		return common.contains(s);
	}

	public boolean isCommonFromConfig(ItemStack item) {
		for (String s : ItemGenerator.getItemGenerator().getConfig().getStringList("Item.List.Common"))
			if (item.getType().toString().contains(s)) return true;
		return false;
	}

	public List<String> getCommonItems() {
		return new ArrayList<>();
	}

	public static void effect(ItemStack item) {
		if (!(item.getItemMeta() instanceof PotionMeta pm)) return;
		Random r = new Random();

		Color col = Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
		pm.setColor(col);

		int d = r.nextInt(480 * 20);
		int a = r.nextInt(5);

		PotionEffect pe = new PotionEffect(PotionEffectType.values()[r.nextInt(PotionEffectType
				.values().length)], ((d >= 160 && r.nextInt(4) == 0) || (d >= 280 &&
				r.nextInt(5) == 0) || (d >= 370 && r.nextInt(6) == 0) ? d / 3 : d),
				((a > 1 && r.nextInt(5) == 0) || (a > 3 && r.nextInt(5) == 0) ? a - 1 : a),
				true, true, true);

		pm.addCustomEffect(pe, true);
		item.setItemMeta(pm);

		IB.name(item, Lang.colorFromRGB(col.getRed(), col.getGreen(), col.getBlue())
				+ String.format(Lang.getMessage(Lang.getServerLang(), "potion"),
				pe.getType().getName().replaceAll("_", " ").toLowerCase()));
		if (pm.getCustomEffects().size() <= 5 && r.nextInt(3) == 0) effect(item);
	}

	public boolean canEdit(Material m) {
		for (String s : editable) if (m.toString().contains(s)) return true;
		return false;
	}

	public void edit(ItemStack item) {
		if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION ||
				item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW) effect(item);

		else if (item.getType().toString().contains("SPAWN_EGG")) rollSpawnEgg(item);
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