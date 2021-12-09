package me.hugo.savethekweebecs.utils.scoreboard;

public class ScoreboardTitle {

    private String text;
    private int interval;

    public ScoreboardTitle(String text, int interval) {
        this.text = text;
        this.interval = interval;
    }

    public String getText() {
        return text;
    }

    public int getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return getText();
    }

}
