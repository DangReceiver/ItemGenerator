package de.cruelambition.listener.essential;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.Nullable;

public class Chat implements Listener {

	public static String format = "<prefix> §7‖ <counter_link><player_color><player_name><link> <message_color><message>";

	@EventHandler
	public void handle(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PC pc = new PC(p);

		String f = format;
		List<Integer> dmc = pc.getDefaultMessageColor();

		ChatColor cc = Lang.colorFromRGB(dmc.get(0), dmc.get(1), dmc.get(2));
		String msg = e.getMessage();
		e.setFormat(f = replaceChat(pc, f, null));

		if (p.hasPermission("Savior.Chat.Color")) f = f.replaceAll("&", "§");

		if (p.hasPermission("Savior.Chat.Tagging"))
			for (Player ap : Bukkit.getOnlinePlayers()) {

				if (!msg.contains(ap.getName())) continue;
				msg = msg.replaceAll(ap.getName(), "§" + Lang.colorFromRGB(100, 220, 40) + "§o@" +
						ap.getName() + cc);

				ap.playSound(ap.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.6F, 1.1F);
			}

		e.setFormat(f.replaceAll("<message>", msg));
	}

	public static String replaceChat(PC pc, String f, @Nullable String msg) {
		List<Integer> dmc = pc.getDefaultMessageColor();

		Player p = pc.thisPlayer();
		ChatColor cc = Lang.colorFromRGB(dmc.get(0), dmc.get(1), dmc.get(2));

		f = f.replaceAll("<prefix>", Lang.CHAT);
		f = f.replaceAll("<player_name>", p.getName()).replaceAll("<player_color>", pc.getPlayerColor());

		f = f.replaceAll("<counter_link>", pc.getCounterLink()).replaceAll("<link>", pc.getLink());
		f = f.replaceAll("<message_color>", "" + cc);

		if (msg != null) f = f.replaceAll("<message>", msg);
		return f;
	}

}