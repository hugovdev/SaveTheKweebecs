package me.hugo.savethekweebecs.utils.gui;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ClickAction {

    void execute(Player player, ClickType type, SaveTheKweebecs main);

}
