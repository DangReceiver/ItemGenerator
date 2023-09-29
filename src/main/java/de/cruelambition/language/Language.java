package de.cruelambition.language;

import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.exceptions.InvalidStringException;

import java.io.File;
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

			if (!langFiles.contains(c.getString("Language") + ".yml")) return;
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

		} else
			Bukkit.getConsoleSender().sendMessage(Lang.getMessage(getServerLang(), "target_invalid"));
	}

	public static boolean isLangFile(String lang) {
		return (new File(df + "/languages", lang + ".yml")).exists();
	}

	public static File getLangFile(String lang) {
		File file = new File(df + "/languages", lang + ".yml");

		if (!file.exists() || !file.exists()) throw new RuntimeException("The resulting lang file does not exist!");
		return file;
	}

	public void removePlayer(Player p) {
		settings.remove(p);
	}

	public static File getLangFolder() {
		return new File(df + "/languages");
	}

	public static void loadResources() {
		ItemGenerator ig = ItemGenerator.getItemGenerator();
		List<File> resources = new ArrayList<>(List.of(getLangFile("en"), getLangFile("de")));

		for (File f : resources) {
			if (f.exists()) continue;

			ConsoleCommandSender cs = Bukkit.getConsoleSender();
			String s = f.getName();

			InputStream in = ig.getResource(s);
			if (in == null) {

				Bukkit.getScheduler().runTaskLaterAsynchronously(ig, () -> cs.sendMessage(getMessage(
						getServerLang(), "error") + getMessage(getServerLang(), "input_stream_invalid")), 0L);
				return;
			}

			try {
				Files.copy(in, new File(getLangFolder(), s).toPath());

				if (f.mkdir()) Bukkit.getScheduler().runTaskLaterAsynchronously(ig, () -> cs.sendMessage(
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
		loadLanguages(langF);
	}

	public static String getMessage(File lang, String key) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();
		if (lang == null || !lang.exists() || key == null) {

			cs.sendMessage("The chosen language is invalid, proceeding on with the server language");
			lang = getServerLang();
		}

//		if (messages.get(lang) == null)
//			throw new RuntimeException(String.format("The String '%s' could not be determined! [0]", lang.getName()));
//		if (messages.get(lang).get(key) == null)
//			throw new RuntimeException(String.format("The String key '%s' is not listed! [1]", key));

		String see = isValid(lang, key) ?
				messages.get(lang).get(key) :

				(isValid(getServerLang(), key) ?
						(messages.get(getServerLang())).get(key) :

						(isValid(lang, "string_not_found") ?
								String.format((messages.get(lang)).get("string_not_found"), key) :
								invalidString(key, lang)));

		if (see == null) throw new RuntimeException("The resulting String does not exist! [2]");
		return see;
	}

	@Deprecated
	public static String getMessageUnverified(File lang, String key) {
		return isValidUnlisted(lang, key) ? (messages.get(lang)).get(key) : (isValidUnlisted(getServerLang(), key) ?

				(messages.get(getServerLang())).get(key) : (isValidUnlisted(lang, "string_not_found") ?
				String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
	}

	public static boolean isValid(@NotNull File lang, @NotNull String key) {
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		if (messages.get(lang) == null ||
				messages.get(lang).get(key) == null) {

			if (missingKeys.isEmpty() || !missingKeys.contains(key))
				missingKeys.add(key.toString() + "; " + lang.getName().split(".yml")[0].toString());

			cs.sendMessage(String.format("§bThe key %s does not exist in the language file %s.", key, lang));
			if (lang.toString().contains("\\")) throw new RuntimeException("§4language Path Is Forbidden!");
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
					cs.sendMessage(Lang.colorFromRGB(210, 210, 210) +
							key + "§8/§7" + messName + " §8» §aWas loaded");

					if (sLang.getString(key + "." + messName) == null) continue;
					messages.put(getServerLang(), lm);

					if (getServerLang().exists()) continue;
					Bukkit.getConsoleSender().sendMessage(String.format(Lang.PRE +
							"§cLanguage %s could not be loaded", getServerLang().getName()));
				}
			printAllMessages(getServerLang());
		}

		for (File file : files) {
			if (file == getServerLang()) continue;

			FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
			cs.sendMessage(String.format("§7Loading §b%s §7language keys§8...", file.getName().split(".yml")[0]));

			for (String key : lang.getKeys(false))
				for (String messName : lang.getConfigurationSection(key).getKeys(false)) {

					cs.sendMessage(key + "§8/§7" + messName + " §8» §aWas loaded");
					if (lang.getString(key + "." + messName) == null) continue;

					messages.put(file, lm);
					if (file.exists()) continue;

					Bukkit.getConsoleSender().sendMessage(String.format(Lang.PRE +
							"§cLanguage %s could not be loaded", file.getName()));
				}
			printAllMessages(file);
		}
	}

	public static void printAllMessages(File file) {
		YamlConfiguration ymlc = YamlConfiguration.loadConfiguration(file);
		ConsoleCommandSender cs = Bukkit.getConsoleSender();

		Map<String, String> lm = new HashMap<>();
		Lang l = new Lang(null);
		l.setLocalLanguage(Lang.getLangFile(file.getName().split("/")[0].split(".yml")[0]));

		for (String key : ymlc.getKeys(false)) {
			for (String messName : ymlc.getConfigurationSection(key).getKeys(true)) {

				if (ymlc.getString(key + "." + messName) != null) {
					String message = ChatColor.translateAlternateColorCodes('§',
							ymlc.getString(key + "." + messName));

					lm.put(messName, message);
				}
				cs.sendMessage("§9" + key + "." + messName + ": " + l.getString(key + "." + messName));
			}
		}
	}
}