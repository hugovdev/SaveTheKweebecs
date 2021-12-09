package me.hugo.savethekweebecs.commands;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GamesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.openInventory(SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player).getGameSelectorMenu().getInventory());
        } else {
            sender.sendMessage("You need to be a player to execute this command!");
        }
        return true;
    }
}
