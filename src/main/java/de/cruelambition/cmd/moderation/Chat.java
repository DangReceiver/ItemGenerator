package de.cruelambition.cmd.moderation;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.IB;
import de.cruelambition.oo.PC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Chat implements CommandExecutor, Listener {

	public static String PERMISSION = "ItemGenerator.Chat.Customization",
			PERMISSION_OTHERS = "ItemGenerator.Chat.Customization.Others";

	//	@Override
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
			sen.sendMessage(Lang.PRE + String.format(l.getString("insufficient_permission"), PERMISSION));
			return false;
		}

		PC pc = new PC(p);
		Inventory i = Bukkit.createInventory(null, 5 * 9, de.cruelambition.listener.essential.
				Chat.replaceChat(pc, de.cruelambition.listener.essential.Chat.format, ""));

		IB.invFiller(i, IB.getFiller(pc.getFiller(), true, false, null, null));
		p.openInventory(i);

		i.setItem(3, IB.lore(IB.name(new ItemStack(Material.YELLOW_BANNER), l.getString(
				"chat_customization_link")), l.getString("chat_customization_link_lore").split("//")));
		i.setItem(5, IB.lore(IB.name(new ItemStack(Material.GREEN_BANNER), l.getString(
				"chat_customization_counter")), l.getString("chat_customization_counter_lore").split("//")));
		return false;
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		HumanEntity he = e.getWhoClicked();
		Inventory clickInv = e.getClickedInventory(),
				topInv = e.getInventory();
		ItemStack item = e.getCurrentItem();

		if (!(he instanceof Player p)) return;
		if (item == null) return;
		if (topInv != clickInv) return;

		PC pc = new PC(p);

		if (p.getOpenInventory().getTitle().equals(de.cruelambition.listener.essential.Chat.
				replaceChat(pc, de.cruelambition.listener.essential.Chat.format, ""))) e.setCancelled(true);
		else return;

		if (item.getType().toString().contains("BANNER")) {
			if (!IB.isEnch(item)) IB.ench(item, Enchantment.DURABILITY, 0);
			else IB.disEnch(item);

			if (topInv.getItem(e.getSlot() - 2) == null || topInv.getItem(e.getSlot() + 2) == null) return;
			if (topInv.getItem(e.getSlot() + 2).getType().toString().contains("BANNER")) {


			} else if (topInv.getItem(e.getSlot() - 2).getType().toString().contains("BANNER")) {


			}
		}
	}
}
