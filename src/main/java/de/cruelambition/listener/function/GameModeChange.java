package de.cruelambition.listener.function;

import de.cruelambition.language.Lang;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeChange implements Listener {

	@EventHandler
	public void handle(PlayerGameModeChangeEvent e) {
		Player p = e.getPlayer();
		Lang l = new Lang(p);

		float vol = 1.25f;

		if (e.getNewGameMode() == GameMode.SURVIVAL) {
			p.sendMessage(Lang.PRE + l.getString("gamemode_changed_survival"));

		} else {
			vol = vol - 0.5f;
			p.sendMessage(Lang.PRE + l.getString("gamemode_changed_non_survival"));
		}

		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.4f, vol);
	}
}
