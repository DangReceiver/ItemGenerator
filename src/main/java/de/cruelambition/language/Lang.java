package de.cruelambition.language;

import java.awt.Color;
import java.io.File;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Lang extends Language {
    public static String PRE = "§9ItemGenerator";

    private File lf;
    private Player p;

    private String last;
    private final Language lang;

    public Lang(Player player) {
        lang = new Language();
        p = player;
        lf = lang.getLang(p.isOnline() ? p : null);
    }

    public void setPlayerLanguage(File pLang) {
        if (p != null) lang.setPlayerLang(p, pLang);
    }

    public void setPlayer(Player player) {
        lf = lang.getLang((p = player));
    }

    public String getString(String key) {
        return Language.getMessage(lf, (last = key));
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
        Bukkit.getConsoleSender().sendMessage(getMessage(getServerLang(), "info") + getMessage(getServerLang(), "info"));

        for (Player ap : Bukkit.getOnlinePlayers())
            ap.sendMessage(PRE + PRE);
    }

    public static void broadcastArg(String key, String... arg) {
        Bukkit.getConsoleSender().sendMessage(getMessage(getServerLang(), "broadcast") + getMessage(getServerLang(), "broadcast"));

        for (Player ap : Bukkit.getOnlinePlayers()) {
            String format = String.format(getMessage((new Lang(ap)).getLang(ap), key), (Object[]) arg);
            ap.sendMessage(PRE + PRE);
        }
    }
}