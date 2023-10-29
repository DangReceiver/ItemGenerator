package de.cruelambition.oo;

import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
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

	public WorldBorder() {
		wb = Bukkit.getWorld("world").getWorldBorder();
		wbn = Bukkit.getWorld("world_nether").getWorldBorder();

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

		asw = getSpawnUpgrader(wb.getWorld());
		asn = getSpawnUpgrader(wbn.getWorld());
	}

	public boolean spawnUpgraderExists(World w) {
		for (Entity en : w.getNearbyEntities(new Location(w, 0.5, 64, 0.5), 12d, 12d, 12d))

			if (en instanceof ArmorStand as && as.getCustomName().equalsIgnoreCase(
					l.getString("as_wb"))) return true;

		return true;
	}

	public ArmorStand getSpawnUpgrader(World w) {
		for (Entity en : w.getNearbyEntities(new Location(w, 0.5, 64, 0.5), 12d, 12d, 12d))
			if (en instanceof ArmorStand as && as.getCustomName().equalsIgnoreCase(l.getString("as_wb"))) return as;

		return null;
	}

	public void spawnUpgrader(World w) {
		ArmorStand as = (ArmorStand) w.spawnEntity(new Location(w, 7.5, 65.5, 7.5), EntityType.ARMOR_STAND);

		as.setMarker(true);
		as.setGravity(false);

		as.setCustomName(l.getString("as_wb"));
		as.setCustomNameVisible(true);

		as.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
		as.setHelmet(IB.ench(new ItemStack(Material.SKELETON_SKULL), Enchantment.BINDING_CURSE, 0));
	}

	public void setDefaultWb(World w) {
		org.bukkit.WorldBorder pwb = w.getWorldBorder();

		if (pwb == null) Bukkit.getConsoleSender().sendMessage(Lang.PRE
				+ new Lang(null).getString("wb_invalid_world"));
		else defaults(pwb);
	}

	public void defaults(org.bukkit.WorldBorder pWb) {
		pWb.setCenter(0.5, 0.5);
		pWb.setSize(pWb.getWorld().getName().contains("_nether") ? 20 * 5 : 20);
		pWb.setDamageAmount(0.25);
		pWb.setDamageBuffer(0.25);
		pWb.setWarningDistance(4);
		pWb.setWarningTime(2);
	}

	public void increase() {
		wb.setSize(wb.getSize() + 2, 4);
		wbn.setSize(wbn.getSize() + 10, 4);
	}

	public void syncWb() {
		wb.setSize(Math.max(wb.getSize(), wbn.getSize() / 5));
		wbn.setSize(Math.max(wb.getSize(), wbn.getSize() * 5));
	}

	@EventHandler
	public void handle(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof ArmorStand as)) return;
		if (!(e.getDamager() instanceof Player p)) return;

		if (!as.getCustomName().equalsIgnoreCase(l.getString("as_wb"))) return;
		p.sendMessage("World Border Upgrader");

		Lang lp = new Lang(p);

		Inventory inv = Bukkit.createInventory(null, 4 * 9, lp.getString("wb_upgrader_inv"));
		IB.invFiller(inv, IB.getFiller(new PC(p).getFiller(), true, true, null, null));

		inv.setItem(inv.getSize() / 2 - 2, new ItemStack(Material.RED_BANNER));
		inv.setItem(inv.getSize() / 2 + 2, new ItemStack(Material.LIME_BANNER));

		p.openInventory(inv);
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		if (!(e.getView() instanceof Player p)) return;

		Lang lp = new Lang(p);
		if (!p.getOpenInventory().getTitle().equalsIgnoreCase(lp.getString("wb_upgrader_inv"))) return;

		ItemStack is = e.getCurrentItem();
		if (is == null) return;

		e.setCancelled(true);
		switch (is.getType()) {

			default:
				break;
			case RED_BANNER:
				p.sendMessage("cancel");
				p.closeInventory();
				break;
			case LIME_BANNER:
				p.sendMessage("proceed");
				broadcastIncrement();
				p.closeInventory();
				break;
		}

		p.sendMessage("click");
	}

	public void broadcastIncrement() {

	}
}
