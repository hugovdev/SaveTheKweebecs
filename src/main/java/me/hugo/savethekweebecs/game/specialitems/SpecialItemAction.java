package me.hugo.savethekweebecs.game.specialitems;

import me.hugo.savethekweebecs.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface SpecialItemAction {

    void execute(Player player, ClickType type, Game game);

}
