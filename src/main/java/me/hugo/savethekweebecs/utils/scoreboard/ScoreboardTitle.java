package me.hugo.savethekweebecs.utils.scoreboard;

public record ScoreboardTitle(String text, int interval) {

    @Override
    public String toString() {
        return text();
    }

}
