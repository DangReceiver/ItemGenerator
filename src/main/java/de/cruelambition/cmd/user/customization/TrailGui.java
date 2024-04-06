package de.cruelambition.cmd.user.customization;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.utils.IB;
import de.cruelambition.oo.PC;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TrailGui implements CommandExecutor, Listener {

	private static List<String> trail = new ArrayList<>();

	private static List<Particle> directional = new ArrayList<>(Arrays.asList(Particle.EXPLOSION_NORMAL,
			Particle.FIREWORKS_SPARK, Particle.WATER_BUBBLE, Particle.WATER_WAKE, Particle.CRIT,
			Particle.CRIT_MAGIC, Particle.SMOKE_NORMAL, Particle.SMOKE_LARGE, Particle.PORTAL,
			Particle.ENCHANTMENT_TABLE, Particle.FLAME, Particle.CLOUD, Particle.DRAGON_BREATH, Particle.END_ROD,
			Particle.DAMAGE_INDICATOR, Particle.TOTEM, Particle.SPIT, Particle.SQUID_INK, Particle.BUBBLE_POP,
			Particle.BUBBLE_COLUMN_UP, Particle.NAUTILUS, Particle.CAMPFIRE_COSY_SMOKE, Particle.CAMPFIRE_SIGNAL_SMOKE,
			Particle.SOUL_FIRE_FLAME, Particle.SOUL, Particle.REVERSE_PORTAL, Particle.SMALL_FLAME,
			Particle.ELECTRIC_SPARK, Particle.SCRAPE, Particle.WAX_OFF, Particle.WAX_ON, Particle.WAX_ON));

	public static final String tTitle = "§3Choose a trail", btTitle = "§aBuy trail";


	public static List<Particle> guiParticles = new ArrayList<>(Arrays.asList(Particle.BLOCK_DUST,
			Particle.CAMPFIRE_COSY_SMOKE, Particle.CLOUD, Particle.CRIMSON_SPORE, Particle.DAMAGE_INDICATOR,
			Particle.DRAGON_BREATH, Particle.DRIP_LAVA, Particle.DRIP_WATER, Particle.DRIPPING_HONEY,
			Particle.DRIPPING_OBSIDIAN_TEAR, Particle.ENCHANTMENT_TABLE, Particle.END_ROD, Particle.EXPLOSION_LARGE,
			Particle.FIREWORKS_SPARK, Particle.FLAME, Particle.HEART, Particle.NAUTILUS, Particle.NOTE,
			Particle.SOUL, Particle.SOUL_FIRE_FLAME, Particle.SPELL_INSTANT, Particle.SPELL_MOB,
			Particle.SPELL_WITCH, Particle.SWEEP_ATTACK, Particle.TOTEM, Particle.VILLAGER_HAPPY,
			Particle.WARPED_SPORE, Particle.WATER_WAKE));

	public static HashMap<Particle, String> parList = new HashMap<>();

	public static void fillParList() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		for (Particle par : guiParticles) {

			parList.put(par, "IdleSteams.trailsParticle." + par.toString().toLowerCase());
			String ps = par.toString().toUpperCase();

			if (!c.isSet("ParticleList." + ps)) {
				c.set("ParticleList." + ps + ".material", Material.GOLD_BLOCK.toString());
				c.set("ParticleList." + ps + ".cost", 16);

				c.set("ParticleList." + ps + ".color", "b");
				c.set("ParticleList." + ps + ".lore", "§7§oA particle");
			}
		}
		ItemGenerator.getItemGenerator().saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Lang l = new Lang(null);

		if (!(sen instanceof Player p)) {
			sen.sendMessage(Lang.getMessage(Lang.getServerLang(), "non_player"));
			return true;
		}

		l.setPlayer(p);
		Inventory i = Bukkit.createInventory(null, 6 * 9, tTitle);

		IB.invFiller(i, IB.getFiller(Material.GRAY_STAINED_GLASS_PANE, true, false, "", ""));
		p.openInventory(i);

		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

		int a = 10;
		for (Particle par : guiParticles) {

			while (!allowedSlot(a, i.getSize())) {
				if (a >= i.getSize()) return false;
				else a++;
			}

			if (!allowedSlot(a, i.getSize())) return false;
			List<String> lore = Lang.splitString(String.format(l.getString("BuyPreviewCost"),
					c.getDouble("ParticleList." + par.toString() + ".cost")));

			lore.add(0, c.getString("ParticleList." + par + ".lore"));
			i.setItem(a, IB.lore(IB.name(new ItemStack(Material.valueOf(c.getString("ParticleList."
					+ par + ".material").toUpperCase())), "§" // HERE
					+ c.getString("ParticleList." + par.toString() + ".color")
					+ par.toString().toLowerCase()), lore));
			a++;

		}
		return false;
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		HumanEntity he = e.getWhoClicked();
		if (!(he instanceof Player p)) return;

		Lang l = new Lang(p);

		if (p.getOpenInventory().getTitle().equalsIgnoreCase(tTitle)) {
			e.setCancelled(true);

			ItemStack ci = e.getCurrentItem();
			if (ci == null) return;

			if (ci.getType().toString().contains("PANE") && !ci.getItemMeta().hasDisplayName()) return;

			Particle tp;

			try {
				tp = Particle.valueOf(ChatColor.stripColor(ci.getItemMeta().getDisplayName().toUpperCase()));
			} catch (Exception ex) {
				tp = null;
			}

			if (tp == null) {
				p.sendMessage(Lang.PRE + l.getString("arg_invalid"));
				return;
			}

			if (p.hasPermission(parList.get(tp))) {
				PC pc = new PC(p);

				if (pc.isSet("Temp.particle")) {
					pc.set("Temp", null);
					pc.savePCon();

					trail.remove(p.getName());
					p.sendMessage(" \n" + Lang.PRE + l.getString("trail_stopped"));
				}

				if (tp == null) {
					p.sendMessage(Lang.PRE + String.format(l.getString("arg_null"), 0));
					return;
				}

				p.closeInventory();

				Particle finalTp = tp;
				Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
					if (!trail.contains(p.getName())) trail.add(p.getName());

					loopTrail(p, finalTp);
					pc.set("Temp.particle", finalTp.toString());
					pc.savePCon();

				}, 5);
				p.sendMessage(Lang.PRE + String.format(l.getString("particle_spawning"),
						tp.toString().toLowerCase()));
			} else {
				FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
				String ps = Particle.valueOf(ChatColor.stripColor(
						ci.getItemMeta().getDisplayName().toUpperCase())).toString();


				Inventory i = Bukkit.createInventory(null, 3 * 9, btTitle);
				IB.invFiller(i, IB.getFiller(new PC(p).getFiller(), true, false, "", ""));

				i.setItem(i.getSize() / 2 - 2, IB.name(new ItemStack(Material.GREEN_BANNER),
						l.getString("confirm_banner")));
				i.setItem(i.getSize() / 2, IB.lore(ci, Lang.splitString(String.format(
						l.getString("buy_preview_cost"), c.getDouble("ParticleList." + ps + ".cost")))));
				i.setItem(i.getSize() / 2 + 2, IB.name(new ItemStack(Material.RED_BANNER),
						l.getString("cancel_banner")));

				p.openInventory(i);
			}

		} else if (p.getOpenInventory().getTitle().equalsIgnoreCase(btTitle)) {
			e.setCancelled(true);

			ItemStack ci = e.getCurrentItem();
			if (ci == null) return;

			FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
			Inventory ti = p.getOpenInventory().getTopInventory();

			ItemStack parItem = ti.getItem(ti.getSize() / 2);
			double cost = c.getDouble("ParticleList." + ChatColor.stripColor(
					parItem.getItemMeta().getDisplayName().toUpperCase()) + ".cost");


			if (ci.getType().toString().contains("_GLASS_PANE")) {
				return;

			} else if (ci.getType() == Material.RED_BANNER) {
				p.closeInventory();

			} else if (ci.getType() == Material.GREEN_BANNER) {
				PC pc = new PC(p);

				if (IB.getMaterialAmount(Material.EMERALD, p.getInventory()) < 16) {
					p.sendMessage(Lang.PRE + String.format(l.getString("about_buyable_item"), cost));
					return;
				}

				IB.removeItems(Material.EMERALD, 16, p.getInventory());

				pc.savePCon();
				p.sendMessage(Lang.PRE + String.format(l.getString("trail_purchased"),
						parItem.getItemMeta().getDisplayName(), cost));

			}
			p.closeInventory();
		}
	}

	public void loopTrail(Player p, Particle par) {
		if (!trail.contains(p.getName()) || !p.isOnline()) return;

		String s = par.getDataType().toString();
		if (s.contains("Void")) {

			if (isDirectional(par)) p.getLocation().getWorld().spawnParticle(par,
					p.getLocation().add(0, 0.5, 0), 0, 0.1, 0.2, 0.2, 0.15);
			else p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1);

		} else if (s.contains("BlockData"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1,
					Material.GOLD_BLOCK.createBlockData());

		else if (s.contains("MaterialData"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1,
					new MaterialData(Material.GOLD_INGOT));

		else if (s.contains("DustTransition"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1,
					new Particle.DustTransition(Color.YELLOW, Color.ORANGE, 0.2f));

		else if (s.contains("ItemStack"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1,
					new ItemStack(Material.GOLD_INGOT));

		else if (s.contains("DustOptions"))
			p.getLocation().getWorld().spawnParticle(par, p.getLocation().add(0, 0.5, 0), 1,
					new Particle.DustOptions(Color.YELLOW, 0.1f));

		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> loopTrail(p, par), 4);
	}

	public boolean isDirectional(Particle p) {
		return directional.contains(p);
	}

	public static boolean allowedSlot(final int i, final int size) {
		return i < size && ((i >= 10 && i < 17) || (i >= 19 && i < 26)
				|| (i >= 28 && i < 35) || (i >= 37 && i < 44));
	}
}
