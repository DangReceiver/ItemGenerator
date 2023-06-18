package de.cruelambition.oo;

import de.cruelambition.language.Lang;
import de.cruelambition.language.Language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PC {
	private final OfflinePlayer op;
	private String uuid;

	private final File f;
	private final YamlConfiguration c;

	private final String path;

	public PC(OfflinePlayer pOp) {
		/*  29 */
		path = "ItemGenerator/player/%s.yaml";
		/*  30 */
		op = pOp;

		/*  32 */
		f = new File("ItemGenerator/player/" + op.getUniqueId() + ".yaml");
		/*  33 */
		new YamlConfiguration();
		/*  34 */
		c = YamlConfiguration.loadConfiguration(f);
	}

	public File getFile() {
		/*  38 */
		return f;
	}

	public YamlConfiguration getCon() {
		/*  42 */
		return c;
	}

	public PC load(OfflinePlayer op) {
		/*  46 */
		if (!hasCon(op))
			/*  47 */ createCon(op);
		return new PC(op);
	}

	public PC load(Player p) {
		/*  52 */
		if (!hasCon((OfflinePlayer) p))
			/*  53 */ createCon((OfflinePlayer) p);
		/*  54 */
		return new PC((OfflinePlayer) p);
	}

	public boolean hasCon(OfflinePlayer p) {
		/*  58 */
		return (new File(String.format(path, new Object[] {p.getUniqueId()}))).exists();
	}

	public PC createCon(OfflinePlayer op) {
		/*  62 */
		File tf = new File(String.format(path, new Object[] {op.getUniqueId()}));

		/*  64 */
		if (!tf.exists())
			try {
				/*  66 */
				tf.createNewFile();
			}
			/*  68 */ catch (IOException ignore) {

				/*  70 */
				if (!createFolder(path))
					try {
						/*  72 */
						tf.createNewFile();
					}
					/*  74 */ catch (IOException ex) {
						/*  75 */
						ex.printStackTrace();
					}
			}
		/*  78 */
		return new PC(op);
	}


	public static boolean createFolder(String path) {
		/*  83 */
		if (path == null) path = "plugins/ItemGenerator/player";
		/*  84 */
		File f1 = new File(path);

		/*  86 */
		if (f1.exists()) {
			/*  87 */
			System.out.println("The folder already exists.");
			/*  88 */
			return false;
		}

		/*  91 */
		if (!f1.mkdir()) {
			/*  92 */
			System.out.println("The folder could not be created.");
			/*  93 */
			return false;
		}

		/*  96 */
		return true;
	}


	public void set(String pPath, Object pInput) {
		/* 103 */
		c.set(pPath, pInput);
	}

	public String getString(String pPath) {
		/* 107 */
		return c.getString(pPath);
	}

	public List<String> getStringList(String pPath) {
		/* 111 */
		return c.getStringList(pPath);
	}

	public boolean getBoolean(String pPath) {
		/* 115 */
		return c.getBoolean(pPath);
	}

	public Color getColor(String pPath) {
		/* 119 */
		return c.getColor(pPath);
	}

	public ConfigurationSection getConfigurationSection(String pPath) {
		/* 123 */
		return c.getConfigurationSection(pPath);
	}

	public double getDouble(String pPath) {
		/* 127 */
		return c.getDouble(pPath);
	}

	public float getFloat(String pPath) {
		/* 131 */
		return (float) c.getDouble(pPath);
	}

	public int getInt(String pPath) {
		/* 135 */
		return c.getInt(pPath);
	}

	public long getLong(String pPath) {
		/* 139 */
		return c.getLong(pPath);
	}

	public boolean isSet(String pPath) {
		/* 143 */
		return c.isSet(pPath);
	}

	public OfflinePlayer getOfflinePlayer(String pPath) {
		/* 147 */
		return c.getOfflinePlayer(pPath);
	}

	public Player getPlayer(String pPath) {
		/* 151 */
		return c.getOfflinePlayer(pPath).isOnline() ?
				/* 152 */       (Player) c.getOfflinePlayer(pPath) : null;
	}

	public OfflinePlayer thisOfflinePlayer() {
		/* 156 */
		return op;
	}

	public Player thisPlayer() {
		/* 160 */
		return op.isOnline() ? (Player) op : null;
	}

	public boolean setDefaultMessageColor(int r, int g, int b) {
		/* 164 */
		if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) return false;

		/* 166 */
		c.set("Customization.Chat.ChatColor", new ArrayList(Arrays.asList((Object[]) new Integer[] {Integer.valueOf(r), Integer.valueOf(g), Integer.valueOf(b)})));
		/* 167 */
		return true;
	}

	public boolean setDefaultMessageColor(List<Integer> rgb) {
		/* 171 */
		if (rgb.size() != 2 || ((Integer) rgb.get(0)).intValue() > 255 || ((Integer) rgb.get(0)).intValue() < 0 || ((Integer) rgb.get(1)).intValue() > 255 || ((Integer) rgb.get(1)).intValue() < 0 || ((Integer) rgb.get(2)).intValue() > 255 || ((Integer) rgb.get(2)).intValue() < 0) {
			/* 172 */
			return false;
		}
		/* 174 */
		c.set("Customization.Chat.ChatColor", rgb);
		/* 175 */
		return true;
	}

	public List<Integer> getDefaultMessageColor() {
		/* 179 */
		return c.isSet("Customization.Chat.ChatColor") ? c.getIntegerList("Customization.Chat.ChatColor") :
				/* 180 */       Arrays.<Integer>asList(new Integer[] {Integer.valueOf(190), Integer.valueOf(180), Integer.valueOf(185)});
	}


	public String getPlayerColor() {
		/* 187 */
		return c.isSet("Customization.Chat.Color") ? c.getString("Customization.Chat.Color") : "§7";
	}

	public String getCounterLink() {
		/* 191 */
		return c.isSet("Customization.Chat.Link.Counter") ? c.getString("Customization.Chat.Link.Counter") : "§8";
	}

	public String getLink() {
		/* 195 */
		return c.isSet("Customization.Chat.Link.Default") ? c.getString("Customization.Chat.Link.Default") : "§8:";
	}

	public List<String> getLinks() {
		/* 199 */
		return c.isSet("Customization.Chat.Link") ? new ArrayList<>(Arrays.asList(new String[] {getCounterLink(), getLink()
/* 200 */})) : new ArrayList<>(Arrays.asList(new String[] {"", "§8:"}));
	}

	public boolean hasLogoutLocation() {
		/* 204 */
		return c.isSet("Statistics.Locations.LogOut");
	}

	public Location getLogoutLocation() {
		/* 208 */
		return c.getLocation("Statistics.Locations.LogOut");
	}

	public void setLogoutLocation(Location l) {
		/* 212 */
		c.set("Statistics.Locations.LogOut", l);
	}

	public boolean hasDeathLocation() {
		/* 216 */
		return c.isSet("Statistics.Locations.Death");
	}

	public Location getDeathLocation() {
		/* 220 */
		return c.getLocation("Statistics.Locations.Death");
	}

	public void setDeathLocation(Location l) {
		/* 224 */
		c.set("Statistics.Locations.Death", l);
	}

	public String getLanguageString() {
		/* 228 */
		return c.getString("Settings.Language");
	}

	public File getLanguage() {
		/* 232 */
		return Language.getLangFile((getLanguageString() != null) ? getLanguageString() : Language.getServerLang().getName().split(".")[0]);
	}

	public boolean setLanguage(File f) {
		/* 236 */
		if (Lang.isLangFile(f.getName().split(".yml")[0])) {
			/* 237 */
			c.set("Settings.Language", f.getName().split("\\.")[0]);
			/* 238 */
			return true;
		}
		/* 240 */
		return false;
	}

	public void updatePlayTime() {
		/* 244 */
		setTotalPlayTime(getTotalPlayTime() + getQuitTime() - getJoinTime());
		/* 245 */
		setJoinTime(-1L);
	}

	public void setTotalPlayTime(long millis) {
		/* 249 */
		c.set("Mechanics.Time.TotalPlayTime", Long.valueOf(millis));
	}

	public long getTotalPlayTime() {
		/* 253 */
		return c.getLong("Mechanics.Time.TotalPlayTime");
	}

	public long getCurrentPlayTIme() {
		/* 257 */
		return c.getLong("Mechanics.Time.TotalPlayTime") + System.currentTimeMillis() - getJoinTime();
	}

	public void setQuitTime(long millis) {
		/* 261 */
		c.set("Mechanics.Time.QuitTime", Long.valueOf(millis));
	}

	public long getQuitTime() {
		/* 265 */
		return c.getLong("Mechanics.Time.QuitTime");
	}

	public void setJoinTime(long millis) {
		/* 269 */
		c.set("Mechanics.Time.JoinTime", Long.valueOf(millis));
	}

	public long getJoinTime() {
		/* 273 */
		return c.getLong("Mechanics.Time.JoinTime");
	}

	public int getPronouns() {
		/* 277 */
		return c.getInt("Settings.Pronouns", 0);
	}

	public void setPronouns(int i) {
		/* 281 */
		c.set("Settings.Pronouns", Integer.valueOf((i <= 4) ? i : 0));
	}

	public float getReadOutVolume() {
		/* 285 */
		return (float) c.getDouble("Settings.ReadOut.Volume", 1.0D);
	}

	public void setReadOutVolume(float volume) {
		/* 289 */
		c.set("Settings.ReadOut.Volume", Float.valueOf(volume));
	}

	public float getReadOutPitch() {
		/* 293 */
		return (float) c.getDouble("Settings.ReadOut.Pitch", 1.0D);
	}

	public void setReadOutPitch(float pitch) {
		/* 297 */
		c.set("Settings.ReadOut.Pitch", Float.valueOf(pitch));
	}

	public boolean getReadOutEnabled() {
		/* 301 */
		return c.getBoolean("Settings.ReadOut.toggle");
	}

	public void setReadOutEnabled(boolean enabled) {
		/* 305 */
		c.set("Settings.ReadOut.toggle", Boolean.valueOf(enabled));
	}

	public void savePCon() {
		try {
			/* 310 */
			c.save(f);
			/* 311 */
		} catch (IOException e) {
			/* 312 */
			Bukkit.getConsoleSender().sendMessage(Language.getMessage(Lang.getServerLang(), "info") + Language.getMessage(Lang.getServerLang(), "info"));
		}
	}


	public void savePConErrorFree() {
		try {
			/* 319 */
			c.save(f);
			/* 320 */
		} catch (IOException iOException) {
		}
	}
}