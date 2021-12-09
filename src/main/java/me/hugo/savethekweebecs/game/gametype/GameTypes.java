package me.hugo.savethekweebecs.game.gametype;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.gameevent.GameEvent;
import org.bukkit.ChatColor;

import java.util.Arrays;

public enum GameTypes {

    NORMAL(new GameType("Normal", ChatColor.GREEN, Arrays.asList(GameEvent.GOLDEN_FRENZY, GameEvent.TRORK_WIN),
            SaveTheKweebecs.getPlugin().getKitManager().TRORK_NORMAL_KIT, SaveTheKweebecs.getPlugin().getKitManager().KWEEBEC_NORMAL_KIT));

    private GameType gameType;

    GameTypes(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }
}
