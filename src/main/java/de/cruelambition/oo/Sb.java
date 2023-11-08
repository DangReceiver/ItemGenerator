package de.cruelambition.oo;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Sb {

	private static int timeEntry = 0, worldEntry = 0, deathEntry = 0, killEntry = 0;

	public static final int TIME_SLOT = 16, WORLD_SLOT = 13, DEATH_SLOT = 10, KILL_SLOT = 7;

	private static BukkitTask bt;
	private static final int MAX_IDLE = Bukkit.getIdleTimeout();


	public static void setAllScoreBoards() {
		for (Player ap : Bukkit.getOnlinePlayers()) setDefaultScoreBoard(new Lang(ap));
	}

	public static void scoreboardLoop() {
		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {

			if (timeEntry >= 1) timeEntry = -1;
			timeEntry++;

			if (worldEntry >= 1) worldEntry = -1;
			worldEntry++;

			if (deathEntry >= 1) deathEntry = -1;
			deathEntry++;

			if (killEntry >= 1) killEntry = -1;
			killEntry++;

			for (Player ap : Bukkit.getOnlinePlayers()) updateFullScoreBoard(new Lang(ap));
			if (worldEntry == 1) startNbEntities();
			else cancelNbEntities();
		}, 8 * 20, 8 * 20);
	}

	public static void startNbEntities() {
		bt = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			for (Player ap : Bukkit.getOnlinePlayers()) Sb.updateWorldSlot(new Lang(ap));
		}, 30, 30);
	}

	public static void cancelNbEntities() {
		bt.cancel();
	}

	public static void timeLoop() {
		Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			for (Player ap : Bukkit.getOnlinePlayers()) Sb.updateTimeSlot(new Lang(ap));
		}, 8 * 20, 20);
	}

	public static void setDefaultScoreBoard(Lang l) {
		String chat = Lang.CHAT;

		if (chat.length() >= 17) chat = "§cInvalid String";
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

		Objective obj = sb.registerNewObjective("  " + chat, "abc", chat + "  ");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		Score space0 = obj.getScore("§0 "),
				space1 = obj.getScore(" §0"),
				space2 = obj.getScore(" §0 "),
				space3 = obj.getScore(" §0  "),
				space4 = obj.getScore("  §0 ");

		space0.setScore(17);
		space1.setScore(14);
		space2.setScore(11);
		space3.setScore(8);
		space4.setScore(5);

		Team dTime = sb.registerNewTeam("dTime"),
				dWorld = sb.registerNewTeam("dWorld"),
				dDeaths = sb.registerNewTeam("dDeaths"),
				dKills = sb.registerNewTeam("dKills"),

				dTimeDis = sb.registerNewTeam("dTimeDis"),
				dWorldDis = sb.registerNewTeam("dWorldDis"),
				dDeathsDis = sb.registerNewTeam("dDeathsDis"),
				dKillsDis = sb.registerNewTeam("dKillsDis");

		dTime.addEntry("" + ChatColor.AQUA);
		dWorld.addEntry("" + ChatColor.BLACK);
		dDeaths.addEntry("" + ChatColor.BLUE);
		dKills.addEntry("" + ChatColor.GREEN);

		dTimeDis.addEntry("" + ChatColor.DARK_AQUA);
		dWorldDis.addEntry("" + ChatColor.DARK_GRAY);
		dDeathsDis.addEntry("" + ChatColor.DARK_BLUE);
		dKillsDis.addEntry("" + ChatColor.DARK_GREEN);

		PC pc = new PC(l.thisPlayer());

		String ti = l.getString("sb_time_" + timeEntry).length() <= 16 ?
				l.getString("sb_time_" + timeEntry) : "§8-",
				wo = l.getString("sb_world_" + worldEntry).length() <= 16 ?
						l.getString("sb_world_" + worldEntry) : "§8-",

				de = l.getString("sb_deaths_" + deathEntry).length() <= 16 ?
						l.getString("sb_deaths_" + deathEntry) : "§8-",
				ki = l.getString("sb_kills_" + killEntry).length() <= 16 ?
						l.getString("sb_kills_" + killEntry) : "§8-";

		dTimeDis.setPrefix("  §8» " + ti + " ⌚");
		dWorldDis.setPrefix("  §8» " + wo + " \uD83C\uDF0F");
		dDeathsDis.setPrefix("  §8» " + de + " \uD83D\uDD48");
		dKillsDis.setPrefix("  §8» " + ki + " ⚔");

		dTime.setPrefix("    §e➥ ☞ " + String.format("%s", convertTime(System.currentTimeMillis())));
		dWorld.setPrefix("    §b➥ ۩ " + l.thisPlayer().getWorld().getName());
		dDeaths.setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
		dKills.setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));

		obj.getScore("" + ChatColor.AQUA).setScore(TIME_SLOT - 1);
		obj.getScore("" + ChatColor.BLACK).setScore(WORLD_SLOT - 1);
		obj.getScore("" + ChatColor.BLUE).setScore(DEATH_SLOT - 1);
		obj.getScore("" + ChatColor.GREEN).setScore(KILL_SLOT - 1);

		obj.getScore("" + ChatColor.DARK_AQUA).setScore(TIME_SLOT);
		obj.getScore("" + ChatColor.DARK_GRAY).setScore(WORLD_SLOT);
		obj.getScore("" + ChatColor.DARK_BLUE).setScore(DEATH_SLOT);
		obj.getScore("" + ChatColor.DARK_GREEN).setScore(KILL_SLOT);

		l.thisPlayer().setScoreboard(sb);
