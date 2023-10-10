package de.cruelambition.language;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.exceptions.InvalidStringException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Language {
	private static File sLang;
	private final Map<Player, File> settings = new HashMap<>();
	private static File df;
	private static final Map<File, Map<String, String>> messages = new HashMap<>();

	public static List<String> missingKeys;

	public Language() {
		df = ItemGenerator.getItemGenerator().getDataFolder();
		missingKeys = new ArrayList<>();

		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();
		sLang = getLangFile(c.isSet("Lang") ? c.getString("Lang") : setDefaultLang());
	}

	public String setDefaultLang() {
		FileConfiguration c = ItemGenerator.getItemGenerator().getConfig();

		c.set("Lang", "en");
		ItemGenerator.getItemGenerator().saveConfig();

		return c.getString("Lang", "en");
	}

	public void setPlayerInSettings(Player p, File f) {
		settings.put(p, f);
	}

	public File getLang(Player p) {
//		ConsoleCommandSender cs = Bukkit.getConsoleSender();
//		cs.sendMessage("§6" + settings.get(p));
//		cs.sendMessage("§c" + ((!settings.isEmpty() && settings.get(p) != null) ? settings.get(p) : getServerLang()));

		return (!settings.isEmpty() && settings.get(p) != null) ? settings.get(p) : getServerLang();
	}

	public static File getServerLang() {
		if (sLang == null || !sLang.exists())
			throw new RuntimeException("§6Server Language invalid or does not exist!");
		return sLang;
	}

	public List<String> getLanguages() {
		List<String> langFiles = new ArrayList<>();
		File folder = new File(df + "/languages");

		if (folder.listFiles() == null) throw new RuntimeException("§cLanguage folder is empty!!");
		for (File file : folder.listFiles()) langFiles.add(file.getName());

		return langFiles;
	}

	public void setServerLanguage(File serverLang) {
		ItemGenerator itemGenerator = ItemGenerator.getItemGenerator();
		sLang = serverLang;

		FileConfiguration c = itemGenerator.getConfig();
		List<String> langFiles = getLanguages();

		if (serverLang == null) {
			if (!c.isSet("Language")) {

				c.set("Language", "en");
				itemGenerator.saveConfig();
			}

			if (!langFiles.contains(c.getString("Language") + ".yml")) return;
			sLang = new File(df + "/languages", c.getString("Language") + ".yml");

		} else if (langFiles.contains(serverLang.getName()))
			sLang = serverLang;
	}

	public void setPlayerLang(Player p, File file) {
		if (p == null) return;
		removePlayer(p);

		Bukkit.getConsoleSender().sendMessage("§5settings: §b" + p.getName() + " §e|| " + file.getName());

		if (!file.exists()) settings.put(p, getServerLang());
		else settings.put(p, file);
	}

	public static boolean isLangFile(String lang) {
		return (new File(df + "/languages", lang + ".yml")).exists();
	}

	public static File getLangFile(String lang) {
		File file = new File(df + "/languages", lang + ".yml");

		try {
			if (!file.exists() && !file.createNewFile())
				throw new RuntimeException("The resulting lang file does not exist!");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return file;
	}

	public void removePlayer(Player p) {
		Bukkit.getConsoleSender().sendMessage("§4Removing " + p.getName());
		settings.remove(p);
	}

	public static File getLangFolder() {
		return new File(df + "/languages");
	}

	public static void loadResources() {
		ItemGenerator ig = ItemGenerator.getItemGenerator();
		List<File> resources = new ArrayList<>(List.of(getLangFile("en"), getLangFile("de"), getLangFile("ch")));

		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		for (File f : resources) {

			if (f.exists() && f.length() > 0) continue;
			String s = f.getName();

			InputStream in = ig.getResource(s);
			if (in == null) {

				Bukkit.getScheduler().runTaskLaterAsynchronously(ig, () -> cs.sendMessage(getMessage(
						getServerLang(), "error") + getMessage(getServerLang(), "input_stream_invalid")), 0L);
				return;
			}

			try {
				if (f.length() == 0) f.delete();
				Files.copy(in, new File(getLangFolder(), s).toPath());

				if (!f.exists()) Bukkit.getScheduler().runTaskLaterAsynchronously(ig, () -> cs.sendMessage(
						getMessage(getServerLang(), "error") + getMessage(getServerLang(),
								"cannot_create_language_file")), 0L);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void loadingSequence() {
		loadResources();

		if (getServerLang() == null) setServerLanguage(new File(getLangFolder(), "en.yml"));
		loadLanguages(getLangFolder());
	}

	public static String getMessage(File lang, String key) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		if (lang == null || !lang.exists() || key == null) {

			cs.sendMessage("The chosen language is invalid, carrying on with the server language");
			lang = getServerLang();
		}

		String see = isValid(lang, key) ? messages.get(lang).get(key) :
				(isValid(getServerLang(), key) ? messages.get(getServerLang()).get(key) :

						(isValid(lang, "string_not_found") ?
								String.format(messages.get(lang).get("string_not_found"), key) :
								invalidString(key, lang)));

		if (see == null) throw new RuntimeException("The resulting String does not exist! [0]");
		return see;
	}

//	@Deprecated
//	public static String getMessageUnverified(File lang, String key) {
//		return isValidUnlisted(lang, key) ? (messages.get(lang)).get(key) : (isValidUnlisted(getServerLang(), key) ?
//
//				(messages.get(getServerLang())).get(key) : (isValidUnlisted(lang, "string_not_found") ?
//				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
//	}

	public static boolean isValid(@NotNull File lang, @NotNull String key) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		if (lang.toString().contains("\\")) lang = new File(lang.getPath());

		if (messages.get(lang) == null ||
				messages.get(lang).get(key) == null) {

			if (missingKeys.isEmpty() || !missingKeys.contains(key))
				missingKeys.add(key.toString() + "; " + lang.getName().split(".yml")[0].toString());

			cs.sendMessage(String.format("§bThe key %s does not exist in the language file %s.", key, lang));
			return false;
		}
		return true;
	}

	public static boolean isValidUnlisted(File lang, String key) {
		return (messages.get(lang) != null && ((Map) messages.get(lang)).get(key) != null);
	}

	public static String invalidString(String key, File lang) {
		String s;
		try {

			return (messages != null && messages.get(getServerLang()) != null
					&& messages.get(getServerLang()).get(key) != null) ?

					messages.get(getServerLang()).get(key) :
					String.format("§e§oString '%s' in language file '%s' not loaded yet!",
							key, lang.getName().split(".yml")[0]);

		} catch (InvalidStringException ex) {
			Bukkit.getConsoleSender().sendMessage("§6" + ex.getMessage());
			s = "§6" + ex.getMessage();
		}
		return s;
	}

	public static void loadLanguages(File langF) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		File[] files = langF.listFiles();

		if (langF == null || files == null || files.length == 0) {
			cs.sendMessage("Server langauge file is empty! Nothing to load...");
			return;
		}

		Map<String, String> lm = new HashMap<>();
		List<File> list = new ArrayList<>(Arrays.stream(files).toList());

		if (list.contains(getServerLang())) {
			YamlConfiguration sLang = YamlConfiguration.loadConfiguration(getServerLang());

			list.remove(getServerLang());
			for (String key : sLang.getKeys(false))

				for (String messName : sLang.getConfigurationSection(key).getKeys(false)) {
					String message = ChatColor.translateAlternateColorCodes('§',
							sLang.getString(key + "." + messName));
//					cs.sendMessage(Lang.colorFromRGB(210, 210, 210) +
//							key + "§8/§7" + messName + " §8» §aWas loaded");

					if (sLang.getString(key + "." + messName) == null) continue;
					lm.put(messName, message);

				}
			if (!getServerLang().exists())
				Bukkit.getConsoleSender().sendMessage(String.format(Lang.PRE +
						"§cLanguage %s could not be loaded", getServerLang().getName()));

			messages.put(getServerLang(), lm);
//			printAllMessages(getServerLang());
		}

		for (File file : files) {
			if (file == getServerLang()) continue;

			FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
			cs.sendMessage(String.format("§7Loading §b%s §7language keys§8...", file.getName().split(".yml")[0]));

			for (String key : lang.getKeys(false))
				for (String messName : lang.getConfigurationSection(key).getKeys(false)) {

					String message = ChatColor.translateAlternateColorCodes('§',
							lang.getString(key + "." + messName));
					lm.put(messName, message);

					if (lang.getString(key + "." + messName) == null) continue;
//					cs.sendMessage(key + "§8/§7" + messName + " §8» §aWas loaded");
				}

			if (!file.exists()) {
				Bukkit.getConsoleSender().sendMessage(Lang.PRE + String.format(
						"§cLanguage %s could not be loaded", file.getName()));
				continue;
			}

			messages.put(file, lm);
			if (!isValid(file, "string_not_found")) throw
					new RuntimeException("Language file is missing essential Strings!");
//			printAllMessages(file);
		}
	}

	/*
	 			DEBUG START
	 */

	public static void printAllMessages(File file) {
		YamlConfiguration ymlc = YamlConfiguration.loadConfiguration(file);
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		Map<String, String> lm = new HashMap<>();
		Lang l = new Lang(null);
		l.setLocalLanguage(Lang.getLangFile(file.getName().split("/")[0].split(".yml")[0]));

		for (String key : ymlc.getKeys(false))
			for (String messName : ymlc.getConfigurationSection(key).getKeys(true)) {

				if (ymlc.getString(key + "." + messName) != null) {
					String message = ChatColor.translateAlternateColorCodes('§',
							ymlc.getString(key + "." + messName));

					lm.put(messName, message);
				}
				cs.sendMessage("§9" + key + "." + messName + ": " + l.getString(messName));
			}
	}

	public static void printMessages() {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		for (Map<String, String> val : messages.values()) {
			cs.sendMessage("§a" + val);
		}

		for (String val : messages.get(getServerLang()).values()) {
			cs.sendMessage("§2" + val);
		}
	}

	/*
	 			DEBUG END
	 */
}