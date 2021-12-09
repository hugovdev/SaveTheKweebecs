package me.hugo.savethekweebecs.commands;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameMap;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveTheKweebecsCommand implements CommandExecutor {

    SaveTheKweebecs main;

    public SaveTheKweebecsCommand(SaveTheKweebecs main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                sendHelpMessage(player);
                return true;
            } else {
                String subCommand = args[0];

                if (player.isOp()) {
                    if (subCommand.equalsIgnoreCase("addgame")) {
                        if (args.length == 3) {
                            String mapName = args[1].toLowerCase();
                            String gameName = args[2].replaceAll("_", " ");
                            GameMap gameMap = main.getMap(mapName);

                            if (gameMap != null) {
                                new Game(gameMap, main, gameName);
                                player.sendMessage("§eCreated game on map §a" + mapName + " §ewith name §b" + gameName + "§e!");
                            } else {
                                player.sendMessage("§cCould not find the map §b" + mapName + "§c!");
                                player.sendMessage("§cValid maps:");

                                String gameMapNames = "";
                                for (String mapNames : main.getGameMaps().keySet())
                                    gameMapNames = gameMapNames + mapNames + " | ";

                                player.sendMessage("§b" + gameMapNames);
                            }
                        } else {
                            player.sendMessage("§cUsage: §c/stk §baddgame <mapName> <gameName>");
                        }
                    } else if (subCommand.equalsIgnoreCase("removegame")) {
                        if (args.length == 2) {
                            String gameName = args[1].replaceAll("_", " ");

                            main.getArenaSelectorMenu().reset();

                            for (Game game : new ArrayList<>(main.getGames())) {
                                if (!game.getMapName().equalsIgnoreCase(gameName)) {
                                    main.getArenaSelectorMenu().addItem(game.getGameIcon());
                                } else {
                                    if (game.getGameState() == GameState.WAITING || game.getGameState() == GameState.STARTING) {
                                        for (Player players : new ArrayList<>(game.getPlayerList())) {
                                            game.leaveGame(players);
                                            player.sendMessage("§cYou have been §bkicked§c from this game because it has been removed!");
                                        }

                                        game.remove();
                                        main.getGames().remove(game);
                                        player.sendMessage("§eGame §a" + gameName + " §eremoved!");
                                    } else {
                                        main.getArenaSelectorMenu().addItem(game.getGameIcon());
                                        player.sendMessage("§cGame is ingame! Please remove it when it hasn't started.");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage("§cUsage: §c/stk §bremovegame <gameName>");
                        }
                    } else {
                        sendHelpMessage(player);
                    }
                } else {
                    player.sendMessage("§cYou can't execute this command!");
                }
            }
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage("§eSave The Kweebecs version §a" + SaveTheKweebecs.getPlugin().getDescription().getVersion());
        player.sendMessage("§7Play Game Mode");
        player.sendMessage("§eCommand List:");
        player.sendMessage("");
        player.sendMessage("§c/stk §baddgame <mapName> <gameName>");
        player.sendMessage("§c/stk §bremovegame <gameName>");
    }
}
