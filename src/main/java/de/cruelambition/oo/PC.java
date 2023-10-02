package de.cruelambition.oo;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PC {
	private final OfflinePlayer op;

	private final File f;
	private final YamlConfiguration c;

	private final String path;

	public PC(OfflinePlayer pOp) {
		path = "plugins/ItemGenerator/players";
		op = pOp;

		f = new File(path, op.getUniqueId() + ".yml");
		c = YamlConfiguration.loadConfiguration(f);
	}

	public File getFile() {
		return f;
	}

	public YamlConfiguration getCon() {
		return c;
	}

	public PC load(OfflinePlayer op) {
		if (!hasCon(op)) createCon(op);
		return new PC(op);
	}

	public PC load(Player p) {
		if (!hasCon(p)) createCon(p);
		return new PC(p);
	}

	public boolean hasCon(OfflinePlayer p) {
		return (new File(path, p.getUniqueId() + ".yml")).exists();
	}

	public PC createCon(OfflinePlayer op) {
		File tf = new File(path, op.getUniqueId() + ".yml");

		if (!tf.exists())
			try {

				tf.createNewFile();
			} catch (IOException ignore) {

				if (!createFile(op.getUniqueId())) tf.mkdir();
			}
		return new PC(op);
	}

	public static boolean createFile(UUID uuid) {
		String path = "plugins/ItemGenerator/players";
		File f1 = new File(path, uuid.toString() + ".yml");

		if (f1.exists())
			return true;

		try {
			if (!f1.createNewFile()) {
				System.out.println("The file could not be created.");
				return false;
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	public void set(String pPath, Object pInput) {
		c.set(pPath, pInput);
	}

	public String getString(String pPath) {
		return c.getString(pPath);
	}

	public List<String> getStringList(String pPath) {
		return c.getStringList(pPath);
	}

	public boolean getBoolean(String pPath) {
		return c.getBoolean(pPath);
	}

	public Color getColor(String pPath) {
		return c.getColor(pPath);
	}

	public ConfigurationSection getConfigurationSection(String pPath) {
		return c.getConfigurationSection(pPath);
	}

	public double getDouble(String pPath) {
		return c.getDouble(pPath);
	}

	public float getFloat(String pPath) {
		return (float) c.getDouble(pPath);
	}

	public int getInt(String pPath) {
		return c.getInt(pPath);
	}

	public long getLong(String pPath) {
		return c.getLong(pPath);
	}

	public boolean isSet(String pPath) {
		return c.isSet(pPath);
	}

	public OfflinePlayer getOfflinePlayer(String pPath) {
		return c.getOfflinePlayer(pPath);
	}

	public Player getPlayer(String pPath) {
		return c.getOfflinePlayer(pPath).isOnline() ?
				(Player) c.getOfflinePlayer(pPath) : null;
	}

	public OfflinePlayer thisOfflinePlayer() {
		return op;
	}

	public Player thisPlayer() {
		return op.isOnline() ? (Player) op : null;
	}

	public boolean setDefaultMessageColor(int r, int g, int b) {
		if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) return false;

		c.set("Customization.Chat.ChatColor", new ArrayList(Arrays.asList(r, g, b)));
		return true;
	}

	public boolean setDefaultMessageColor(List<Integer> rgb) {
		if (rgb.size() != 2 || rgb.get(0) > 255 || rgb.get(0) < 0 ||
				rgb.get(1) > 255 || rgb.get(1) < 0 || rgb.get(2) > 255 || rgb.get(2) < 0) {
			return false;
		}
		c.set("Customization.Chat.ChatColor", rgb);
		return true;
	}

	public List<Integer> getDefaultMessageColor() {
		return c.isSet("Customization.Chat.ChatColor") ? c.getIntegerList("Customization.Chat.ChatColor") :
				Arrays.<Integer>asList(new Integer[] {Integer.valueOf(190),
						Integer.valueOf(180), Integer.valueOf(185)});
	}

	public String getPlayerColor() {
		return c.isSet("Customization.Chat.Color") ? c.getString("Customization.Chat.Color") : "§7";
	}

	public String getCounterLink() {
		return c.isSet("Customization.Chat.Link.Counter") ?
				c.getString("Customization.Chat.Link.Counter") : "§8";
	}

	public String getLink() {
		return c.isSet("Customization.Chat.Link.Default") ?
				c.getString("Customization.Chat.Link.Default") : "§8:";
	}

	public List<String> getLinks() {
		return c.isSet("Customization.Chat.Link") ? new ArrayList<>(Arrays.asList(getCounterLink(),
				getLink())) : new ArrayList<>(Arrays.asList("", "§8:"));
	}

	public boolean hasLogoutLocation() {
		return c.isSet("Statistics.Locations.LogOut");
	}

	public Location getLogoutLocation() {
		return c.getLocation("Statistics.Locations.LogOut");
	}

	public void setLogoutLocation(Location l) {
		c.set("Statistics.Locations.LogOut", l);
	}

	public boolean hasDeathLocation() {
		return c.isSet("Statistics.Locations.Death");
	}

	public Location getDeathLocation() {
		return c.getLocation("Statistics.Locations.Death");
	}

	public void setDeathLocation(Location l) {
		c.set("Statistics.Locations.Death", l);
	}

	public String getLanguageString() {
		return c.isSet("Settings.Language") ? c.getString("Settings.Language") :
				Language.getServerLang().getName().split("\\.yml")[0];
	}

	public File getLanguage() {
		return Language.getLangFile(getLanguageString() != null ?
				getLanguageString() : Language.getServerLang().getName().split("\\.")[0]);
	}

	public boolean setLanguage(File f) {
		if (Lang.isLangFile(f.getName().split("\\.")[0])) {
			c.set("Settings.Language", f.getName().split("\\.")[0]);
			return true;
		}
		return false;
	}

	public void updatePlayTime() {
		setTotalPlayTime(getTotalPlayTime() + getQuitTime() - getJoinTime());
		setJoinTime(-1L);
	}

	public void setTotalPlayTime(long millis) {
		c.set("Mechanics.Time.TotalPlayTime", Long.valueOf(millis));
	}

	public long getTotalPlayTime() {
		return c.getLong("Mechanics.Time.TotalPlayTime");
	}

	public long getCurrentPlayTIme() {
		return c.getLong("Mechanics.Time.TotalPlayTime") + System.currentTimeMillis() - getJoinTime();
	}

	public void setQuitTime(long millis) {
		c.set("Mechanics.Time.QuitTime", Long.valueOf(millis));
	}

	public long getQuitTime() {
		return c.getLong("Mechanics.Time.QuitTime");
	}

	public void setJoinTime(long millis) {
		c.set("Mechanics.Time.JoinTime", Long.valueOf(millis));
	}

	public long getJoinTime() {
		return c.getLong("Mechanics.Time.JoinTime");
	}

	public int getPronouns() {
		return c.getInt("Settings.Pronouns", 0);
	}

	public void setPronouns(int i) {
		c.set("Settings.Pronouns", Integer.valueOf((i <= 4) ? i : 0));
	}

	public float getReadOutVolume() {
		return (float) c.getDouble("Settings.ReadOut.Volume", 1.0D);
	}

	public void setReadOutVolume(float volume) {
		c.set("Settings.ReadOut.Volume", Float.valueOf(volume));
	}

	public float getReadOutPitch() {
		return (float) c.getDouble("Settings.ReadOut.Pitch", 1.0D);
	}

	public void setReadOutPitch(float pitch) {
		c.set("Settings.ReadOut.Pitch", Float.valueOf(pitch));
	}

	public boolean getReadOutEnabled() {
		return c.getBoolean("Settings.ReadOut.toggle");
	}

	public void setReadOutEnabled(boolean enabled) {
		c.set("Settings.ReadOut.toggle", Boolean.valueOf(enabled));
	}

	public void savePCon() {
		try {
			c.save(f);
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(Language.getMessage(Lang.getServerLang(),
					"info") + Language.getMessage(Lang.getServerLang(), "info"));
		}
	}
}