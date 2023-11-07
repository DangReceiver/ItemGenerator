package de.cruelambition.cmd.user;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.IB;
import de.cruelambition.oo.PC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Backpack implements CommandExecutor, Listener {
	public static String PERMISSION = "ItemGenerator.Backpack";

	@Override
	public boolean onCommand(CommandSender sen, Command cmd, String lab, String[] args) {
		Player p;
		Lang l = new Lang(null);

		if (sen instanceof Player) {
			p = (Player) sen;
			l.setPlayer(p);

		} else {
			sen.sendMessage(Lang.PRE + Lang.getMessage(null, "not_a_player"));
			return false;
		}

		if (!p.hasPermission(PERMISSION)) {
			p.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		PC pc = new PC(p);
		int bps = pc.getBackpackSize();

		if (args.length == 1) {

			if (!(args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("unlock"))) {
				p.sendMessage(Lang.PRE + l.getString("invalid_argument"));
				return false;
			}

			Inventory inv = Bukkit.createInventory(null, 3 * 9, l.getString("backpack_upgrade_inv"));

			IB.invFiller(inv, IB.getFiller(pc.getFiller(), true, false, null, null));
			inv.setItem((inv.getSize() / 2 + 2), IB.lore(IB.name(new ItemStack(Material.RED_TERRACOTTA),
					l.getString("cancel_purchase")), l.getString("cancel_purchase_lore")));
			inv.setItem((inv.getSize() / 2 + 2), IB.lore(IB.name(new ItemStack(Material.PAPER),
					l.getString("purchase_info")), String.format(l.getString("purchase_info"),
					(8 * (bps + 1) * bps + 32))));

			p.openInventory(inv);
			Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(), () -> {

				inv.setItem((inv.getSize() / 2 - 2), IB.lore(IB.name(new ItemStack(Material.LIME_TERRACOTTA),
						l.getString("confirm_purchase")), String.format(l.getString("backpack_upgrade"),
						(8 * (bps + 1) * bps + 32))));

				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.5f, 1.2f);
			}, 35);

			return false;
		} else if (args.length != 0) {
			p.sendMessage(Lang.PRE + String.format(l.getString("argument_range"), 0, 1));
			return false;
		}

		if (bps == 0) {
			p.sendMessage(Lang.PRE + l.getString("backpack_not_unlocked"));
			return false;
		}

		p.openInventory(pc.getBackpack());
		p.sendMessage(Lang.PRE + l.getString("backpack_opened"));
		return false;
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		HumanEntity he = e.getWhoClicked();
		if (he instanceof Player p) {

			Lang l = new Lang(p);
			if (e.getClickedInventory() != p.getOpenInventory().getTopInventory()) return;
			if (!p.getOpenInventory().getTitle().equals(l.getString("backpack_upgrade_inv"))) return;

			e.setCancelled(true);

			if (!(e.getCurrentItem().getType().toString().contains("LIME") ||
					e.getCurrentItem().getType().toString().contains("RED"))) return;

			if (e.getCurrentItem().getType().toString().contains("LIME")) {
				PC pc = new PC(p);

				int bps = pc.getBackpackSize();
				int cost = (8 * (bps + 1) * bps + 32);

				if (IB.getMaterialAmount(Material.EMERALD, p.getInventory()) < cost) {
					p.sendMessage(Lang.PRE + l.getString("insufficient_funds"));
					return;
				}

				IB.removeItems(Material.EMERALD, cost, p.getInventory());
				pc.increaseBackpackSize();

				pc.savePCon();
				p.closeInventory();

				p.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_5, 0.5f, 1.15f);
				p.sendMessage(Lang.PRE + l.getString("backpack_upgrade_purchased"));

			} else if (e.getCurrentItem().getType().toString().contains("RED")) {
				p.closeInventory();

				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 0.8f);
				p.sendMessage(Lang.PRE + l.getString("purchase_cancelled"));
			}
		}
	}

	@EventHandler
	public void handle(InventoryCloseEvent e) {
		HumanEntity he = e.getPlayer();
		if (he instanceof Player p) {

			Lang l = new Lang(p);
			if (!p.getOpenInventory().getTitle().equals(l.getString("backpack_inventory"))) return;

			PC pc = new PC(p);
			Inventory ti = p.getOpenInventory().getTopInventory();

			pc.setBackpack(ti);
			pc.savePCon();

			p.sendActionBar(Lang.PRE + l.getString("backpack_saved"));
		}
	}
}
