package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Recipes implements Listener {

	public static List<Recipe> rec;

	public Recipes() {
		rec = new ArrayList<>();

		ItemStack item = new ItemStack(Material.NETHERRACK);
		NamespacedKey key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "1");

		ShapedRecipe netherrack1 = new ShapedRecipe(key, item);
		netherrack1.shape(" W ",
				"WWW",
				" W ");

		netherrack1.setIngredient('W', Material.NETHER_WART);
		rec.add(netherrack1);


		item.setAmount(8);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "2");
		ShapedRecipe netherrack2 = new ShapedRecipe(key, item);
		netherrack2.shape("BB ",
				"BB ",
				"   ");

		netherrack2.setIngredient('B', Material.NETHER_WART_BLOCK);
		rec.add(netherrack2);


		item.setAmount(3);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "3");
		ShapedRecipe netherrack3 = new ShapedRecipe(key, item);
		netherrack3.shape("OW ",
				"WO ",
				"   ");

		netherrack3.setIngredient('O', Material.OBSIDIAN);
		netherrack3.setIngredient('W', Material.NETHER_WART);
		rec.add(netherrack3);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "5");
		ShapedRecipe netherrack5 = new ShapedRecipe(key, item);
		netherrack5.shape("G  ",
				"   ",
				"   ");

		netherrack5.setIngredient('G', Material.NETHER_GOLD_ORE);
		rec.add(netherrack5);

		item.setAmount(1);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.NETHERRACK.toString() + "4");
		ShapedRecipe netherrack4 = new ShapedRecipe(key, item);
		netherrack4.shape("Q  ",
				"   ",
				"   ");

		netherrack4.setIngredient('Q', Material.NETHER_QUARTZ_ORE);
		rec.add(netherrack4);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.DIRT.toString());

		ItemStack dirt = new ItemStack(Material.DIRT);
		dirt.setAmount(3);

		ShapedRecipe dirtRec1 = new ShapedRecipe(key, dirt);
		dirtRec1.shape("GBG",
				"SDS",
				"CWC");

		dirtRec1.setIngredient('W', Material.WATER_BUCKET);
		dirtRec1.setIngredient('C', Material.CLAY_BALL);
		dirtRec1.setIngredient('G', Material.GRASS);
		dirtRec1.setIngredient('S', Material.STONE);
		dirtRec1.setIngredient('B', Material.BONE_MEAL);
		dirtRec1.setIngredient('D', Material.EMERALD_BLOCK);
		rec.add(dirtRec1);


		dirt.setAmount(2);
		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "2");
		ShapedRecipe dirtRec2 = new ShapedRecipe(key, dirt);
		dirtRec2.shape("RW ",
				"SR ",
				"   ");

		dirtRec2.setIngredient('W', Material.WATER_BUCKET);
		dirtRec2.setIngredient('R', Material.ROOTED_DIRT);
		dirtRec2.setIngredient('S', Material.GOLDEN_SWORD);
		rec.add(dirtRec2);


		dirt.setAmount(2);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "3");
		ShapedRecipe dirtRec3 = new ShapedRecipe(key, dirt);
		dirtRec3.shape("   ",
				" C ",
				"CFC");

		dirtRec3.setIngredient('C', Material.COARSE_DIRT);
		dirtRec3.setIngredient('F', Material.FIRE_CHARGE);
		rec.add(dirtRec3);

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

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.MAGMA_BLOCK.toString() + "1");
		ItemStack magma = new ItemStack(Material.MAGMA_BLOCK);
		magma.setAmount(3);

		ShapedRecipe magmaRec = new ShapedRecipe(key, magma);
		magmaRec.shape("   ",
				"NLN",
				"   ");

		magmaRec.setIngredient('L', Material.LAVA_BUCKET);
		magmaRec.setIngredient('N', Material.NETHERRACK);
		rec.add(magmaRec);

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.MAGMA_BLOCK.toString() + "2");
		magma.setAmount(2);

		ShapedRecipe magmaRec2 = new ShapedRecipe(key, magma);
		magmaRec2.shape("   ",
				"NFN",
				"   ");

		magmaRec2.setIngredient('F', Material.FIRE_CHARGE);
		magmaRec2.setIngredient('N', Material.NETHERRACK);
		rec.add(magmaRec2);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE.toString() + "1");
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

		key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.LIGHT_GRAY_CONCRETE_POWDER.toString() + "1");
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


//		PotionEffectType.ABSORPTION
//		PotionEffectType.DAMAGE_RESISTANCE
//		PotionEffectType.FAST_DIGGING
//		PotionEffectType.GLOWING
//		PotionEffectType.HEALTH_BOOST
//		PotionEffectType.SATURATION
	}

	public List<Recipe> getRec() {
		return rec;
	}

	@EventHandler
	public void handle(CraftItemEvent e) {
//		ConsoleCommandSender cs = Bukkit.getConsoleSender();
//
//		cs.sendMessage(e.getRecipe().getResult().toString());
//		cs.sendMessage(e.getResult().toString());
//		cs.sendMessage(e.getWhoClicked().getName());
	}
}
