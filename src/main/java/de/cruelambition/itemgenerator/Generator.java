package de.cruelambition.itemgenerator;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import de.cruelambition.oo.utils.IB;
import de.cruelambition.oo.utils.Items;
import de.cruelambition.oo.PC;
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

import java.util.*;

public class Generator {

	private static List<Material> common, rare, forbidden, spawnEggs;
	private static List<String> editable;

	private boolean stopGenerator;
	private BukkitTask g;
	private final FileConfiguration c;

	public Generator(boolean alternativeGenerator) {
		common = new ArrayList<>();
		rare = new ArrayList<>();

		forbidden = new ArrayList<>();
		spawnEggs = new ArrayList<>();

		editable = new ArrayList<>();
		c = ItemGenerator.getItemGenerator().getConfig();

		syncForbidden();
		syncRare();

		setupCommon();
		extractSpawnEggs();
		more();

		removeForbiddenItems(common);
		setupEditable();

		List<Integer> frequencies = getFrequencies();

		if (!alternativeGenerator) {
			startGeneratorLoop(frequencies.get(0), frequencies.get(1));

			Bukkit.getConsoleSender().sendMessage(Lang.PRE + Language.getMessage(
					Language.getServerLang(), "starting_classic_generator"));
		} else {
			alternativeGenerator(frequencies.get(0), frequencies.get(1));

			Bukkit.getConsoleSender().sendMessage(Lang.PRE + Language.getMessage(
					Language.getServerLang(), "starting_alternative_generator"));
		}
	}

	// Generator Loop Start

	public List<Integer> getFrequencies() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		int gsi = c.getInt("Loops.Generator.StartIn", 12),
				gf = c.getInt("Loops.Generator.Frequency", 25),
				csi = c.getInt("Loops.Check.StartIn", 60),
				cf = c.getInt("Loops.Check.Frequency", 80);

