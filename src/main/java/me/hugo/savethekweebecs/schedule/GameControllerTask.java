package me.hugo.savethekweebecs.schedule;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.game.gameevent.GameEvent;
import me.hugo.savethekweebecs.utils.StringUtility;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class GameControllerTask extends BukkitRunnable {

    SaveTheKweebecs main;

    public GameControllerTask(SaveTheKweebecs main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Game game : new ArrayList<>(main.getGames())) {
            int eventTime = game.getEventTime();

            if (game.getGameState() == GameState.INGAME) {
                manageIngameEvents(game);
            } else if (game.getGameState() == GameState.STARTING) {
                game.setEventTime(game.getEventTime() - 1);
                for (Player player : game.getPlayerList())
                    main.getPlayerManager().getGamePlayer(player).getPlayerBoard().set("Starting in §a" + eventTime + "s", 6);

                if ((eventTime % 5 == 0 && eventTime != 0) || (eventTime < 5 && eventTime != 0)) {
                    game.sendTitleMessage((eventTime > 10 ? "§a" : (eventTime > 5 ? "§6" : "§c")) + eventTime, "", 10, 30, 10);
                    game.sendGameMessage("&eGame starting in " + (eventTime > 10 ? "§a" : (eventTime > 5 ? "§6" : "§c")) + eventTime + " &esecond" + (eventTime == 1 ? "" : "s") + "!");
                    game.sendSound(Sound.BLOCK_NOTE_BLOCK_HAT);
                }

                if (eventTime == 0) {
                    game.attemptStart();
                }
            }
        }
    }

    private void manageIngameEvents(Game game) {
        int eventTime = game.getEventTime();

        GameEvent gameEvent = game.getGameType().getGameType().getEventList().get(game.getEventIndex());

        if (eventTime <= 0) {
            gameEvent.actionClass.runEvent(game);
            if (game.getEventIndex() < game.getGameType().getGameType().getEventList().size() - 1) {
                game.setEventIndex(game.getEventIndex() + 1);
                game.setEventTime(game.getGameType().getGameType().getEventList().get(game.getEventIndex()).time);
                eventTime = game.getEventTime();
            }
        }

        for (Player player : game.getPlayerList())
            main.getPlayerManager().getGamePlayer(player)
                    .getPlayerBoard().set(gameEvent.name + ": §a" + StringUtility.getDurationString(game.getEventTime()), 11);

        game.setEventTime(eventTime - 1);
    }
}