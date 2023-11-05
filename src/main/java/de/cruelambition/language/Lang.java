package de.cruelambition.language;

import java.awt.Color;
import java.io.File;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Lang extends Language {

	public static String PRE = colorFromRGB(230, 60, 150) + "ItemGenerator§8: "
			+ colorFromRGB(180, 180, 180),
			CHAT = "§5IG§8: ";

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
		if (p == null) throw new RuntimeException("Player target invalid: Cannot set new language");

		lang.setPlayerLang(p, pLang);
		updateLf(pLang);
	}

	public Player thisPlayer() {
		return p;
	}

	public void setPlayer(Player player) {
		lf = lang.getLang((p = player));
	}

	public String getString(String key) {
		return translateCustomColor(Language.getMessage(lf, (last = key)));
	}

	public String translateCustomColor(String s) {
		if (!s.contains("$")) return s;
		int r = 0, g = 0, b = 0;

		try {
			r = Integer.parseInt(s.split("\\$")[1].split(",")[0]);
			g = Integer.parseInt(s.split("\\$" + r + ",")[1].split(",")[0]);

			b = Integer.parseInt(s.split("\\$" + r + "," + g + ",")[1].split("; ")[0]);
			s = s.replace("$" + r + "," + g + "," + b + "; ", "");

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return s.replace("$", colorFromRGB(r, g, b) + "");
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

		for (String s : replacements) temp = temp.replaceAll(toReplace, s);
		return temp;
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