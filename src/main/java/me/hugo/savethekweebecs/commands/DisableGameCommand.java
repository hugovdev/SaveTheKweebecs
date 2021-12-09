package me.hugo.savethekweebecs.commands;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

public class DisableGameCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length == 0) {
                player.sendMessage("§cUsage: /disablegame §b<game>");
                return true;
            }

            if (player.isOp()) {
                String game = args[0];
                GlobalGame globalGame = GlobalGame.fromName(game);

                if (globalGame != null) {
                    globalGame.setOpen(false);

                    GlobalGame availableGame = null;

                    for (GlobalGame games : GlobalGame.values())
                        if (games.isOpen()) {
                            availableGame = games;
                            break;
                        }

                    for (GamePlayer gamePlayer : SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayers()) {
                        Player players = gamePlayer.getPlayer();
                        if (gamePlayer.getCurrentGame() == globalGame && SaveTheKweebecs.getPlugin().getPlayerGame(players) == null) {
                            if (availableGame != null) {
                                players.sendMessage("§cThe game §b" + globalGame.getName() + "§c was closed!");
                                players.sendMessage("§aRedirecting to §b" + availableGame.getName() + "§a...");
                                gamePlayer.setCurrentGame(availableGame);
                                availableGame.getClickAction().execute(players, ClickType.LEFT);
                            } else {
                                players.kickPlayer("§cGame you were on was closed!");
                            }
                        }
                        gamePlayer.updateGameSelector();
                    }

                    player.sendMessage("§aYou closed the game §b" + globalGame.getName());

                    if (SaveTheKweebecs.getPlugin().getDefaultGame() == globalGame && availableGame != null) {
                        player.sendMessage("§aAlso set §b" + availableGame.getName() + "§a as the default game!");
                        SaveTheKweebecs.getPlugin().setDefaultGame(availableGame);
                    }
                } else {
                    player.sendMessage("§cGame §b" + game + " §ccould not be found!");

                    String gameList = "";
                    for (GlobalGame games : GlobalGame.values())
                        gameList = gameList + games.name() + " | ";

                    player.sendMessage("§cPossible games: §b" + gameList);
                }
            } else {
                player.sendMessage("§cYou can't execute this command!");
            }
        } else {
            sender.sendMessage("You need to be a player to execute this command!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("disablegame") && sender instanceof Player && sender.isOp()) {
            if (args.length == 1 && args[0] == "") {
                return Arrays.asList(Arrays.stream(GlobalGame.values()).map(GlobalGame::name).toArray(String[]::new));
            }
        }
        return null;
    }
}