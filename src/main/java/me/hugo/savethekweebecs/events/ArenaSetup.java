package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.LocationUtil;
import me.hugo.savethekweebecs.utils.StringUtility;
import me.hugo.savethekweebecs.utils.gui.ClickAction;
import me.hugo.savethekweebecs.utils.gui.Icon;
import me.hugo.savethekweebecs.utils.gui.MenuHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class ArenaSetup implements Listener {

    private static String currentSetting;
    private static UUID currentArena;
    private static Game game;
    FileConfiguration configurationFile = SaveTheKweebecs.getPlugin().getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItem(0, SaveTheKweebecs.getPlugin().getArenaCreator());
        player.getInventory().setHeldItemSlot(0);
    }

    public static void updateArenaCreator(Player player) {
        if (currentArena == null) {
            game = new Game();
            currentArena = UUID.randomUUID();
            game.setUuid(currentArena);
            player.sendMessage(StringUtility.format("&eA new arena instance has been created! You can now start configuring it!"));
        }

        MenuHandler arenaSetupMenu = SaveTheKweebecs.getPlugin().getArenaSetupMenu();

        arenaSetupMenu.setIcon(11, new Icon(new ItemBuilder(Material.OAK_SAPLING, 1)
                .setName("&aAdd Kweebec Spawnpoint")
                .setLoreWithWrap("&7Click to add a Kweebec spawnpoint.\n\n&fSpawnpoints: " + game.getKweebecSpawns().size() + "\n\n&eClick to add!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            Location playerLocation = player1.getLocation();
            game.getKweebecSpawns().add(playerLocation);
            player1.sendMessage(StringUtility.format("&eYou added a kweebec spawnpoint."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(13, new Icon(new ItemBuilder(Material.OAK_SIGN, 1)
                .setName("&aMap Name")
                .setLoreWithWrap("&7Click to add a a Map Name.\n\n" + "&fCurrent Name: &a"
                        + (game.getMapName() == null ? "None" : game.getMapName()) + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            player1.closeInventory();
            player1.sendMessage(StringUtility.format("&e&lType in chat the new map name you want."));
            currentSetting = "mapName";
        }));

        arenaSetupMenu.setIcon(15, new Icon(new ItemBuilder(Material.IRON_AXE, 1)
                .setName("&aAdd Trork Spawnpoint")
                .setLoreWithWrap("&7Click to add a Trork spawnpoint.\n\n&fSpawnpoints: " + game.getTrorkSpawns().size() + "\n\n&eClick to add!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            Location playerLocation = player1.getLocation();
            game.getTrorkSpawns().add(playerLocation);
            player1.sendMessage(StringUtility.format("&eYou added a trork spawnpoint."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(29, new Icon(new ItemBuilder(Material.SEA_PICKLE, 1)
                .setName("&aAdd Kweebec NPC Spawnpoint")
                .setLoreWithWrap("&7Click to add a Kweebec NPC spawnpoint.\n\n&fSpawnpoints: " + game.getKweebecNPCSpawns().size() + "\n\n&eClick to add!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            Location playerLocation = player1.getLocation();
            game.getKweebecNPCSpawns().add(playerLocation);
            player1.sendMessage(StringUtility.format("&eYou added a kweebec NPC spawnpoint."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(31, new Icon(new ItemBuilder(Material.PLAYER_HEAD, 1)
                .setName("&aMinimum Players")
                .setLoreWithWrap("&7Click to change the minimum players for this map.\n\n" + "&fMinumum Players: &a"
                        + game.getMinPlayers() + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            player1.closeInventory();
            player1.sendMessage(StringUtility.format("&e&lType in chat the minimum players for this map."));
            currentSetting = "minPlayers";
        }));

        arenaSetupMenu.setIcon(32, new Icon(new ItemBuilder(Material.PLAYER_HEAD, 2)
                .setName("&aMaximum Players")
                .setLoreWithWrap("&7Click to change the maximum players for this map.\n\n" + "&fMaximum Players: &a"
                        + game.getMaxPlayers() + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            player1.closeInventory();
            player1.sendMessage(StringUtility.format("&e&lType in chat the maximum players for this map."));
            currentSetting = "maxPlayers";
        }));

        arenaSetupMenu.setIcon(33, new Icon(new ItemBuilder(Material.PLAYER_HEAD, 2)
                .setName("&aPlayers per Team")
                .setLoreWithWrap("&7Click to change the players per team for this map.\n\n" + "&fPlayers Per Team: &a"
                        + game.getPlayersPerTeam() + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            player1.closeInventory();
            player1.sendMessage(StringUtility.format("&e&lType in chat the players per team for this map."));
            currentSetting = "playersPerTeam";
        }));

        arenaSetupMenu.setIcon(46, new Icon(new ItemBuilder(Material.GOLDEN_APPLE, 1)
                .setName("&aSpectator Spawn")
                .setLoreWithWrap("&7Click to set your location as the spectator spawn." + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            game.setSpectatorLocation(player.getLocation());
            player1.sendMessage(StringUtility.format("&eSet spectator spawn to your location."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(45, new Icon(new ItemBuilder(Material.NETHER_STAR, 1)
                .setName("&aLobby Spawn")
                .setLoreWithWrap("&7Click to set your location as the lobby spawn." + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            game.setLobbyLocation(player.getLocation());
            player1.sendMessage(StringUtility.format("&eSet lobby spawn to your location."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(50, new Icon(new ItemBuilder(Material.NETHER_STAR, 1)
                .setName("&aMain lobby")
                .setLoreWithWrap("&7Click to set your location as the MAIN lobby spawn." + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            SaveTheKweebecs.getPlugin().setMainLobby(player.getLocation());
            player1.sendMessage(StringUtility.format("&eSet main lobby spawn to your location."));
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIcon(51, new Icon(new ItemBuilder(Material.GREEN_TERRACOTTA, 1)
                .setName("&aSave")
                .setLoreWithWrap("&7Click to save this map in the config." + "\n\n&eClick to change!", 35)
                .toItemStack()).addClickAction((player1, type, main) -> {
            if (SaveTheKweebecs.getPlugin().getMainLobby() != null) {
                SaveTheKweebecs.getPlugin().getConfig().set("mainLobbyLocation", LocationUtil.getStringByLocation(SaveTheKweebecs.getPlugin().getMainLobby()));
                player.sendMessage(StringUtility.format("&eMain Lobby location saved!"));
                SaveTheKweebecs.getPlugin().setMainLobby(null);
            }
            game.save();
            player.sendMessage(StringUtility.format("&eGame settings have been saved on the file!"));
            currentArena = null;
            game = null;
            updateArenaCreator(player);
        }));

        arenaSetupMenu.setIndicator(new ItemBuilder(Material.GOLD_NUGGET, 1).setName("&aMap being set up")
                .setLoreWithWrap("&fUUID: &a" + currentArena.toString(), 60).toItemStack());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (currentSetting.equals("mapName")) {
            event.setCancelled(true);
            game.setMapName(event.getMessage());
            updateArenaCreator(player);
            currentSetting = null;
        } else if (currentSetting.equals("minPlayers")) {
            event.setCancelled(true);
            game.setMinPlayers(Integer.valueOf(event.getMessage()));
            updateArenaCreator(player);
            currentSetting = null;
        } else if (currentSetting.equals("maxPlayers")) {
            event.setCancelled(true);
            game.setMaxPlayers(Integer.valueOf(event.getMessage()));
            updateArenaCreator(player);
            currentSetting = null;
        } else if (currentSetting.equals("playersPerTeam")) {
            event.setCancelled(true);
            game.setPlayersPerTeam(Integer.valueOf(event.getMessage()));
            updateArenaCreator(player);
            currentSetting = null;
        }
    }

}
