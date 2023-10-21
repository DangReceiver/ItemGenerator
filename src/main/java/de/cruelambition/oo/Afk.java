package de.cruelambition.oo;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.cruelambition.cmd.user.Spawn;
import de.cruelambition.itemgenerator.ItemGenerator;
import de.cruelambition.language.Lang;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;

public class Afk implements Listener {

	private final BukkitTask checker;

	public Afk() {
		checker = Bukkit.getScheduler().runTaskTimer(ItemGenerator.getItemGenerator(), () -> {
			PC pc;

			for (Player ap : Bukkit.getOnlinePlayers()) {
				pc = new PC(ap);

				if (isAfk(pc)) {
					sendToSpawn(ap);
					ap.sendMessage("detected as afk");
				}
			}
		}, 10 * 20, 20 * 20);
	}

	public BukkitTask getChecker() {
		return checker;
	}

	public void updateAfkTime(PC pc) {
		pc.setAfkTime(System.currentTimeMillis() - pc.getAfkLead());
	}

	// ^^
	public boolean isAfk(PC pc) {
		if (pc.getAfkTime() >= 9000 || System.currentTimeMillis() - pc.getAfkLead() >= 9000) {

			pc.thisPlayer().sendMessage((pc.getAfkTime() >= 9000) + " || "
					+ (System.currentTimeMillis() - pc.getAfkLead() >= 9000) + " || " +
					System.currentTimeMillis() + " - " + pc.getAfkLead() + " ==> "
					+ (System.currentTimeMillis() - pc.getAfkLead()));

			warnAfk(pc);
			return false;
		}

		if (pc.getAfkTime() >= 10000 || System.currentTimeMillis() - pc.getAfkLead() >= 10000)
			setAfk(pc, true);

		return true;
	}

	public void warnAfk(PC pc) {
		Player p = pc.thisPlayer();

		p.sendTitle(Lang.PRE, new Lang(p).getString("afk_warn"), 5, 60, 20);
		Utils.oneByOne(p, Sound.BLOCK_NOTE_BLOCK_BELL, 4, 0.8f, 0.2f,
				true, 1, 7, 0);
	}

	public void setAfk(PC pc, boolean isAfk) {
		Player p = pc.thisPlayer();

		p.sendTitle(Lang.PRE, new Lang(p).getString("afk_warn"), 30, 200, 80);
		Utils.oneByOne(p, Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1.3f, -0.2f,
				true, 1, 6, 0);
		pc.setAfk(isAfk);
	}

	public void unsetAfk(PC pc) {
		pc.setAfk(false);
		Player p = pc.thisPlayer();

		p.sendTitle(" ", " ", 0, 0, 0);
		p.teleport(pc.getLogoutLocation());
	}

	public void sendToSpawn(Player p) {
		Spawn.sendToSpawn(p);
	}


	// Afk tracking
	@EventHandler
	public void handle(PlayerToggleSneakEvent e) {
		afkListener(e.getPlayer());
	}

	@EventHandler
	public void handle(PlayerJumpEvent e) {
		afkListener(e.getPlayer());
	}

	@EventHandler
	public void handle(AsyncChatEvent e) {
		Bukkit.getScheduler().runTaskLater(ItemGenerator.getItemGenerator(),
				() -> afkListener(e.getPlayer()), 0);
	}

	@EventHandler
	public void handle(InventoryClickEvent e) {
		HumanEntity he = e.getWhoClicked();
		if (!(he instanceof Player p)) return;

		afkListener(p);
	}

	public void afkListener(Player p) {
		PC pc = new PC(p);
		pc.setAfkLead(System.currentTimeMillis());
		pc.savePCon();

		if (!isAfk(pc)) return;
		else if (pc.isAfk()) unsetAfk(pc);
	}
}
