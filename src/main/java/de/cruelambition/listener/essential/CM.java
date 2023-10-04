package de.cruelambition.listener.essential;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CM implements Listener {
	@EventHandler
	public void handle(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PC pc = new PC(p);
		pc.load(p);

		if (pc.getLanguageString() == null)
			pc.setLanguage(Lang.getServerLang());

//		ConsoleCommandSender cs = Bukkit.getConsoleSender();
//		cs.sendMessage("0");

		Lang l = new Lang(null);
//		cs.sendMessage("1");
		l.setPlayerInSettings(p, pc.getLanguage());
//		cs.sendMessage("2");
		l.setPlayer(p);
//		cs.sendMessage("3");

//		p.sendMessage(pc.getLanguage() + "");
//		cs.sendMessage("4");
//		p.sendMessage("§b" + l.getLang(p) + " || " + l.getLanguage());
//		cs.sendMessage("5");

		e.setJoinMessage(null);
		if (!p.hasPlayedBefore()) {

			Lang.broadcastArg("player_first_join", p.getName());
			p.teleport(SpawnWorld.getSafeSpawnLocation());

//			cs.sendMessage("6");
			if (!pc.hasCon(p)) pc.createCon(p);
			pc.savePCon();

		} else if (pc.getJoinTime() - pc.getQuitTime() <= 25000L)
			Lang.broadcastArg("player_rejoin_" + (new Random()).nextInt(4), p.getName());

		else Lang.broadcastArg("player_join_" + (new Random()).nextInt(17), p.getName());

//		cs.sendMessage("7");
		pc.setJoinTime(System.currentTimeMillis());
		pc.savePCon();

//		cs.sendMessage("8");
		p.sendTitle(Lang.PRE, String.format(l.getString("welcome_back"), p.getName()), 30, 50, 50);
//		p.sendMessage(l.getLang(p) + " || " + l.getLanguage());
//		cs.sendMessage("9");
	}

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PC pc = new PC(p);

//		Lang l = new Lang(p);
//		File lf = pc.getLanguage() != null ? pc.getLanguage() : null;
//		l.setPlayerLanguage(lf);

		pc.setQuitTime(System.currentTimeMillis());
		pc.updatePlayTime();
		pc.savePCon();

		e.setQuitMessage(null);
		Lang.broadcastArg("player_quit_" + (new Random()).nextInt(17), p.getName());
	}
}