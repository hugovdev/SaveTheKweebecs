package me.hugo.savethekweebecs.schedule;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ColorUtil;
import me.hugo.savethekweebecs.utils.scoreboard.ScoreboardTitle;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreboardAnimationTask extends BukkitRunnable {

    ArrayList<ScoreboardTitle> scoreboardTitles;
    int scoreboardTitleTime;
    int scoreboardTitleIndex;
    ScoreboardTitle scoreboardTitle;


    public ScoreboardAnimationTask(String title) {
        scoreboardTitles = new ArrayList<>();
        title = ChatColor.stripColor(title.toUpperCase().replace("&", "§"));
        List<ScoreboardTitle> titleList = new ArrayList<>();
        titleList.add(new ScoreboardTitle("§b§l" + title, 100));
        for (int i = 0; i < title.length(); i++) {
            String titleResult = (i == 0 ? "" : "§f§l" + title.substring(0, i)) + "§3§l" + title.charAt(i) + ((i + 1) != title.length() ? "§b§l" + title.substring(i + 1) : "");
            titleList.add(new ScoreboardTitle(titleResult, 2));
        }
        titleList.add(new ScoreboardTitle("§f§l" + title, 5));
        titleList.add(new ScoreboardTitle("§b§l" + title, 5));
        titleList.add(new ScoreboardTitle("§f§l" + title, 5));
        scoreboardTitles.addAll(titleList);

        this.scoreboardTitleIndex = 0;
        this.scoreboardTitle = this.scoreboardTitles.get(scoreboardTitleIndex);
        this.scoreboardTitleTime = scoreboardTitle.getInterval();
    }

    @Override
    public void run() {
        if (scoreboardTitles != null && scoreboardTitles.size() > 0) {
            if (scoreboardTitleTime > 0) {
                scoreboardTitleTime = scoreboardTitleTime - 1;
            } else {
                scoreboardTitleIndex = ((scoreboardTitleIndex + 1) == scoreboardTitles.size() ? 0 : (scoreboardTitleIndex + 1));
                ScoreboardTitle newScoreboardTitle = scoreboardTitles.get(scoreboardTitleIndex);
                scoreboardTitle = newScoreboardTitle;

                scoreboardTitleTime = scoreboardTitle.getInterval();
            }

            for (GamePlayer gamePlayer : SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayers()) {
                BPlayerBoard playerBoard = gamePlayer.getPlayerBoard();
                if (!playerBoard.getName().equalsIgnoreCase(scoreboardTitle.getText())) {
                    playerBoard.setName(scoreboardTitle.getText());
                }
            }
        }
    }
}
