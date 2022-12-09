package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = SaveTheKweebecs.getPlugin().getPlayerGame(player);

        event.setQuitMessage(null);

        if (game != null) game.leaveGame(player);
        SaveTheKweebecs.getPlugin().getPlayerManager().removeGamePlayer(player);

        for (GamePlayer gamePlayer : SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayers()) {
            Player updatingPlayer = gamePlayer.getPlayer();
            Game gameFromPlayer = SaveTheKweebecs.getPlugin().getPlayerGame(updatingPlayer);

            if (gameFromPlayer == null)
                gamePlayer.getPlayerBoard().set("Players: §a" + (Bukkit.getOnlinePlayers().size() - 1), 3);
        }
    }

}
