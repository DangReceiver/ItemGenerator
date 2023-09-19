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
        sLang = serverLang;
        setServerLanguage(serverLang);
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

        }
    }

    public static boolean isLangFile(String lang) {
        return (new File(df + "/languages", lang + ".yml")).exists();
    }

    public static File getLangFile(String lang) {
        File file = new File(df + "languages", lang + ".yml");
        if (file == null) throw new RuntimeException("The resulting lang file does not exist!");
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
        List<File> resources = new ArrayList<>(List.of(getLangFile("en.yml"), getLangFile("de.yml")));

        for (File f : resources) {
            Path target = f.toPath();
            String s = f.getName();

            InputStream in = itemGenerator.getResource(s);
            ConsoleCommandSender cs = Bukkit.getConsoleSender();

            if (in == null) {
                Bukkit.getScheduler().runTaskLaterAsynchronously(itemGenerator,
                        () -> cs.sendMessage(getMessage(getServerLang(), "error")
                                + getMessage(getServerLang(), "error")), 0L);

                return;
            }

            try {
                Files.copy(in, getCache().toPath());
                File tempFile = new File(String.valueOf(getCache().toPath()) + getCache().toPath());

                if (tempFile.length() <= getLangFile(s).length()) {
                    tempFile.delete();

                    continue;
                }

                Files.copy(in, target);
                Bukkit.getScheduler().runTaskLaterAsynchronously(itemGenerator,
                        () -> cs.sendMessage(getMessage(getServerLang(), "error")
                                + getMessage(getServerLang(), "error")), 0L);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static File getCache() {
        return new File(df + "/cache");
    }

    public void loadMessages() {
        File langF = getLangFolder();

        if (getServerLang() == null) setSLang(new File(langF, "en.yml"));
        loadCustomLanguages(langF);
    }

    public static String getMessage(File lang, String key) {
        if (lang == null) lang = getServerLang();
        return isValid(lang, key) ? (messages.get(lang)).get(key) : (isValid(getServerLang(), key) ?
                (messages.get(getServerLang())).get(key) : (isValid(lang, "string_not_found") ?
                String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
    }

    @Deprecated
    public static String getMessageUnverified(File lang, String key) {
        return isValidUnlisted(lang, key) ? (messages.get(lang)).get(key) : (isValidUnlisted(getServerLang(), key) ?
                (messages.get(getServerLang())).get(key) : (isValidUnlisted(lang, "string_not_found") ?
                String.format((messages.get(lang)).get("string_not_found"), key) : invalidString(key, lang)));
    }

    public static boolean isValid(File lang, String key) {
        boolean b = (messages.get(lang) != null && ((Map) messages.get(lang)).get(key) != null);
        if (!b & !missingKeys.contains(key)) missingKeys.add(key + ";" + lang.getName().split(".yml")[0]);

        return b;
    }

    public static boolean isValidUnlisted(File lang, String key) {
        return (messages.get(lang) != null && ((Map) messages.get(lang)).get(key) != null);
    }

    public static String invalidString(String key, File lang) {
        String s = "";
        try {
            return s = (messages != null && messages.get(getServerLang()) != null) ? (String) ((Map)
                    messages.get(getServerLang())).get(key) : String.format(
                    "§7§oString '%s' in language file '%s' not loaded yet!§7", key,
                    lang.getName().split(".yml")[0]);

        } catch (InvalidStringException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    public static void loadCustomLanguages(File langF) {
        if (langF == null || langF.listFiles() == null || (langF.listFiles()).length == 0) {
            loadResources();
            Bukkit.getConsoleSender().sendMessage("Resources loaded");
            loadCustomLanguages(getLangFile("en"));
        }

        for (File file : langF.listFiles()) {
            Map<String, String> lm = new HashMap<>();
            FileConfiguration lang = YamlConfiguration.loadConfiguration(file);

            ConsoleCommandSender cs = Bukkit.getConsoleSender();
            for (String key : lang.getKeys(false))
                for (String messName : lang.getConfigurationSection(key).getKeys(false)) {

                    cs.sendMessage(key + "." + messName);
                    if (lang.getString(key + "." + messName) != null) {

                        cs.sendMessage(key + "§8/§7" + messName + " §8» §aWas loaded");
                        if (lang.getString(key + "." + messName) != null) {

                            messages.put(file, lm);

                            if (!file.exists()) Bukkit.getConsoleSender().sendMessage(String.format(
                                    Lang.PRE + "Language %s could not be loaded", file.getName()));

                        }
                    }
                }

            Bukkit.getConsoleSender().sendMessage(Lang.PRE +
                    String.format(getMessage(file, "language_loaded"), file.getName()));
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


                for (Player ap : Bukkit.getOnlinePlayers())
                    System.out.println(key + "." + messName + ": " + l.getString(key + "." + messName));
            }
        }
    }
}