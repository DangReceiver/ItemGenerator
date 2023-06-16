package de.cruelambition.listener.essential;

import de.cruelambition.language.Lang;
import de.cruelambition.oo.PC;
import de.cruelambition.worlds.SpawnWorld;

import java.io.File;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CM implements Listener {
    @EventHandler
    public void handle(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PC pc = new PC(p);

        if (pc.getLanguageString() == null)
            pc.setLanguage(Lang.getServerLang());

        pc.setJoinTime(System.currentTimeMillis());
        pc.savePCon();

        Lang l = new Lang(p);
        File lf = pc.getLanguage();
        l.setPlayerLanguage(lf);

        e.setJoinMessage(null);
        if (!p.hasPlayedBefore()) {
            Lang.broadcastArg("player_first_join", new String[]{p.getName()});
            p.teleport(SpawnWorld.getSafeSpawnLocation());

        } else if (pc.getJoinTime() - pc.getQuitTime() <= 20000L)
            Lang.broadcastArg("player_rejoin_" + (new Random())
                    .nextInt(4), new String[]{p.getName()});
        else
            Lang.broadcastArg("player_join_" + (new Random()).nextInt(17), new String[]{p.getName()});

        p.setGameMode(GameMode.ADVENTURE);
        p.sendTitle(Lang.PRE, String.format(Lang.getMessage(lf, "welcome_back"), new Object[]{p.getName()}), 30, 50, 50);
    }

    @EventHandler
    public void handle(PlayerQuitEvent e) {
    }
}


/* Location:              H:\Downloads\ItemGenerator-0.1.0.jar!\de\cruelambition\listener\essential\CM.class
 * Java compiler version: 16 (60.0)
 * JD-Core Version:       1.1.3
 */