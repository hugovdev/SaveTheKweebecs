package me.hugo.savethekweebecs.game.gameevent.list;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.game.gameevent.GameEventAction;
import me.hugo.savethekweebecs.utils.InstantFirework;
import me.hugo.savethekweebecs.utils.npc.NPC;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TrorksWinEvent extends GameEventAction {

    @Override
    public void runEvent(Game game) {
        game.setGameState(GameState.ENDING);
        game.sendGameMessage("&cTrorks&e have won the game! Kweebecs couldn't be saved in time!");
        game.sendTitleMessage("&c&lTRORKS WON", null, 10, 40, 10);
        game.sendSound(Sound.ENTITY_PLAYER_LEVELUP);
        game.updateGameIcon();
        SkinsRestorerAPI skinsRestorerAPI = SaveTheKweebecs.getPlugin().getSkinsRestorerAPI();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : new ArrayList<>(game.getPlayerList())) {
                    SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player).setBoard(game);

                    if (!game.getSpectatorList().contains(player))
                        try {
                            skinsRestorerAPI.applySkin(new PlayerWrapper(player));
                        } catch (SkinRequestException e) {
                            e.printStackTrace();
                        }
                }
            }
        }.runTaskAsynchronously(SaveTheKweebecs.getPlugin());

        for (NPC npc : game.getRemainingKweebecs()) npc.destroyNPC(game.getPlayerList());

        /*
        Trork Win Effect
         */

        new BukkitRunnable() {
            int counter = 0;
            FireworkEffect trorkWinEffect = FireworkEffect.builder().withColor(Color.RED, Color.ORANGE, Color.BLUE, Color.BLACK).withFade(Color.YELLOW).build();

            @Override
            public void run() {
                if (counter == 8) {
                    cancel();
                    game.resetGame("Trorks");
                } else {
                    new InstantFirework(trorkWinEffect, game.getSpectatorLocation());
                }
                counter++;
            }
        }.runTaskTimer(SaveTheKweebecs.getPlugin(), 10L, 15L);
    }

}
