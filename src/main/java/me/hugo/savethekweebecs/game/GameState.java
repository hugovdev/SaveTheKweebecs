package me.hugo.savethekweebecs.game;

import org.bukkit.ChatColor;

public enum GameState {

    WAITING, STARTING, INGAME, ENDING;

    public ChatColor getColor() {
        switch (this) {
            case STARTING:
                return ChatColor.GOLD;
            case WAITING:
                return ChatColor.GREEN;
            case INGAME:
                return ChatColor.RED;
            case ENDING:
                return ChatColor.GRAY;
            default:
                return ChatColor.AQUA;
        }
    }

    public String getStateString() {
        switch (this) {
            case STARTING:
                return "Starting";
            case WAITING:
                return "Waiting";
            case INGAME:
                return "Playing";
            case ENDING:
                return "Ending";
            default:
                return "Error";
        }
    }

}