//		updateTime(p);
	}

	public static void unsetAllScoreboards() {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective(Lang.PRE.replace(": ", ""),
				"abc", Lang.PRE.replace(": ", "") + "      ");

		for (Player ap : Bukkit.getOnlinePlayers()) ap.setScoreboard(sb);
	}

	public static void unsetScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		sb.registerNewObjective(" ", "abc", " ");
		p.setScoreboard(sb);
	}

	public static void updateFullScoreBoard(Lang l) {
		updateTimeSlot(l);
		updateWorldSlot(l);
		updateKillSlot(l);
		updateDeathSlot(l);
	}

	public static void updateTimeSlot(Lang l) {
		if (timeEntry == 0) updateTime(l);
		else if (timeEntry == 1) updateWorldTime(l);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateTimeHeader(l);
	}

	public static void updateTimeHeader(Lang l) {
		l.thisPlayer().getScoreboard().getTeam("dTimeDis").setPrefix(
				l.getString("sb_time_" + timeEntry));
	}

	public static void updateTime(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s",
				convertTime(System.currentTimeMillis())));
	}

	public static void updateWorldTime(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		sb.getTeam("dTime").setPrefix("    §e➥ ☞ " + String.format("%s",
				convertTime(l.thisPlayer().getWorld().getFullTime())));
	}

	public static void updateWorldSlot(Lang l) {
		if (worldEntry == 0) updateWorld(l);
		else if (worldEntry == 1) updateNearbyEntities(l);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateWorldHeader(l);
	}

	public static void updateWorldHeader(Lang l) {
		l.thisPlayer().getScoreboard().getTeam("dWorldDis").setPrefix(
				l.getString("sb_world_" + timeEntry));
	}

	public static void updateWorld(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ ۩ " + l.thisPlayer().getWorld().getName());
	}

	public static void updateNearbyEntities(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		sb.getTeam("dWorld").setPrefix("    §b➥ ۩ " +
				l.thisPlayer().getLocation().getNearbyEntities(8, 8, 8).size());
	}

	public static void updateDeathSlot(Lang l) {
		if (deathEntry == 0) updateDeaths(l);
		else if (deathEntry == 1) updateXp(l);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateDeathHeader(l);
	}

	public static void updateDeathHeader(Lang l) {
		l.thisPlayer().getScoreboard().getTeam("dDeathsDis").setPrefix(
				l.getString("sb_deaths_" + deathEntry));
	}

	public static void updateDeaths(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();

		PC pc = new PC(l.thisPlayer());
		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + String.format("%s", pc.getDeaths()));
	}

	public static void updateXp(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		Duration id = l.thisPlayer().getIdleDuration();
		sb.getTeam("dDeaths").setPrefix("    §c➥ ⚔ " + id.getSeconds() + " / " + MAX_IDLE);
	}

	public static void updateKillSlot(Lang l) {
		if (killEntry == 0) updateKills(l);
		else if (killEntry == 1) updateKillLevel(l);
		else throw new RuntimeException(Lang.getMessage(Lang.getServerLang(), "invalid_time_slot"));

		updateKillHeader(l);
	}

	public static void updateKillHeader(Lang l) {
		l.thisPlayer().getScoreboard().getTeam("dKillsDis").setPrefix(l.getString("sb_kills_" + killEntry));
	}

	public static void updateKills(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();

		PC pc = new PC(l.thisPlayer());
		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + String.format("%s", pc.getKills()));
	}

	public static void updateKillLevel(Lang l) {
		Scoreboard sb = l.thisPlayer().getScoreboard();
		PC pc = new PC(l.thisPlayer());

		sb.getTeam("dKills").setPrefix("    §d➥ ❤ " + pc.getKillStatus() + " / " + pc.getKillLevelRequirement());
	}

	public static String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}
}