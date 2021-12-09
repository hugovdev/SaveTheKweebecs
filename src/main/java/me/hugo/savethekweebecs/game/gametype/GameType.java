package me.hugo.savethekweebecs.game.gametype;

import me.hugo.savethekweebecs.game.gameevent.GameEvent;
import me.hugo.savethekweebecs.game.kits.GameKit;
import org.bukkit.ChatColor;

import java.util.List;

public class GameType {

    private List<GameEvent> eventList;
    private GameKit trorkKit;
    private GameKit kweebecKit;
    private String typeName;
    private ChatColor typeColor;

    public GameType(String typeName, ChatColor typeColor, List<GameEvent> eventList, GameKit trorkKit, GameKit kweebecKit){
        this.typeName = typeName;
        this.typeColor = typeColor;
        this.eventList = eventList;
        this.trorkKit = trorkKit;
        this.kweebecKit = kweebecKit;
    }

    public ChatColor getTypeColor() {
        return typeColor;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<GameEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<GameEvent> eventList) {
        this.eventList = eventList;
    }

    public GameKit getTrorkKit() {
        return trorkKit;
    }

    public void setTrorkKit(GameKit trorkKit) {
        this.trorkKit = trorkKit;
    }

    public GameKit getKweebecKit() {
        return kweebecKit;
    }

    public void setKweebecKit(GameKit kweebecKit) {
        this.kweebecKit = kweebecKit;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeColor(ChatColor typeColor) {
        this.typeColor = typeColor;
    }
}
