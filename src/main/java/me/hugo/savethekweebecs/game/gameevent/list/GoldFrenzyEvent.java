package me.hugo.savethekweebecs.game.gameevent.list;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.gameevent.GameEventAction;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GoldFrenzyEvent extends GameEventAction {

    @Override
    public void runEvent(Game game) {
        game.sendGameMessage("&e&lGOLD FRENZY! &7Everyone has been given &e50 gold&7! Spend it on the &ashop&e!");
        game.sendGameMessage("&e+&l50 &egold!");
        game.sendSound(Sound.ENTITY_ITEM_PICKUP);

        for(Player player: new ArrayList<>(game.getPlayerList())) {
            GamePlayer gamePlayer = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player);
            gamePlayer.setGold(gamePlayer.getGold() + 50);
            gamePlayer.getPlayerBoard().set("Gold: §e" + gamePlayer.getGold() + " ⛃", 6);
        }
    }

}
