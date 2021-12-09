package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        player.sendMessage("§aYou connected to §bLobby 1§a.");

        for (GamePlayer gamePlayer : SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayers()) {
            Player updatingPlayer = gamePlayer.getPlayer();
            Game game = SaveTheKweebecs.getPlugin().getPlayerGame(updatingPlayer);

            if (game == null)
                gamePlayer.getPlayerBoard().set("Players: §a" + Bukkit.getOnlinePlayers().size(), 3);
        }

        SaveTheKweebecs.getPlugin().getDefaultGame().getClickAction().execute(player, ClickType.LEFT);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld() == player.getWorld()) {
                p.showPlayer(SaveTheKweebecs.getPlugin(), player);
                player.showPlayer(SaveTheKweebecs.getPlugin(), p);
                continue;
            }
            p.hidePlayer(SaveTheKweebecs.getPlugin(), player);
            player.hidePlayer(SaveTheKweebecs.getPlugin(), p);
        }

        player.setPlayerListHeader("\n§b§lHYTALE THANKMAS\n\n§7Remember to donate to charity with the\n§7link on the description!\n");
        player.setPlayerListFooter("\n§eHosted by ThoriumCube\n§aevents.thoriumcu.be\n");

        SaveTheKweebecs.getPlugin().getBossBar().addPlayer(player);
    }

}
