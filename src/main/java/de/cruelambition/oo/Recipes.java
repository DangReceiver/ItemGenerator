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

import java.util.ArrayList;
import java.util.List;

public class Recipes implements Listener {

	private List<Recipe> rec;

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
		dirt.setAmount(2);

		ShapedRecipe dirtRec1 = new ShapedRecipe(key, dirt);
		dirtRec1.shape("GBG",
				"SDS",
				"CWC");

		dirtRec1.setIngredient('W', Material.WATER_BUCKET);
		dirtRec1.setIngredient('C', Material.CLAY_BALL);
		dirtRec1.setIngredient('G', Material.GRASS);
		dirtRec1.setIngredient('S', Material.STONE);
		dirtRec1.setIngredient('B', Material.BONE_MEAL);
		dirtRec1.setIngredient('D', Material.DIAMOND);
		rec.add(dirtRec1);


		key = new NamespacedKey(ItemGenerator.getItemGenerator(),
				Material.DIRT.toString() + "2");
		ShapedRecipe dirtRec2 = new ShapedRecipe(key, dirt);
		dirtRec2.shape("RW ",
				"SR ",
				"   ");

		dirtRec2.setIngredient('W', Material.WATER_BUCKET);
		dirtRec2.setIngredient('R', Material.ROOTED_DIRT);
		dirtRec2.setIngredient('S', Material.SHEARS);
		rec.add(dirtRec2);


		dirt.setAmount(1);

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
		boe.setAmount(10);

		int i = 1;
		for (Material m : Material.values()) {

			if (m.toString().contains("SKULL") || m.toString().contains("HEAD")) {
				if (m == null || m.toString().contains("LEGACY") || m == Material.AIR ||
						m == Material.SKULL_POTTERY_SHERD || m == Material.SKULL_BANNER_PATTERN) continue;

				if (new ItemStack(m) == null || new ItemStack(m).getType() == Material.AIR) continue;

				key = new NamespacedKey(ItemGenerator.getItemGenerator(), Material.EXPERIENCE_BOTTLE.toString() + i);
				ShapedRecipe nBoeRec = new ShapedRecipe(key, boe);

				nBoeRec.shape("   ",
						" S ",
						" B ");

				nBoeRec.setIngredient('B', Material.GLASS_BOTTLE);
				nBoeRec.setIngredient('S', m);
				rec.add(nBoeRec);
				i++;
			}
		}


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
		ItemStack rotten = new ItemStack(Material.LEATHER);

		ShapedRecipe rottenFlesh = new ShapedRecipe(key, rotten);
		rottenFlesh.shape("RRR",
				"RRR",
				"RR ");

		rottenFlesh.setIngredient('R', Material.ROTTEN_FLESH);
		rec.add(rottenFlesh);
	}

	public List<Recipe> getRec() {
		return rec;
	}

	@EventHandler
	public void handle(CraftItemEvent e) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		cs.sendMessage(e.getRecipe().getResult().toString());
		cs.sendMessage(e.getResult().toString());
		cs.sendMessage(e.getWhoClicked().getName());
	}
}
