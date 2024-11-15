package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Recipes implements Listener {

	public static List<Recipe> rec;

	public Recipes() {
		rec = new ArrayList<>();

		ItemStack item = new ItemStack(Material.NETHERRACK);
		NamespacedKey key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "1");

		ShapelessRecipe netherrack1 = new ShapelessRecipe(key, item);

		netherrack1.addIngredient(5, Material.NETHER_WART);
		rec.add(netherrack1);


		item.setAmount(8);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "2");
		ShapelessRecipe netherrack2 = new ShapelessRecipe(key, item);
		netherrack2.addIngredient(4, Material.NETHER_WART_BLOCK);
		rec.add(netherrack2);


		item.setAmount(3);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "3");
		ShapelessRecipe netherrack3 = new ShapelessRecipe(key, item);
		netherrack3.addIngredient(2, Material.OBSIDIAN);
		netherrack3.addIngredient(2, Material.NETHER_WART);
		rec.add(netherrack3);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "5");
		ShapelessRecipe netherrack5 = new ShapelessRecipe(key, item);
		netherrack5.addIngredient(Material.NETHER_GOLD_ORE);
		rec.add(netherrack5);

		item.setAmount(1);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "4");
		ShapelessRecipe netherrack4 = new ShapelessRecipe(key, item);
		netherrack4.addIngredient(1, Material.NETHER_QUARTZ_ORE);
		rec.add(netherrack4);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.DIRT.toString());

		ItemStack dirt = new ItemStack(Material.DIRT);
		dirt.setAmount(2);

		ShapedRecipe dirtRec1 = new ShapedRecipe(key, dirt);
		dirtRec1.shape("GBG",
				"SDS",
				"CWC");

		dirtRec1.setIngredient('W', Material.WATER_BUCKET);
		dirtRec1.setIngredient('C', Material.CLAY_BALL);
		dirtRec1.setIngredient('S', Material.STONE);
		dirtRec1.setIngredient('B', Material.BONE_MEAL);
		dirtRec1.setIngredient('G', Material.SHORT_GRASS);
		dirtRec1.setIngredient('D', Material.EMERALD);
		rec.add(dirtRec1);


		dirt.setAmount(3);
		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "2");
		ShapelessRecipe dirtRec2 = new ShapelessRecipe(key, dirt);


		dirtRec2.addIngredient(Material.WATER_BUCKET);
		dirtRec2.addIngredient(3, Material.ROOTED_DIRT);
		dirtRec2.addIngredient(Material.GOLDEN_SWORD);
		rec.add(dirtRec2);


		dirt.setAmount(4);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "3");
		ShapedRecipe dirtRec3 = new ShapedRecipe(key, dirt);
		dirtRec3.shape(" C ",
				"CFC",
				" C ");

		dirtRec3.setIngredient('C', Material.COARSE_DIRT);
		dirtRec3.setIngredient('F', Material.FIRE_CHARGE);
		rec.add(dirtRec3);

		dirt.setAmount(3);
		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "4");
		ShapelessRecipe dirtRec4 = new ShapelessRecipe(key, dirt);


		dirtRec4.addIngredient(Material.WATER_BUCKET);
		dirtRec4.addIngredient(3, Material.ROOTED_DIRT);
		dirtRec4.addIngredient(Material.IRON_AXE);
		rec.add(dirtRec4);

		ItemStack boe = new ItemStack(Material.EXPERIENCE_BOTTLE);
		boe.setAmount(9);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 1);
		ShapedRecipe nBoeRec = new ShapedRecipe(key, boe);

		nBoeRec.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec.setIngredient('S', Material.SKELETON_SKULL);
		rec.add(nBoeRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 2);
		ShapedRecipe nBoeRec2 = new ShapedRecipe(key, boe);

		nBoeRec2.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec2.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec2.setIngredient('S', Material.WITHER_SKELETON_SKULL);
		rec.add(nBoeRec2);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 3);
		ShapedRecipe nBoeRec3 = new ShapedRecipe(key, boe);

		nBoeRec3.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec3.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec3.setIngredient('S', Material.CREEPER_HEAD);
		rec.add(nBoeRec3);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 4);
		ShapedRecipe nBoeRec4 = new ShapedRecipe(key, boe);

		nBoeRec4.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec4.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec4.setIngredient('S', Material.DRAGON_HEAD);
		rec.add(nBoeRec4);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 5);
		ShapedRecipe nBoeRec5 = new ShapedRecipe(key, boe);

		nBoeRec5.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec5.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec5.setIngredient('S', Material.PIGLIN_HEAD);
		rec.add(nBoeRec5);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 6);
		ShapedRecipe nBoeRec6 = new ShapedRecipe(key, boe);

		nBoeRec6.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec6.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec6.setIngredient('S', Material.ZOMBIE_HEAD);
		rec.add(nBoeRec6);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + 7);
		ShapedRecipe nBoeRec7 = new ShapedRecipe(key, boe);

		nBoeRec7.shape("BBB",
				"BSB",
				"BBB");

		nBoeRec7.setIngredient('B', Material.GLASS_BOTTLE);
		nBoeRec7.setIngredient('S', Material.PLAYER_HEAD);
		rec.add(nBoeRec7);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.MAGMA_BLOCK.toString() + "1");
		ItemStack magma = new ItemStack(Material.MAGMA_BLOCK);
		magma.setAmount(3);

		ShapelessRecipe magmaRec = new ShapelessRecipe(key, magma);
		magmaRec.addIngredient(Material.LAVA_BUCKET);
		magmaRec.addIngredient(2, Material.NETHERRACK);
		rec.add(magmaRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.MAGMA_BLOCK.toString() + "2");
		magma.setAmount(2);

		ShapelessRecipe magmaRec2 = new ShapelessRecipe(key, magma);
		magmaRec2.addIngredient(Material.FIRE_CHARGE);
		magmaRec2.addIngredient(2, Material.NETHERRACK);
		rec.add(magmaRec2);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE.toString() + "1");
		ItemStack upgrade = new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE);

		ShapedRecipe upgradeRec = new ShapedRecipe(key, upgrade);
		upgradeRec.shape("DXD",
				"DRD",
				"DND");

		upgradeRec.setIngredient('D', Material.DIAMOND);
		upgradeRec.setIngredient('X', Material.OBSIDIAN);
		upgradeRec.setIngredient('R', Material.NETHERRACK);
		upgradeRec.setIngredient('N', Material.NETHERITE_BLOCK);
		rec.add(upgradeRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.LEATHER.toString() + "1");
		ItemStack rotten = new ItemStack(Material.RABBIT_HIDE);

		ShapedRecipe rottenFlesh = new ShapedRecipe(key, rotten);
		rottenFlesh.shape("RRR",
				"RRR",
				"RRR");

		rottenFlesh.setIngredient('R', Material.ROTTEN_FLESH);
		rec.add(rottenFlesh);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.LAPIS_LAZULI.toString() + "1");
		ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
		lapis.setAmount(2);

		ShapedRecipe lapisRec = new ShapedRecipe(key, lapis);
		lapisRec.shape("   ",
				"BPB",
				"SOS");

		lapisRec.setIngredient('B', Material.BLUE_DYE);
		lapisRec.setIngredient('P', Material.PHANTOM_MEMBRANE);
		lapisRec.setIngredient('O', Material.BOOK);
		lapisRec.setIngredient('S', Material.STONE);
		rec.add(lapisRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.LAPIS_LAZULI.toString() + "2");

		ShapedRecipe lapisRec2 = new ShapedRecipe(key, lapis);
		lapisRec2.shape("   ",
				"BPB",
				"SOS");

		lapisRec2.setIngredient('B', Material.LIGHT_BLUE_DYE);
		lapisRec2.setIngredient('P', Material.PHANTOM_MEMBRANE);
		lapisRec2.setIngredient('O', Material.BOOK);
		lapisRec2.setIngredient('S', Material.STONE);
		rec.add(lapisRec2);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.DIAMOND.toString() + "1");
		ItemStack diamond = new ItemStack(Material.DIAMOND);
		diamond.setAmount(3);

		ShapedRecipe diamondRec = new ShapedRecipe(key, diamond);
		diamondRec.shape("GLG",
				"EBE",
				"POP");

		diamondRec.setIngredient('L', Material.LIGHT_BLUE_DYE);
		diamondRec.setIngredient('E', Material.EMERALD);
		diamondRec.setIngredient('O', Material.OBSIDIAN);
		diamondRec.setIngredient('G', Material.GLASS);
		diamondRec.setIngredient('P', Material.TNT);
		diamondRec.setIngredient('B', Material.BRUSH);
		rec.add(diamondRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.LIGHT_GRAY_CONCRETE_POWDER.toString() + "1");
		ItemStack concrete = new ItemStack(Material.LIGHT_GRAY_CONCRETE_POWDER);
		concrete.setAmount(1);

		ShapedRecipe concreteRec = new ShapedRecipe(key, concrete);
		concreteRec.shape("MMM",
				"MFM",
				"MMM");

		concreteRec.setIngredient('M', Material.BONE_MEAL);
		concreteRec.setIngredient('F', Material.FIRE_CHARGE);
		rec.add(concreteRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.BLAZE_POWDER.toString() + "1");
		ItemStack blazePowder = new ItemStack(Material.BLAZE_POWDER);
		blazePowder.setAmount(1);

		ShapedRecipe blazePowderRec = new ShapedRecipe(key, blazePowder);
		blazePowderRec.shape("MMM",
				"BFB",
				"MMM");

		blazePowderRec.setIngredient('M', Material.BONE_MEAL);
		blazePowderRec.setIngredient('B', Material.BONE_BLOCK);
		blazePowderRec.setIngredient('F', Material.FLINT_AND_STEEL);
		rec.add(blazePowderRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.END_PORTAL_FRAME.toString() + "1");
		ItemStack frame = new ItemStack(Material.END_PORTAL_FRAME);
		frame.setAmount(2);

		ShapedRecipe frameRec = new ShapedRecipe(key, frame);
		frameRec.shape("OEO",
				"RNR",
				"BOB");

		frameRec.setIngredient('E', Material.ENDER_EYE);
		frameRec.setIngredient('B', Material.CRYING_OBSIDIAN);
		frameRec.setIngredient('R', Material.REINFORCED_DEEPSLATE);
		frameRec.setIngredient('N', Material.NETHER_STAR);
		frameRec.setIngredient('O', Material.OBSIDIAN);
		rec.add(frameRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.END_PORTAL_FRAME.toString() + "2");
		frame.setAmount(1);
		ShapedRecipe frameRec1 = new ShapedRecipe(key, frame);
		frameRec1.shape("RER",
				"ONO",
				"BOB");

		frameRec1.setIngredient('R', Material.NETHERITE_BLOCK);
		frameRec1.setIngredient('E', Material.ENDER_EYE);
		frameRec1.setIngredient('B', Material.BEACON);
		frameRec1.setIngredient('N', Material.NETHER_STAR);
		frameRec1.setIngredient('O', Material.OBSIDIAN);
		rec.add(frameRec1);

		ItemStack wheat = new ItemStack(Material.WHEAT);
		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.WHEAT.toString() + "1");
		wheat.setAmount(8);
		ShapedRecipe wheatRec = new ShapedRecipe(key, wheat);
		wheatRec.shape("SSS",
				"SBS",
				"SSS");

		wheatRec.setIngredient('S', Material.WHEAT_SEEDS);
		wheatRec.setIngredient('B', Material.BONE_BLOCK);
		rec.add(wheatRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.WHEAT.toString() + "2");
		wheat.setAmount(5);
		ShapedRecipe wheatRec2 = new ShapedRecipe(key, wheat);
		wheatRec2.shape("SSS",
				"BBB",
				"SSS");

		wheatRec2.setIngredient('S', Material.WHEAT_SEEDS);
		wheatRec2.setIngredient('B', Material.BONE_MEAL);
		rec.add(wheatRec2);

		ItemStack wool = new ItemStack(Material.GREEN_WOOL);
		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.GREEN_WOOL.toString() + "1");
		wool.setAmount(2);
		ShapedRecipe woolRec = new ShapedRecipe(key, wool);
		woolRec.shape("LFL",
				"FGF",
				"LFL");

		woolRec.setIngredient('F', Material.FEATHER);
		woolRec.setIngredient('L', Material.LEATHER);
		woolRec.setIngredient('G', Material.GREEN_DYE);
		rec.add(woolRec);

		int i = 1;
		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.CHEST.toString() + i);
		ItemStack chest = new ItemStack(Material.CHEST);
		ShapelessRecipe chestRec = new ShapelessRecipe(key, chest);

		for (Material m : Material.values()) {
			if (m.toString().contains("SHULKER_") && !m.toString().contains("_SHELL")) {

				chestRec.addIngredient(m);
				rec.add(chestRec);

				i++;

				key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.CHEST.toString() + i);
				chestRec = new ShapelessRecipe(key, chest);
			}
		}

		chestRec.addIngredient(Material.SHULKER_BOX);
		rec.add(chestRec);
	}

	public List<Recipe> getRec() {
		return rec;
	}

	@EventHandler
	public void handle(CraftItemEvent e) {
		if (e.getRecipe().getResult().getType() != Material.CHEST) return;
		if (!(e.getRecipe() instanceof Keyed k)) return;

		if (!k.getKey().toString().contains("itemgenerator:")) return;
		if (!(e.getWhoClicked() instanceof Player p)) return;

		Random r = new Random();
		int i = r.nextInt(7) + 1;

		delayedSpawning(p.getLocation(), EntityType.THROWN_EXP_BOTTLE, 4, i, 0);
	}

	public static void delayedSpawning(Location loc, EntityType type, int delay, int times, int count) {
		if (count >= times) return;
		loc.getWorld().spawnEntity(loc.clone().add(0, 0.4, 0), type);

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
				() -> delayedSpawning(loc, type, delay, times, count + 1), delay);
	}
}