		return new ArrayList<>(Arrays.asList(gsi, gf, csi, cf));
	}

	public void setFrequencies(int csi, int cf, int gsi, int gf) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		c.set("Loops.Generator.StartIn", gsi);
		c.set("Loops.Generator.Frequency", gf);

		c.set("Loops.Check.StartIn", csi);
		c.set("Loops.Check.Frequency", cf);

		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void startGeneratorLoop(int startIn, int frequency) {
		g = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {

			if (stopGenerator) {
				stopGenerator = false;
				Bukkit.getConsoleSender().sendMessage(Lang.PRE + Language.getMessage(
						Language.getServerLang(), "stopping_generator_old"));
				return;
			}

			giveAll();
		}, 20L * startIn, 20L * frequency);
	}

	public void alternativeGenerator(int startIn, int frequency) {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		Bukkit.getConsoleSender().sendMessage(Lang.PRE + "StartIn: " + startIn + " || Frequency: " + frequency);

		g = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
//			Bukkit.getConsoleSender().sendMessage(Lang.PRE + Language.getMessage(
//					Language.getServerLang(), "reloop_in_alternative"));

			PC pc;
			World spawn = Bukkit.getWorld("Spawn");

			for (Player ap : Bukkit.getOnlinePlayers()) {
				if (ap.getWorld() == spawn) continue;

				pc = new PC(ap);
				if (!pc.isSet("Generator.canGenerate") || !pc.mayGenerateItem()) {
					ap.sendMessage(Lang.PRE + Lang.getMessage(pc.getLanguage(), "generator_ready"));

					pc.allowItemGeneration();
					pc.savePCon();
				}
			}

			c.set("Generator.Delay", frequency + 1);
			ItemGenerator.getItemGenerator().saveConfig();
		}, 20L * startIn, 20L * frequency);

		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {

			c.set("Generator.Delay", c.getInt("Generator.Delay") - 1);
			ItemGenerator.getItemGenerator().saveConfig();

		}, 20L * startIn, 20);
	}

	public void stopGeneratorLoop() {
		stopGenerator = true;
		g.cancel();
	}

	public void restartGeneratorLoop(int startIn, int frequency) {
		stopGeneratorLoop();

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
				() -> startGeneratorLoop(startIn, frequency), 100);
	}

	public void give(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 0.5f, 0.8f);

		ItemStack is = new ItemStack(Material.AIR);
		Random r = new Random();

		is.setType(r.nextInt(10) == 0 ? getRare() : getCommon());
		Material type = is.getType();

		if (canEdit(type) && r.nextInt(4) == 0) edit(is);
		else if (type.toString().contains("SPAWN_EGG")) edit(is);

		Lang l = new Lang(p);
		if (isCustomItem(type)) {

			p.sendMessage(Lang.PRE + l.getString("rolling_custom_item"));
			is = Items.getCustomItem(type, r.nextInt(getCustomItemAmount(type) + 1) + 1);

//				ap.sendMessage("cmd: " + cmd + " || type: " + type);
			p.sendMessage(Lang.PRE + String.format(l.getString("received_custom_item")),
					is.getItemMeta().getDisplayName());
		}

		String s = type.toString().toLowerCase().replaceAll("_", " ");
		if (isRare(type)) p.sendMessage(Lang.PRE + l.getString("receiving_rare_item"));

		if (p.getInventory().firstEmpty() != -1) {

			p.getInventory().addItem(is);
			p.sendActionBar(Lang.PRE + String.format(l.getString("generated_item_inv"), s));

		} else {
			p.getWorld().dropItemNaturally(p.getLocation(), is);
			p.sendActionBar(Lang.PRE + String.format(l.getString("generated_item_drop"), s));
		}

		PC pc = new PC(p);
		List<String> list = pc.isSet("Generator.Receiving.Materials") ?
				pc.getStringList("Generator.Receiving.Materials") : new ArrayList<>();

		if (list.size() >= 8) list.remove(0);
		list.add(System.currentTimeMillis() + "::" + type.toString());

		pc.set("Generator.Receiving.Materials", list);
		pc.savePCon();
	}

	public void giveAll() {
		List<Player> wP = Bukkit.getWorld("world").getPlayers();
		if (wP == null) throw new RuntimeException(
				Lang.getMessage(Language.getServerLang(), "giveall_world_player_invalid"));

		for (Player ap : wP) {
			if (ap.getGameMode() != GameMode.SURVIVAL) continue;
			give(ap);
		}
	}

	// Generator Loop End

	// Item Handling Start

	public int getCustomItemAmount(Material m) {
		for (int a = 0; a <= Items.mats.size() - 1; a++)
			if (Items.mats.get(a) == m) return Items.amount[a];

		return -1;
	}

	public void rollSpawnEgg(ItemStack item) {
		item.setType(spawnEggs.get(new Random().nextInt(spawnEggs.size()) + 1));
	}

	public void addAllCustomItems(List<Material> list) {
		for (int a = 0; a <= Items.mats.size() - 1; a++)
			for (int b = 0; b <= Items.amount[a]; b++)
				list.add(Items.mats.get(a));
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

	public static boolean applicable(ItemStack item, Enchantment ench) {
		return item.getType() == Material.ENCHANTED_BOOK ||
				(ench.getItemTarget().includes(item) && ench.canEnchantItem(item));
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

	// Item Handling End

	// Generator List Setup Start

	public void syncRare() {
		if (!c.isSet("Generator.Lists.Rare")) setupRare();

		List<Material> rareTemp = new ArrayList<>();
		for (String s : c.getStringList("Generator.Lists.Rare")) rareTemp.add(Material.valueOf(s));

		addAllCustomItems(rareTemp);
		removeForbiddenItems(rare = rareTemp);
	}

	public void setupRare() {
		List<String> rareTemp = new ArrayList<>(),
				temp = new ArrayList<>(Arrays.asList("NETHERITE", "DIAMOND", "BEACON", "NETHER", "END", "DRIPSTONE",
						"SPAWN", "IRON", "OBSIDIAN", "_SHULKER", "DIRT", "BARRIER", "ELYTRA", "TRIM", "BEDROCK", "DISC"));


		for (String s : temp)
			for (Material m : Material.values())
				if (m.toString().contains(s)) rareTemp.add(m.toString());

		c.set("Generator.Lists.Rare", rareTemp);
		c.set("Generator.Lists.Forbidden", rareTemp);

		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void syncForbidden() {
		if (!c.isSet("Generator.Lists.Forbidden")) setupForbidden();

		List<Material> forbiddenTemp = new ArrayList<>();
		for (String s : c.getStringList("Generator.Lists.Forbidden")) forbiddenTemp.add(Material.valueOf(s));

		forbidden = forbiddenTemp;
	}

	public void setupForbidden() {
		List<String> forbiddenTemp = new ArrayList<>(),
				temp = new ArrayList<>(Arrays.asList("STRUCTURE", "COMMAND", "JIGSAW", "DEBUG", "STEM", "AIR",
						"_PANE", "BOAT", "MINECART", "PRESSURE_PLATE", "BUTTON", "BANNER", "CANDLE", "WALL", "HANGING",
						"POTTE", "LEGACY", "ARMOR_TRIM_SMITHING_TEMPLATE", "_BED"));

		for (String s : temp)
			for (Material m : Material.values())
				if (m.toString().contains(s)) forbiddenTemp.add(m.toString());

		c.set("Generator.Lists.Forbidden", forbiddenTemp);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void setupCommon() {
		common.addAll(Arrays.asList(Material.values()));
		removeRareItems(common);
	}

	public void more() {
		for (int i = 0; i <= 4; i++) // Total: 6
			addMaterialToCommon(Material.POTION);
		for (int i = 0; i <= 2; i++) // Total: 4
			addMaterialToCommon(Material.SPLASH_POTION);

		for (int i = 0; i <= 1; i++) // Total: 3
			addMaterialToCommon(Material.LINGERING_POTION);
		for (int i = 0; i <= 1; i++) // Total: 3
			addMaterialToCommon(Material.TIPPED_ARROW);

		for (int i = 0; i <= 4; i++) // Total: 6
			addMaterialToCommon(Material.ENCHANTED_BOOK);
		for (int i = 0; i <= 3; i++) // Total: 4 || 1=>3
			addMaterialToRare(Material.ALLAY_SPAWN_EGG);
	}

	public void setupEditable() {
		editable = new ArrayList<>(Arrays.asList(Material.ENCHANTED_BOOK.toString(),
				Material.POTION.toString(), Material.SPLASH_POTION.toString(),
				Material.TIPPED_ARROW.toString(), Material.LINGERING_POTION.toString(),
				"CHESTPLATE", "LEGGINGS", "BOOTS", "HELMET", "_SWORD", "_PICKAXE", "_AXE", "_SHOVEL",
				Material.ALLAY_SPAWN_EGG.toString()));
	}

	public void addMaterialToRare(Material m) {
		rare.add(m);
	}

	public void addMaterialToCommon(Material m) {
		common.add(m);
	}

	public void extractSpawnEggs() {
		if (rare.isEmpty() || rare == null) throw new RuntimeException("Rare list is empty");

		for (Material m : rare) {
			if (!(m.toString().contains("SPAWN_EGG") && !m.toString().contains("ALLAY"))) continue;
			if (!spawnEggs.contains(m)) spawnEggs.add(m);
		}

		for (Material mat : spawnEggs) rare.remove(mat);

		if (!spawnEggs.contains(Material.ALLAY_SPAWN_EGG)) spawnEggs.add(Material.ALLAY_SPAWN_EGG);
		if (!rare.contains(Material.ALLAY_SPAWN_EGG)) rare.add(Material.ALLAY_SPAWN_EGG);
	}

	public boolean isEditable(Material m) {
		for (String s : editable) if (m.toString().contains(s)) return true;
		return false;
	}

	public boolean isCustomItem(Material m) {
		return Items.mats.contains(m);
	}

	public boolean isForbidden(Material m) {
		return forbidden.contains(m);
	}

	public void removeForbiddenItems(List<Material> mat) {
		for (Material m : forbidden)
			if (mat.contains(m)) mat.remove(m);
	}

	public boolean isRare(Material m) {
		return rare.contains(m);
	}

	public void removeRareItems(List<Material> mat) {
		for (Material m : rare)
			if (mat.contains(m)) mat.remove(m);
	}

	public Material getCommon() {
		return common.get(new Random().nextInt(common.size()) + 1);
	}

	public Material getCommonFromInt(int i) {
		return common.get(i);
	}

	public Material getRare() {
		return rare.get(new Random().nextInt(rare.size()) + 1);
	}

	public Material getRareFromInt(int i) {
		return rare.get(i);
	}

	// Generator List Setup End

}
