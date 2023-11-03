package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import io.papermc.paper.math.Rotations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WorldBorder implements Listener {

	private org.bukkit.WorldBorder wb, wbn;

	private ArmorStand asw, asn;
	private Lang l;
	private FileConfiguration c;

	public WorldBorder() {
		wb = Bukkit.getWorld("world").getWorldBorder();
		wbn = Bukkit.getWorld("world_nether").getWorldBorder();

		c = ItemGenerator.getItemGenerator().getConfig();

		l = new Lang(null);
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		if (wb == null || wbn == null) {
			cs.sendMessage(Lang.PRE + l.getString("wb_not_obtainable"));
			return;
		}

		if (!spawnUpgraderExists(wb.getWorld())) {
			cs.sendMessage(Lang.PRE + String.format(l.getString("spawning_wb_non_existing"),
					wb.getWorld().getName()));
			spawnUpgrader(wb.getWorld());
		}

		if (!spawnUpgraderExists(wbn.getWorld())) {
			cs.sendMessage(Lang.PRE + String.format(l.getString("spawning_wb_non_existing"),
					wbn.getWorld().getName()));
			spawnUpgrader(wbn.getWorld());
		}

		setDefaultWb(wb.getWorld());
		setDefaultWb(wbn.getWorld());

		asw = getSpawnUpgrader(wb.getWorld());
		asn = getSpawnUpgrader(wbn.getWorld());
	}

	public boolean spawnUpgraderExists(World w) {
//		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		for (Entity en : w.getNearbyEntities(new Location(w, 0.5, 64, 0.5), 8d, 8d, 8d)) {
//			cs.sendMessage("§2en: " + en.toString() + " || dn: " + (en.getCustomName() != null
//					? en.getCustomName() : null));

			if (en instanceof ArmorStand as && !as.hasGravity()) {
//				cs.sendMessage("§2is upgrader");
				return true;
			}
		}

		return false;
	}

	public ArmorStand getSpawnUpgrader(World w) {
		for (Entity en : w.getNearbyEntities(new Location(w, 0.5, 64, 0.5), 8d, 8d, 8d))
			if (en instanceof ArmorStand as && !as.hasGravity()) return as;

		return null;
	}

	public void spawnUpgrader(World w) {
		ArmorStand as = (ArmorStand) w.spawnEntity(new Location(w, 2.5, 67.0, 2.5), EntityType.ARMOR_STAND);

		as.setMaxHealth(1000);
		as.setHealth(1000);

		as.setGravity(false);
		as.setBasePlate(false);

		as.setCustomName(l.getString("as_wb"));
		as.setCustomNameVisible(true);

		as.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
		as.setHelmet(IB.ench(new ItemStack(Material.CREEPER_HEAD), Enchantment.BINDING_CURSE, 0));
	}

	public void setDefaultWb(World w) {
		org.bukkit.WorldBorder pwb = w.getWorldBorder();
		if (c.isSet("Border." + w.getName() + ".hadDefaults") && pwb != null) return;

		if (pwb == null) Bukkit.getConsoleSender().sendMessage(Lang.PRE
				+ new Lang(null).getString("wb_invalid_world"));
		else defaults(pwb);
	}

	public void defaults(org.bukkit.WorldBorder pWb) {
		pWb.setCenter(0.5, 0.5);
		pWb.setSize(pWb.getWorld().getName().contains("_nether") ? 21 * 3 : 21);
		pWb.setDamageAmount(0.25);
		pWb.setDamageBuffer(0.25);
		pWb.setWarningDistance(4);
		pWb.setWarningTime(2);

		c.set("Border." + pWb.getWorld().getName() + ".hadDefaults", true);
		c.set("Border." + pWb.getWorld().getName() + ".upgrade", 1);
		ItemGenerator.getItemGenerator().saveConfig();
	}

	public void increase() {
		wb.setSize(wb.getSize() + 4, 4);
		wbn.setSize(wbn.getSize() + 20, 4);
	}

	public void syncWb() {
		wb.setSize(Math.max(wb.getSize(), wbn.getSize() / 3), 6);
		wbn.setSize(Math.max(wb.getSize() * 3, wbn.getSize()), 6);
	}

	public int getUpgradeCost(World w) {
		if (w.getName().contains("_nether")) w = Bukkit.getWorld(w.getName().split("_nether")[0]);
		int cost;

		cost = (int) (c.isSet("Border." + w.getName() + ".upgrade") ?
				c.getInt("Border." + w.getName() + ".upgrade") * (w.getWorldBorder().getSize() / 25) : -1);

		return cost;
	}

	@EventHandler
	public void handle(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof ArmorStand as)) return;

		if (!(e.getDamager() instanceof Player p)) return;
		if (!as.getCustomName().equalsIgnoreCase(l.getString("as_wb"))) return;

		e.setCancelled(true);
		Lang lp = new Lang(p);

		if (c.isSet("Border." + p.getWorld().getName() + ".delay")) {
			p.sendMessage(Lang.PRE + l.getString("wb_upgrader_on_delay"));
			return;
		}

		Inventory inv = Bukkit.createInventory(null, 4 * 9, lp.getString("wb_upgrader_inv"));
		IB.invFiller(inv, IB.getFiller(new PC(p).getFiller(), true, true, null, null));

		inv.setItem(inv.getSize() / 2 - 3, IB.lore(IB.name(new ItemStack(Material.RED_BANNER),
				l.getString("cancel_purchase")), Utils.splitString(l.getString("cancel_purchase_lore"))));
		inv.setItem(inv.getSize() / 2 - 5, IB.lore(IB.name(new ItemStack(Material.KNOWLEDGE_BOOK),
				l.getString("purchase_info")), Utils.splitString(String.format(l.getString("wb_upgrade_info"),
				getUpgradeCost(as.getWorld())))));
		inv.setItem(inv.getSize() / 2 - 7, IB.lore(IB.name(new ItemStack(Material.LIME_BANNER),
				l.getString("confirm_purchase"))));

		p.openInventory(inv);
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player p)) return;

		Lang lp = new Lang(p);
		if (!p.getOpenInventory().getTitle().equalsIgnoreCase(lp.getString("wb_upgrader_inv"))) return;

		e.setCancelled(true);
		ItemStack is = e.getCurrentItem();

		if (is == null) return;
		switch (is.getType()) {

			default:
				break;

			case RED_BANNER:
				p.closeInventory();
				break;

			case LIME_BANNER:
				if (c.isSet("Border." + p.getWorld().getName() + ".delay")) {
					p.sendMessage(Lang.PRE + l.getString("wb_upgrader_on_delay"));
					return;
				}

				if (p.getLevel() < getUpgradeCost(p.getWorld())) {
					p.sendMessage(Lang.PRE + l.getString("insufficient_exp_level"));
					return;
				}

				p.setLevel(p.getLevel() - getUpgradeCost(p.getWorld()));
				p.sendTitle(Lang.PRE, l.getString("you_upgraded_wb"), 30, 40, 60);

				broadcastIncrement(p, p.getWorld());
				increase();

				c.set("Border." + p.getWorld().getName() + ".upgrade",
						c.getInt("Border." + p.getWorld().getName() + ".upgrade") + 1);

				c.set("Border." + p.getWorld().getName() + ".delay", true);
				ItemGenerator.getItemGenerator().saveConfig();

				Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {
					c.set("Border." + p.getWorld().getName() + ".delay", null);
					ItemGenerator.getItemGenerator().saveConfig();
				}, 4 * 20 + 1);

				p.closeInventory();
				break;
		}
	}

	public void broadcastIncrement(Player p, World w) {
		Lang.broadcastArg("wb_upgraded", p.getName(), getUpgradeCost(w) + "");
	}
}
