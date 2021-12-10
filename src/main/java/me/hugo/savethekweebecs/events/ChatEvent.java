package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Game game = SaveTheKweebecs.getPlugin().getPlayerGame(player);

        if (event.isCancelled()) return;

        event.setCancelled(true);

        if (game != null) {
            if (game.getGameState() == GameState.INGAME) {
                for (Player playerInGame : new ArrayList<>(game.getPlayerList()))
                    playerInGame.sendMessage((game.getTrorkPlayers().contains(player) ? "§b[T] §7" : (game.getSpectatorList().contains(player) ? "§6[Spectator] §7" : "§e[K] §7")) + player.getName() + " §8» §7" + event.getMessage());
            } else {
                for (Player playerInGame : new ArrayList<>(game.getPlayerList()))
                    playerInGame.sendMessage((player.isOp() ? "§c[Admin] §f" : "§7") + player.getName() + " §8» §7" + event.getMessage());
            }
        } else {
            for (Player playerInWorld : player.getWorld().getPlayers())
                playerInWorld.sendMessage((player.isOp() ? "§c[Admin] §f" : "§7") + player.getName() + " §8» §7" + event.getMessage());
        }
    }

}
