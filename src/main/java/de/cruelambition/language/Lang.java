package de.cruelambition.language;

import java.awt.Color;
import java.io.File;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Lang extends Language {

	public static String PRE = colorFromRGB(195, 40, 155) + "ItemGenerator§8: "
			+ colorFromRGB(160, 160, 160);

	private File lf;
	private Player p;

	private String last;
	private final Language lang;

	public Lang(Player player) {
		lang = new Language();
		p = player;
		lf = lang.getLang(p != null ? p : null);
	}

	public static File getServerLang() {
		return Language.getServerLang();
	}

	public void setPlayerLanguage(File pLang) {
		if (p != null) lang.setPlayerLang(p, pLang);

		lf = pLang;
		lang.setPlayerLang(p, pLang);
	}

	public void setPlayer(Player player) {
		lf = lang.getLang((p = player));
	}

	public String getString(String key) {
		return Language.getMessage(lf, (last = key));
	}

	public void setLocalLanguage(File temp) {
		Language.getLangFile(temp.getName().split(".yml")[0]);
	}

	public void updateLf(File pF) {
		lf = pF;
	}

	public File getLanguage() {
		return lf;
	}

	public String replaceString(String key, String toReplace, String... replacements) {
		String temp = Language.getMessage(lf, (last = key));

		for (String s : replacements) {
			temp = temp.replaceAll(toReplace, s);
		}
		return temp;
	}

	public String replaceColor(String input, Color... color) {
		for (Color c : color)
			input = input.replace("§c", "" +
					colorFromRGB(c.getRed(), c.getGreen(), c.getBlue()));
		return input;
	}

	public String formatString(String key, String... replacements) {
		return String.format(Language.getMessage(lf, last = key), (Object[]) replacements);
	}


	public static ChatColor colorFromRGB(int r, int g, int b) {
		return ChatColor.of(new Color(r, g, b));
	}

	public String getLast() {
		return last;
	}


	public static ChatColor getColor(int r, int g, int b) {
		return ChatColor.of("#" + r + g + b);
	}

	public static void broadcast(String key) {
		//	PRONOUNS: getMessage(getServerLang(), "info")
		for (Player ap : Bukkit.getOnlinePlayers())
			ap.sendMessage(PRE + getMessage((new Lang(ap)).getLang(ap), key));
	}

	public static void broadcastArg(String key, String... arg) {
		for (Player ap : Bukkit.getOnlinePlayers()) {
			String format = String.format(getMessage(new Lang(ap).getLang(ap), key), (Object[]) arg);
			ap.sendMessage(PRE + format);
		}
	}

	public void printMissingKeys() {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		cs.sendMessage("§7Known missing keys§8:");

		for (String key : missingKeys) cs.sendMessage("§7" + key.replace("; ", "§8« §6"));
	}

	public List<String> getMissingKeys() {
		List<String> mkl = missingKeys;

		int i = 0;
		for (String s : mkl)
			if (!s.contains("; " + lf.getName().split(".yml")[0])) {

				mkl.remove(i);
				i++;
			}

		return mkl;
	}
}