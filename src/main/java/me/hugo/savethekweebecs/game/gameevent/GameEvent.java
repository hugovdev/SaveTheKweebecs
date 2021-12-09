package me.hugo.savethekweebecs.game.gameevent;

import me.hugo.savethekweebecs.game.gameevent.list.GoldFrenzyEvent;
import me.hugo.savethekweebecs.game.gameevent.list.TrorksWinEvent;

public enum GameEvent {

    GOLDEN_FRENZY(80, "Gold Frenzy", new GoldFrenzyEvent()),
    TRORK_WIN(220, "Trorks Win", new TrorksWinEvent());

    public int time;
    public String name;
    public GameEventAction actionClass;

    GameEvent(int time, String name, GameEventAction actionClass) {
        this.time = time;
        this.name = name;
        this.actionClass = actionClass;
    }

}
