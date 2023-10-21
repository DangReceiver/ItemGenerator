package de.cruelambition.listener.essential;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import de.cruelambition.oo.*;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Recipe;

public class CM implements Listener {
	@EventHandler
	public void handle(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PC pc = new PC(p);
		pc.load(p);

		Sb.setDefaultScoreBoard(p);

		if (pc.getLanguageString() == null)
			pc.setLanguage(Lang.getServerLang());

		Lang l = new Lang(null);
		l.setPlayerInSettings(p, pc.getLanguage());
		l.setPlayer(p);

//		p.sendMessage(pc.getLanguage() + "");
//		p.sendMessage("§b" + l.getLang(p) + " || " + l.getLanguage());

		e.setJoinMessage(null);
		if (!p.hasPlayedBefore()) {

			Lang.broadcastArg("player_first_join", p.getName());
			p.teleport(SpawnWorld.getSafeSpawnLocation());

			if (!pc.hasCon(p)) pc.createCon(p);
			pc.savePCon();

		} else if (pc.getJoinTime() - pc.getQuitTime() <= 25000L)
			Lang.broadcastArg("player_rejoin_" + (new Random()).nextInt(4), p.getName());

		else Lang.broadcastArg("player_join_" + (new Random()).nextInt(17), p.getName());

		p.setGameMode(GameMode.ADVENTURE);
		p.teleport(SpawnWorld.getSafeSpawnLocation());

		pc.setJoinTime(System.currentTimeMillis());
		pc.savePCon();

		p.sendTitle(Lang.PRE, String.format(l.getString("welcome_back"), p.getName()), 30, 50, 50);
//		p.sendMessage(l.getLang(p) + " || " + l.getLanguage());

		for (Recipe re : ItemGenerator.rec) if (re instanceof Keyed k) p.discoverRecipe(k.getKey());
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

		if (p.getWorld() == Bukkit.getWorld("world")) pc.setLogoutLocation(p.getLocation());
		pc.savePCon();

		e.setQuitMessage(null);
		Lang.broadcastArg("player_quit_" + (new Random()).nextInt(17), p.getName());
	}
}