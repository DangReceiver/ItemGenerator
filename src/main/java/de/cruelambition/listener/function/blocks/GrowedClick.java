package de.cruelambition.listener.function.blocks;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;
import de.cruelambition.oo.IB;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class GrowedClick implements Listener {

	public static HashMap<Material, Material> seeds = new HashMap<>();

	public static void fillSeedList() {
		seeds.put(Material.WHEAT, Material.WHEAT_SEEDS);
		seeds.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
		seeds.put(Material.CARROTS, Material.CARROT);
		seeds.put(Material.POTATOES, Material.POTATO);
		seeds.put(Material.NETHER_WART, Material.NETHER_WART);
	}

	@EventHandler
	public void interactGrowed(final PlayerInteractEvent e) {
		if (e.isCancelled()) return;
		Player p = e.getPlayer();

		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK && p.getGameMode() != GameMode.SPECTATOR
				&& p.getGameMode() != GameMode.ADVENTURE)) return;

		Lang l = new Lang(p);

		if (e.getClickedBlock().getBlockData().toString().contains("[age=7]")) {
			switch (e.getClickedBlock().getType()) {

				default:
					return;

				case WHEAT, CARROTS, POTATOES: {
					if (IB.getMaterialAmount(seeds.get(e.getClickedBlock().getType()), p.getInventory()) < 1) {
						p.sendMessage(Lang.PRE + l.getString("not_enough_seeds"));
						return;
					}

					IB.removeItems(seeds.get(e.getClickedBlock().getType()), 1, p.getInventory());
					e.setCancelled(true);

					for (final ItemStack i : e.getClickedBlock().getDrops())
						p.getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), i);
					e.getClickedBlock().setType(e.getClickedBlock().getType());
				}
			}
			p.playSound(p.getLocation(), Sound.ITEM_CROP_PLANT, 0.5f, 1.2f);

			Random r = new Random();
			if (r.nextInt(17) != 1) return;

			for (int i = 0; i < r.nextInt(3); i++)
				p.getWorld().spawnEntity(p.getLocation(), EntityType.THROWN_EXP_BOTTLE);

		} else if (e.getClickedBlock().getBlockData().toString().contains("[age=3]")) {

			switch (e.getClickedBlock().getType()) {
				default:
					return;

				case NETHER_WART, BEETROOTS: {

					if (IB.getMaterialAmount(seeds.get(e.getClickedBlock().getType()), p.getInventory()) < 1) {
						p.sendMessage(Lang.PRE + l.getString("not_enough_seeds"));
						return;
					}

					IB.removeItems(seeds.get(e.getClickedBlock().getType()), 1, p.getInventory());
					e.setCancelled(true);

					for (final ItemStack i : e.getClickedBlock().getDrops())
						p.getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), i);
					e.getClickedBlock().setType(e.getClickedBlock().getType());
				}
			}
			p.playSound(p.getLocation(), Sound.ITEM_CROP_PLANT, 0.5f, 1.1f);

			Random r = new Random();
			if (r.nextInt(15) != 1) return;

			for (int i = 0; i < r.nextInt(3); i++)
				p.getWorld().spawnEntity(p.getLocation(), EntityType.THROWN_EXP_BOTTLE);
		}
	}
}
