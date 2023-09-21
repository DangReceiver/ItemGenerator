package de.cruelambition.language;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.exceptions.InvalidStringException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Language {
	private static File sLang;
	private final Map<Player, File> settings = new HashMap<>();
	private static File df;
	private final String lp;
	private static final Map<File, Map<String, String>> messages = new HashMap<>();

	public static List<String> missingKeys;

	public Language() {
		df = ItemGenerator.getItemGenerator().getDataFolder();
		lp = df + "/languages";

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

	public void setSLang(File serverLang) {
		setServerLanguage(sLang = serverLang);
	}

	public File getDf() {
		return df;
	}

	public static String invalidString(String key) {
		try {
			String s = (messages != null && messages.get(getServerLang()) != null) ? (String) ((Map)
					messages.get(getServerLang())).get(key) : "§oString not loaded yet!";

			s = s.split("a")[1];
		} catch (InvalidStringException ex) {
			ex.printStackTrace();
		}

		return (messages != null && messages.get(getServerLang()) != null) ?
				String.format((String) ((Map) messages.get(getServerLang())).get("string_not_found"),
						key) : "§oString not loaded yet!";
	}

	public File getLang(Player p) {
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

	public void setServerLanguage(File language) {
		ItemGenerator itemGenerator = ItemGenerator.getItemGenerator();
		FileConfiguration c = itemGenerator.getConfig();
		List<String> langFiles = getLanguages();

		if (language == null) {
			if (!c.isSet("Language")) {
				c.set("Language", "en");
				itemGenerator.saveConfig();
			}

			if (!langFiles.contains(c.getString("Language") + ".yml"))
				return;
			sLang = new File(df + "/languages", c.getString("Language") + ".yml");
		} else if (langFiles.contains(language.getName()))
			sLang = language;
	}

	public void setPlayerLang(Player p, File file) {
		if (p != null) {
			removePlayer(p);
			settings.put(p, getLangFile("en"));

			if (!file.exists()) return;
			settings.put(p, file);
		} else {
			Bukkit.getConsoleSender().sendMessage(Lang.getMessage(getServerLang(), "target_invalid"));
		}
	}

	public static boolean isLangFile(String lang) {
		return (new File(df + "/languages", lang + ".yml")).exists();
	}

	public static File getLangFile(String lang) {
		File file = new File(df + "/languages", lang + ".yml");
		if (file == null || !file.exists()) throw new RuntimeException("The resulting lang file does not exist!");
		return file;
	}

	public void removePlayer(Player p) {
		settings.remove(p);
	}

	public static File getLangFolder() {
		return new File(df + "/languages");
	}

	public static void loadResources() {
		ItemGenerator itemGenerator = ItemGenerator.getItemGenerator();
		List<File> resources = new ArrayList<>(List.of(getLangFile("en"), getLangFile("de")));

		for (File f : resources) {
			if (f.exists()) continue;

			ConsoleCommandSender cs = Bukkit.getConsoleSender();
			String s = f.getName();
			InputStream in = itemGenerator.getResource(s);

			if (in == null) {
				Bukkit.getScheduler().runTaskLaterAsynchronously(itemGenerator, () -> cs.sendMessage(getMessage(
						getServerLang(), "error") + getMessage(getServerLang(), "input_stream_invalid")), 0L);

				return;
			}

			try {
				Files.copy(in, new File(getLangFolder(), s).toPath());

				if (f.mkdir()) Bukkit.getScheduler().runTaskLaterAsynchronously(itemGenerator, () -> cs.sendMessage(
						getMessage(getServerLang(), "error") + getMessage(getServerLang(),
								"cannot_create_language_file")), 0L);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static File getCache() {
		return new File(df + "/cache");
	}

	public void loadingSequence() {
		File langF = getLangFolder();

		loadResources();
		if (getServerLang() == null) setSLang(new File(langF, "en.yml"));
		loadCustomLanguages(langF);
	}

	public static String getMessage(File lang, String key) {
		if (lang == null || key == null) lang = getServerLang();
		if (messages.get(lang) == null || messages.get(lang).get(key) == null)
			throw new RuntimeException("The resulting String does not exist!");
		
		String see = isValid(lang, key) ? (messages.get(lang)).get(key) : (isValid(getServerLang(), key) ?
				(messages.get(getServerLang())).get(key) : (isValid(lang, "string_not_found") ?
				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));

		Bukkit.getConsoleSender().sendMessage("" + isValid(lang, key));
//		Bukkit.getConsoleSender().sendMessage(isValid(getServerLang(), key) ?
//				(messages.get(getServerLang())).get(key) : (isValid(lang, "string_not_found") ?
//				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
		Bukkit.getConsoleSender().sendMessage("" + isValid(getServerLang(), key));
		Bukkit.getConsoleSender().sendMessage(isValid(lang, "string_not_found") ?
				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang));
		Bukkit.getConsoleSender().sendMessage("" + isValid(lang, "string_not_found"));
		Bukkit.getConsoleSender().sendMessage(invalidString(key, lang));


		if (see == null) throw new RuntimeException("The resulting String does not exist!");
		return see;
	}

	@Deprecated
	public static String getMessageUnverified(File lang, String key) {
		return isValidUnlisted(lang, key) ? (messages.get(lang)).get(key) : (isValidUnlisted(getServerLang(), key) ?
				(messages.get(getServerLang())).get(key) : (isValidUnlisted(lang, "string_not_found") ?
				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
	}

	public static boolean isValid(File lang, String key) {
		if (messages.get(lang) == null || messages.get(lang).get(key) == null) return false;
		if (!missingKeys.contains(key)) {
			missingKeys.add(key + ";" + lang.getName().split(".yml")[0]);
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
			return (messages != null && messages.get(getServerLang()) != null) ?
					messages.get(getServerLang()).get(key) : String.format(
					"§7§oString '%s' in language file '%s' not loaded yet!§7", key,
					lang.getName().split(".yml")[0]);

		} catch (InvalidStringException ex) {
			Bukkit.getConsoleSender().sendMessage("§c" + ex.getMessage());
			s = "§c" + ex.getMessage();
		}
		return s;
	}

	public static void loadCustomLanguages(File langF) {
		if (langF == null || langF.listFiles() == null || (langF.listFiles()).length == 0) {
			Bukkit.getConsoleSender().sendMessage("Resources seem not to be loaded yet! Stopping process");
			return;
		}

		if (langF.listFiles() == null) return;
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		Lang l = new Lang(null);

		for (File file : langF.listFiles()) {
			Map<String, String> lm = new HashMap<>();
			FileConfiguration lang = YamlConfiguration.loadConfiguration(file);

			for (String key : lang.getKeys(false))
				for (String messName : lang.getConfigurationSection(key).getKeys(false)) {
//					cs.sendMessage(key + "§8/§7" + messName + " §8» §aWas loaded");

					if (lang.getString(key + "." + messName) != null) {
						messages.put(file, lm);

						if (!file.exists()) Bukkit.getConsoleSender().sendMessage(String.format(
								Lang.PRE + "§cLanguage %s could not be loaded", file.getName()));
					}
				}

//			cs.sendMessage(getServerLang() + "");
//			cs.sendMessage(Lang.getMessage(getLangFile("en"), "welcome_back"));
//			cs.sendMessage(Lang.getMessage(getServerLang(), "language_loaded"));
//			cs.sendMessage(file.getName().split("\\.")[0]);
//
//			cs.sendMessage(Lang.PRE +
//					String.format(
//							Lang.getMessage(getServerLang(), "language_loaded"), file.getName().split("\\.")[0]
//			)
//			);
		}
	}

	public static void printAllMessages(File file) {
		YamlConfiguration ymlc = YamlConfiguration.loadConfiguration(file);

		Map<String, String> lm = new HashMap<>();
		Lang l = new Lang(null);
		l.setLocalLanguage(file);

		for (String key : ymlc.getKeys(false)) {
			for (String messName : ymlc.getConfigurationSection(key).getKeys(false)) {

				if (ymlc.getString(key + "." + messName) != null) {

					String message = ChatColor.translateAlternateColorCodes('§',
							ymlc.getString(key + "." + messName));
					lm.put(messName, message);
				}

				System.out.println(key + "." + messName);


//				for (Player ap : Bukkit.getOnlinePlayers())
				System.out.println(key + "." + messName + ": " + l.getString(key + "." + messName));
			}
		}
	}
}