package me.hugo.savethekweebecs.game;

import com.google.inject.Inject;
import com.grinderwolf.swm.api.world.SlimeWorld;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.gametype.GameTypes;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.player.PlayerManager;
import me.hugo.savethekweebecs.utils.InstantFirework;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.LocationUtil;
import me.hugo.savethekweebecs.utils.npc.NPC;
import me.hugo.savethekweebecs.utils.StringUtility;
import me.hugo.savethekweebecs.utils.gui.Icon;
import me.hugo.savethekweebecs.utils.gui.MenuHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Game {

    private UUID uuid;
    private PlayerManager playerManager;

    private GameMap gameMap;
    private SlimeWorld slimeWorld;

    @Inject
    private SaveTheKweebecs main;

    private final List<Player> playerList;

    private List<Player> kweebecPlayers;
    private List<Player> trorkPlayers;

    private Location lobbyLocation;
    private Location spectatorLocation;
    private List<Location> trorkSpawns;
    private List<Location> kweebecSpawns;
    private ArrayList<Location> kweebecNPCSpawns;

    private List<NPC> kweebecNPCs;

    private String mapName;

    private int minPlayers;
    private int maxPlayers;
    private int playersPerTeam;

    private GameState gameState;

    private int eventIndex;
    private int eventTime;

    private List<NPC> remainingKweebecs;

    private MenuHandler teamSelectorMenu;

    private GameTypes gameType;

    private ItemStack arenaSelectorItemCache;

    @Inject
    public Game(GameMap gameMap, SaveTheKweebecs main) {
        main.getGames().add(this);
        this.main = main;
        this.gameMap = gameMap;
        this.playerManager = main.getPlayerManager();

        /*
        Load game info and simple variables.
         */
        this.uuid = UUID.randomUUID();
        this.playerList = new ArrayList<>();
        this.kweebecPlayers = new ArrayList<>();
        this.trorkPlayers = new ArrayList<>();
        this.kweebecNPCs = new ArrayList<>();

        FileConfiguration configurationFile = main.getConfig();
        Logger logger = main.getLogger();

        /*
        Load from config.
         */
        mapName = configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".mapName");
        logger.info("Loading configuration from map " + mapName + "...");

        minPlayers = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".minPlayers"));
        logger.info("  > Minimum players " + minPlayers);
        maxPlayers = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".maxPlayers"));
        logger.info("  > Maximum players " + maxPlayers);
        playersPerTeam = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".playersPerTeam"));
        logger.info("  > Players per team " + playersPerTeam);

        /*
        Use Slime World Manager to create the world
         */
        loadSlimeWorld(true);
    }

    @Inject
    public Game(GameMap gameMap, SaveTheKweebecs main, String mapName) {
        main.getGames().add(this);
        this.main = main;
        this.gameMap = gameMap;
        this.playerManager = main.getPlayerManager();

        /*
        Load game info and simple variables.
         */
        this.uuid = UUID.randomUUID();
        this.playerList = new ArrayList<>();
        this.kweebecPlayers = new ArrayList<>();
        this.trorkPlayers = new ArrayList<>();
        this.kweebecNPCs = new ArrayList<>();

        FileConfiguration configurationFile = main.getConfig();
        Logger logger = main.getLogger();

        this.mapName = mapName;

        /*
        Load from config.
         */
        minPlayers = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".minPlayers"));
        logger.info("  > Minimum players " + minPlayers);
        maxPlayers = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".maxPlayers"));
        logger.info("  > Maximum players " + maxPlayers);
        playersPerTeam = Integer.parseInt(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".playersPerTeam"));
        logger.info("  > Players per team " + playersPerTeam);

        /*
        Use Slime World Manager to create the world
         */
        loadSlimeWorld(true);
    }

    public Game() {
        this.main = SaveTheKweebecs.getPlugin();
        /*
        Load game info and simple variables.
         */
        this.playerList = new ArrayList<>();
        this.kweebecSpawns = new ArrayList<>();
        this.trorkSpawns = new ArrayList<>();
        this.kweebecNPCSpawns = new ArrayList<>();

        this.minPlayers = 0;
        this.maxPlayers = 0;
        this.playersPerTeam = 0;

        /*
        Load game default values.
         */
        gameState = GameState.WAITING;
        eventIndex = 0;
        eventTime = 30;
    }

    public void resetGame(String teamWhoWon) {
        for (Player player : playerList) {
            GamePlayer gamePlayer = main.getPlayerManager().getGamePlayer(player);

            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
                    "§e§lGame Summary\n\n" +
                            "§7Map: §f" + this.mapName + "\n" +
                            "§7Players: §f" + this.playerList.size() + "/" + this.maxPlayers + "\n" +
                            "§7Mode: §f" + this.gameType.getGameType().getTypeColor() + this.gameType.getGameType().getTypeName() + "\n" +
                            "§7UUID: \n§f" + this.uuid + "\n\n" +
                            "§7Winner: §f" + teamWhoWon + "\n" +
                            "§7Kweebecs: §f" + kweebecPlayers.size() + "\n" +
                            "§7Trorks: §f" + trorkPlayers.size() + "\n\n" +
                            "§7Kills: §f" + gamePlayer.getKills() + "\n" +
                            "§7Deaths: §f" + gamePlayer.getDeaths() + "\n" +
                            (kweebecPlayers.contains(player) ? "§7Kweebecs Saved: §f" + gamePlayer.getKweebecsSaved() + "\n" : "") +
                            "§7Gold Spent: §f" + gamePlayer.getTotalGoldSpent()));

            TextComponent message = new TextComponent(new ComponentBuilder("Game in ").color(ChatColor.YELLOW)
                    .append(this.mapName).color(ChatColor.GREEN).append(" has finished! Wanna see the stats? ").color(ChatColor.YELLOW).append("HOVER").color(ChatColor.YELLOW).bold(true).event(hoverEvent).create());

            player.spigot().sendMessage(message);
            playerManager.sendToLobby(player);
        }

        playerList.clear();
        kweebecPlayers.clear();
        trorkPlayers.clear();

        if (Bukkit.getServer().unloadWorld(uuid.toString(), false)) {
            loadSlimeWorld(false);
        }
    }

    private void loadSlimeWorld(boolean firstTime) {
        slimeWorld = gameMap.getSlimeWorld().clone(uuid.toString());

        main.getSlimePlugin().generateWorld(slimeWorld);
        World world = Bukkit.getWorld(uuid.toString());
        gameMap.setWorld(world);

        world.setKeepSpawnInMemory(false);

        if (firstTime) loadWorldLocations();
        else relocateLocations(gameMap.getWorld());

        loadDefaultValues(firstTime);
    }

    private void loadWorldLocations() {
        FileConfiguration configurationFile = main.getConfig();
        Logger logger = main.getLogger();

        lobbyLocation = LocationUtil.getLocationByString(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".lobbyLocation"), gameMap.getWorld());
        spectatorLocation = LocationUtil.getLocationByString(configurationFile.getString("games." + gameMap.getSlimeWorldName() + ".spectatorLocation"), gameMap.getWorld());

        trorkSpawns = loadSpawnpoints("trorkSpawns", "Trork SpawnPoints");
        kweebecSpawns = loadSpawnpoints("kweebecSpawns", "Kweebec SpawnPoints");
        kweebecNPCSpawns = loadSpawnpoints("kweebecNPCSpawns", "Kweebec NPC Spawns");

        int kweebecId = 0;

        for (Location location : kweebecNPCSpawns) {
            NPC npc = new NPC(location, "Kweebec" + kweebecId);
            npc.setPing(NPC.Ping.FOUR_BARS);
            npc.setSkin(new NPC.SkinTextures("ewogICJ0aW1lc3RhbXAiIDogMTYzMzk3NTk2ODExMCwKICAicHJvZmlsZUlkIiA6ICIyY2ZlNGVkYmU5MjQ0NTdjYWQyMWZiNGRlNDdjY2E4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJIdWdvR2FtZXJTdHlsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYTY2NDc5OTk3Yjk4ODc0YzY1ZjZhMDhkYmZiNmMzMjM4MGZlZDBhYTI3YWY1OGNiMWM2NmNhYmVkNWRhMjgxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                    "NCxWo5drSlsTCTMMZFEhmoTUO0BHn+48XHezFn2ZkZ+J89+1heI9kR3tHf9SR5MpK1kryXX9pEmP90OOWsCEZZGuUIdcSZlkwLsiXWXGp3dlZe0uDwIFVnkAAhsHkinf4meJOUIbUTieSD/Qxiv5cyXVvIgNwXnpz3wGszSXiU1mgjAHd6GwXWqDD/kjkNlwGGEC2qDIKM/ZLNYyNZyDCwJnDX4MVpFvTHIvtEehujg2hW8iQD2kqzbB/0podNx9FDVBz69SoZ5W0pyUtFNeWYED0zZjAVk/Dg7JnYk3YNOgzHhFxv0yvzRBA7y0+k4Tpbfe9w7wV8XrPNfmhwDLEmB6HSosWsZcYZUSr8PClH5HyN5/DlKbSUZQ0CMCSPO9kpVQAnQgdRLwtPKdFZ9c5Hi2bU1DlLbTFs+vJL/NfKnL8OcwyjxjceWeShI26CQqqUyan/pM7ti4Ih6jsh+z+2vsqfxJ1RvdLQgi7KhX13GCjPAyZaVW+OBifuseRQa0eVsyyL1Vu42/oC+5K/zhxheR/1pk2+sj3zni8AOo6yS5Ds5CJxqqeNLFEe54U6rFrw5QhCXB/p2dyYjRIEjk0eIulqn09kWWZMvblXw4xPMEOA4DtPP5+7M1xdDujiU0oiikl2SWRpZa9lF6IO0cl40SFJBnX9t4PZW3DKpn6/8="));
            npc.setGameMode(NPC.Gamemode.SURVIVAL);

            kweebecNPCs.add(npc);
            main.getNpcManager().registerNPC(npc);
            kweebecId++;
        }

        if (trorkSpawns.size() != playersPerTeam || kweebecSpawns.size() != playersPerTeam)
            logger.warning("ERROR: Amount of spawn points on some team isn't the same as the players per team set on the configuration.");
    }

    private void loadDefaultValues(boolean firstTime) {
        /*
        Load game default values.
         */
        gameState = GameState.WAITING;
        eventIndex = 0;
        eventTime = 30;
        remainingKweebecs = new ArrayList<>(kweebecNPCs);

        if (firstTime) {
            gameType = GameTypes.NORMAL;
            teamSelectorMenu = new MenuHandler(9 * 3, "Team Selector", "", null);
            main.getArenaSelectorMenu().addItem(getGameIcon());
        } else {
            updateGameIcon();
        }
        updateTeamSelector(firstTime);
    }

    private void relocateLocations(World world) {
        lobbyLocation.setWorld(world);
        spectatorLocation.setWorld(world);
        for (Location location : kweebecNPCSpawns) location.setWorld(world);
        for (Location location : kweebecSpawns) location.setWorld(world);
        for (Location location : trorkSpawns) location.setWorld(world);
        main.getLogger().info("Locations have been parsed to the newly created world.");
    }

    public void joinGame(Player player) {
        boolean isJoinable = gameState == GameState.WAITING || gameState == GameState.STARTING;

        if (playerList.contains(player)) {
            player.sendMessage("§cYou're already inside this game!");
            return;
        }

        if (isJoinable && playerList.size() < maxPlayers) {
            sendWelcomeMessage(player);
            playerList.add(player);
            sendGameMessage("&a" + player.getName() + " &ehas joined! (&b" + playerList.size() + "&e/&b" + maxPlayers + "&e)");
            main.setPlayerGame(player, this);

            playerManager.preparePlayer(player, GameMode.ADVENTURE);
            player.teleport(lobbyLocation);

            player.getInventory().setItem(0, main.getTeamSelector());
            player.getInventory().setItem(8, main.getArenaLeaveItem());

            player.sendTitle(StringUtility.format("&b&lSAVE THE KWEEBECS"),
                    "Special Thankmas Event", 10, 20, 10);

            if (playerList.size() >= minPlayers && gameState == GameState.WAITING)
                this.gameState = GameState.STARTING;

            playerManager.getGamePlayer(player).setBoard(this);

            for (Player players : playerList)
                playerManager.getGamePlayer(players).getPlayerBoard().set("Players: §a" + playerList.size() + "/" + maxPlayers, 3);
            updateGameIcon();
        } else if (isJoinable && playerList.size() >= maxPlayers) {
            player.sendMessage(StringUtility.format("&c&lSorry! &7This game is full! You can spectate once the game starts."));
        } else if (gameState == GameState.ENDING) {
            player.sendMessage(StringUtility.format("&c&lSorry! &7This game is already ending!"));
        } else {
            player.sendMessage(StringUtility.format("&c&lSorry! &7This game already started!"));
        }
    }

    public void leaveGame(Player player) {
        playerList.remove(player);
        kweebecPlayers.remove(player);
        trorkPlayers.remove(player);

        if (gameState == GameState.WAITING || gameState == GameState.STARTING) {
            sendGameMessage("&a" + player.getName() + " &ehas left! (&b" + playerList.size() + "&e/&b" + maxPlayers + "&e)");

            playerManager.sendToLobby(player);
            updateTeamSelector(false);
        } else if (gameState == GameState.INGAME) {
            if (trorkPlayers.size() == 0 || kweebecPlayers.size() == 0) {
                sendGameMessage("&c&lA team has been eliminated. Game will be restarting...");
                gameState = GameState.ENDING;
                updateGameIcon();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : getPlayerList()) {
                            playerManager.getGamePlayer(player).setBoard(getGame());

                            try {
                                main.getSkinsRestorerAPI().applySkin(new PlayerWrapper(player));
                            } catch (SkinRequestException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.runTaskAsynchronously(main);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        resetGame("Nobody");
                    }
                }.runTaskLater(main, 40L);
            } else {
                player.damage(player.getHealth());
            }
        }

        updateGameIcon();
        main.removePlayerGame(player);
    }

    public void updateGameIcon() {
        main.getArenaSelectorMenu().replaceItem(arenaSelectorItemCache, getGameIcon());
    }

    public Icon getGameIcon() {
        Icon icon = new Icon(new ItemBuilder((gameState == GameState.WAITING ? Material.GREEN_TERRACOTTA
                : gameState == GameState.STARTING ? Material.YELLOW_TERRACOTTA : gameState == GameState.INGAME ? Material.RED_TERRACOTTA : Material.BLACK_TERRACOTTA))
                .setName(gameState.getColor() + mapName)
                .setLore("&7Players: &f" + playerList.size() + "/" + maxPlayers,
                        "&7State: " + gameState.getColor() + gameState.getStateString(),
                        "&7Team Size: &f" + playersPerTeam,
                        "",
                        "&7Mode: " + gameType.getGameType().getTypeColor() + gameType.getGameType().getTypeName(),
                        "",
                        "&eClick to join!")
                .toItemStack())
                .addClickAction((player, type) -> {
                    if (gameState == GameState.WAITING || gameState == GameState.STARTING) {
                        joinGame(player);
                    } else if (gameState == GameState.INGAME) {
                        player.sendMessage("§cJoining started game...");
                    } else {
                        player.sendMessage("§c§lSorry! §cThat map is currently §bending or resetting§c...");
                    }
                });
        arenaSelectorItemCache = icon.itemStack;
        return icon;
    }

    public void respawnPlayer(Player player) {
        boolean isTrork = trorkPlayers.contains(player);

        player.teleport((isTrork ? trorkSpawns.get(trorkPlayers.indexOf(player)) : kweebecSpawns.get(kweebecPlayers.indexOf(player))));
        playerManager.preparePlayer(player, GameMode.SURVIVAL);

        if (isTrork) gameType.getGameType().getTrorkKit().give(player, false);
        else gameType.getGameType().getKweebecKit().give(player, false);

        player.getInventory().setHelmet(playerManager.getGamePlayer(player).getSelectedBanner().getBanner());
        player.sendMessage(StringUtility.format("&eYou just respawned!"));
    }

    private void sendWelcomeMessage(Player player) {
        player.sendMessage(StringUtility.format("&a&l============================================"));
        player.sendMessage("");
        StringUtility.sendCenteredMessage(player, "&a&lWelcome to Save The Kweebecs!");
        StringUtility.sendCenteredMessage(player, "&7&oThankmas Special");
        player.sendMessage("");
        StringUtility.sendCenteredMessage(player, "Choose between two teams and fight for your objective!");
        StringUtility.sendCenteredMessage(player, "Learn more on the &aTeam Selector&f!");
        player.sendMessage("");
        player.sendMessage(StringUtility.format("&a&l============================================"));
    }

    public void sendGameMessage(String message) {
        for (Player player : playerList) {
            player.sendMessage(StringUtility.format(message));
        }
    }

    public void sendTitleMessage(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : playerList) {
            player.sendTitle(StringUtility.format(title), StringUtility.format(subTitle), fadeIn, stay, fadeOut);
        }
    }

    public void sendActionbar(String message, List<Player> playerList) {
        TextComponent parsedMessage = new TextComponent(StringUtility.format(message));
        for (Player player : playerList) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, parsedMessage);
        }
    }

    public void sendSound(Sound sound) {
        for (Player player : playerList) {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    private ArrayList<Location> loadSpawnpoints(String configName, String formalName) {
        ArrayList<Location> locationList = new ArrayList<>();

        for (String string : main.getConfig().getStringList("games." + gameMap.getSlimeWorldName() + "." + configName)) {
            Location location = LocationUtil.getLocationByString(string, gameMap.getWorld());
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
            locationList.add(location);
        }
        main.getLogger().info("  > " + formalName + ": " + locationList.size());

        return locationList;
    }

    private void updateTeamSelector(boolean firstTime) {
        teamSelectorMenu.setIcon(11, new Icon(new ItemBuilder(Material.PLAYER_HEAD, 1)
                .setName("&aKweebecs")
                .setLoreWithWrap("&7Kweebecs have to rescue their Kweebec friends who are kidnapped on the trork lands!" +
                        "\n\n&7Players: &f" + kweebecPlayers.size() + "/" + playersPerTeam + "\n\n&eClick to join team!", 35)
                .setSkullTexture("http://textures.minecraft.net/texture/fa66479997b98874c65f6a08dbfb6c32380fed0aa27af58cb1c66cabed5da281")
                .toItemStack()).addClickAction((player, type) -> {
            if (kweebecPlayers.size() < playersPerTeam && !kweebecPlayers.contains(player)) {
                trorkPlayers.remove(player);
                kweebecPlayers.add(player);

                player.sendMessage(StringUtility.format("&eYou joined the &aKweebecs&e team!"));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                updateTeamSelector(false);
            } else if (kweebecPlayers.size() >= playersPerTeam) {
                player.sendMessage(StringUtility.format("&cThis team is full!"));
            } else {
                player.sendMessage(StringUtility.format("&cYou're already on this team!"));
            }
        }));

        if (firstTime) {
            teamSelectorMenu.setIcon(13, new Icon(new ItemBuilder(Material.STONE_BUTTON, 1)
                    .setName("&aRandom")
                    .setLoreWithWrap("&7Feeling crazy? Join a random team!" + "\n\n&eClick to select!", 35)
                    .toItemStack()).addClickAction((player, type) -> {
                trorkPlayers.remove(player);
                kweebecPlayers.remove(player);

                player.sendMessage(StringUtility.format("&eYou will be assigned a random &ateam &ewhen the game starts!"));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                updateTeamSelector(false);
            }));
        }

        teamSelectorMenu.setIcon(15, new Icon(new ItemBuilder(Material.PLAYER_HEAD, 1)
                .setName("&aTrorks")
                .setLoreWithWrap("&7Trorks have to defend their lands from the Kweebecs! Kidnapped Kweebecs are their only food." +
                        "\n\n&7Players: &f" + trorkPlayers.size() + "/" + playersPerTeam + "\n\n&eClick to join team!", 35)
                .setSkullTexture("http://textures.minecraft.net/texture/ea4f778cd8c8b8bf391e3943409f88afb41ae3d70a93ea74acd2f10123b7de46")
                .toItemStack()).addClickAction((player, type) -> {
            if (trorkPlayers.size() < playersPerTeam && !trorkPlayers.contains(player)) {
                kweebecPlayers.remove(player);
                trorkPlayers.add(player);

                player.sendMessage(StringUtility.format("&eYou joined the &aTrorks&e team!"));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                updateTeamSelector(false);
            } else if (trorkPlayers.size() >= playersPerTeam) {
                player.sendMessage(StringUtility.format("&cThis team is full!"));
            } else {
                player.sendMessage(StringUtility.format("&cYou're already on this team!"));
            }
        }));
    }

    public MenuHandler getTeamSelectorMenu() {
        return teamSelectorMenu;
    }

    public void save() {
        FileConfiguration configurationFile = main.getConfig();
        String configPath = "games." + mapName.toLowerCase().replaceAll(" ", "_") + ".";

        configurationFile.set(configPath + "mapName", mapName);
        configurationFile.set(configPath + "minPlayers", minPlayers);
        configurationFile.set(configPath + "maxPlayers", maxPlayers);
        configurationFile.set(configPath + "playersPerTeam", playersPerTeam);

        ArrayList<String> parsedKweebecSpawnpoints = new ArrayList<>();
        ArrayList<String> parsedKweebecNPCSpawnpoints = new ArrayList<>();
        ArrayList<String> parsedTrorkSpawnpoints = new ArrayList<>();

        for (Location location : kweebecSpawns)
            parsedKweebecSpawnpoints.add(LocationUtil.getStringByLocation(location));
        for (Location location : kweebecNPCSpawns)
            parsedKweebecNPCSpawnpoints.add(LocationUtil.getStringByLocation(location));
        for (Location location : trorkSpawns)
            parsedTrorkSpawnpoints.add(LocationUtil.getStringByLocation(location));

        configurationFile.set(configPath + "trorkSpawns", parsedTrorkSpawnpoints);
        configurationFile.set(configPath + "kweebecSpawns", parsedKweebecSpawnpoints);
        configurationFile.set(configPath + "kweebecNPCSpawns", parsedKweebecNPCSpawnpoints);

        configurationFile.set(configPath + "lobbyLocation", LocationUtil.getStringByLocation(lobbyLocation));
        configurationFile.set(configPath + "spectatorLocation", LocationUtil.getStringByLocation(spectatorLocation));

        main.saveConfig();
    }

    public void attemptStart() {
        if (playerList.size() >= minPlayers) {
            gameState = GameState.INGAME;
            eventTime = gameType.getGameType().getEventList().get(0).time;

            balanceTeams();
            startGame();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (NPC npc : kweebecNPCs)
                        npc.removeFromTabList(playerList);
                }
            }.runTaskLater(main, 30L);
        } else {
            gameState = GameState.WAITING;
            sendGameMessage("&cMore players are needed to start playing!");
            eventTime = 30;
            for (Player player : getPlayerList())
                main.getPlayerManager().getGamePlayer(player).getPlayerBoard().set("Waiting...", 6);
        }
        updateGameIcon();
    }

    private void balanceTeams() {
        Game game = this;

        for (Player players : new ArrayList<>(playerList)) {
            if (!trorkPlayers.contains(players) && !kweebecPlayers.contains(players)) {
                if (trorkPlayers.size() > kweebecPlayers.size()) {
                    kweebecPlayers.add(players);
                } else {
                    trorkPlayers.add(players);
                }
            }
        }

        int playersPerTeam = game.getPlayerList().size() / 2;
        int playersToRemove;
        int index;
        Player player;

        if (game.getTrorkPlayers().size() > playersPerTeam) {
            playersToRemove = game.getTrorkPlayers().size() - playersPerTeam;

            for (int i = 0; i < playersToRemove; i++) {
                index = game.getTrorkPlayers().size() - i - 1;
                player = game.getTrorkPlayers().get(index);
                player.sendMessage(StringUtility.format("&cYou have been moved to &bKweebecs&c team to balance the teams!"));
                game.getTrorkPlayers().remove(index);
                game.getKweebecPlayers().add(player);
            }
        } else if (game.getKweebecPlayers().size() > playersPerTeam) {
            playersToRemove = game.getKweebecPlayers().size() - playersPerTeam;
            for (int i = 0; i < playersToRemove; i++) {
                index = game.getKweebecPlayers().size() - i - 1;
                player = game.getKweebecPlayers().get(index);
                player.sendMessage(StringUtility.format("&cYou have been moved to &bTrorks&c team to balance the teams!"));
                game.getKweebecPlayers().remove(index);
                game.getTrorkPlayers().add(player);
            }
        }
    }

    private void startGame() {
        Game game = this;
        SkinsRestorerAPI skinsRestorerAPI = main.getSkinsRestorerAPI();

        int kweebecsSpawnIndex = 0;
        int trorksSpawnIndex = 0;

        for (Player player : playerList) {
            player.closeInventory();
            playerManager.getGamePlayer(player).setBoard(this);

            boolean isTrork = game.getTrorkPlayers().contains(player);

            player.sendMessage(StringUtility.format("&eThe game has started!"));
            playerManager.preparePlayer(player, GameMode.SURVIVAL);

            if (isTrork) {
                game.getGameType().getGameType().getTrorkKit().give(player, false);
                player.teleport(game.getTrorkSpawns().get(trorksSpawnIndex));
                player.sendTitle("§a§lGO!", "You're a Trork!", 10, 40, 10);
                player.sendMessage("");
                StringUtility.sendCenteredMessage(player, "&eYou are part of the &c&lTRORKS&e team.");
                StringUtility.sendCenteredMessage(player, "&eMake sure those Kweebecs don't get close to");
                StringUtility.sendCenteredMessage(player, "&eyour §bkidnapped §emeal!");
                player.sendMessage("");
                trorksSpawnIndex++;
            } else {
                game.getGameType().getGameType().getKweebecKit().give(player, false);
                player.teleport(game.getKweebecSpawns().get(kweebecsSpawnIndex));
                player.sendTitle("§a§lGO!", "You're a Kweebec!", 10, 40, 10);
                player.sendMessage("");
                StringUtility.sendCenteredMessage(player, "&eYou are part of the &a&lKWEEBECS&e team.");
                StringUtility.sendCenteredMessage(player, "&eRescue your kidnapped friends on the");
                StringUtility.sendCenteredMessage(player, "&cTrork's land§e and fight the Trorks!");
                player.sendMessage("");
                kweebecsSpawnIndex++;
            }

            for (NPC npc : kweebecNPCs) {
                npc.spawnNPC(player);
                npc.setNameTagVisibility(player, false);
            }

            player.getInventory().setHelmet(playerManager.getGamePlayer(player).getSelectedBanner().getBanner());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            loadTeamColors(player, isTrork);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : new ArrayList<>(game.getTrorkPlayers()))
                    skinsRestorerAPI.applySkin(new PlayerWrapper(player), main.getSkinManager().TRORK);
                for (Player player : new ArrayList<>(game.getKweebecPlayers()))
                    skinsRestorerAPI.applySkin(new PlayerWrapper(player), main.getSkinManager().KWEEBEC);
            }
        }.runTaskAsynchronously(main);
    }

    private void loadTeamColors(Player player, boolean isTrork) {
        Scoreboard scoreboard = player.getScoreboard();
        Team ownTeam = scoreboard.getTeam("own");
        Team enemyTeam = scoreboard.getTeam("enemy");

        if (ownTeam == null) ownTeam = scoreboard.registerNewTeam("own");
        if (enemyTeam == null) enemyTeam = scoreboard.registerNewTeam("enemy");

        ownTeam.setColor(org.bukkit.ChatColor.GREEN);
        enemyTeam.setColor(org.bukkit.ChatColor.RED);

        Objective health = scoreboard.getObjective("showHealth");

        if (health == null)
            health = scoreboard.registerNewObjective("showHealth", "health", ChatColor.RED + "❤");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        for (Player players : playerList) {
            if (isTrork) {
                if (trorkPlayers.contains(players)) ownTeam.addEntry(players.getName());
                else enemyTeam.addEntry(players.getName());
            } else {
                if (kweebecPlayers.contains(players)) ownTeam.addEntry(players.getName());
                else enemyTeam.addEntry(players.getName());
            }
        }
    }

    public void saveKweebec(NPC npc, Player saviour) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = npc.getLocation();
                npc.destroyNPC(playerList);
                remainingKweebecs.remove(npc);
                int savedKweebecs = kweebecNPCs.size() - remainingKweebecs.size();

                new InstantFirework(FireworkEffect.builder().withColor(Color.GREEN, Color.ORANGE).flicker(true).trail(true).withFade(Color.RED).build(), location);
                sendGameMessage("&aA &akweebec&e has been saved by &6" + saviour.getName() + "&e! " + (savedKweebecs == kweebecNPCs.size() ? "&a" : savedKweebecs >= (kweebecNPCs.size() / 2) ? "&6" : "&c") + "(" + savedKweebecs + "/" + kweebecNPCs.size() + ")");
                sendActionbar("&bKweebecs &care being saved! &lGo back to base!", trorkPlayers);

                GamePlayer saviourGP = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(saviour);
                saviourGP.setKweebecsSaved(saviourGP.getKweebecsSaved() + 1);

                for (Player player : new ArrayList<>(playerList))
                    playerManager.getGamePlayer(player).getPlayerBoard().set("Saved: §a" + getSavedKweebecs() + "/" + getKweebecNPCs().size(), 9);

                if (savedKweebecs == kweebecNPCs.size()) {
                    SkinsRestorerAPI skinsRestorerAPI = main.getSkinsRestorerAPI();

                    setGameState(GameState.ENDING);

                    for (Player player : new ArrayList<>(playerList)) {
                        player.sendMessage("§aKweebecs§e have won the game! All the Kweebecs were saved in time!");
                        player.sendTitle("§a§lKWEEBECS WON", null, 10, 40, 10);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    updateGameIcon();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player player : new ArrayList<>(getPlayerList())) {
                                playerManager.getGamePlayer(player).setBoard(getGame());

                                try {
                                    skinsRestorerAPI.applySkin(new PlayerWrapper(player));
                                } catch (SkinRequestException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.runTaskAsynchronously(main);

                    new BukkitRunnable() {
                        int counter = 0;
                        final FireworkEffect kweebecWinEffect = FireworkEffect.builder().withColor(Color.GREEN, Color.YELLOW, Color.ORANGE, Color.AQUA).withFade(Color.FUCHSIA).build();

                        @Override
                        public void run() {
                            if (counter == 8) {
                                cancel();
                                resetGame("Kweebecs");
                            } else {
                                new InstantFirework(kweebecWinEffect, getSpectatorLocation());
                            }
                            counter++;
                        }
                    }.runTaskTimer(main, 0L, 15L);
                }
            }
        }.runTask(main);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<Player> getKweebecPlayers() {
        return kweebecPlayers;
    }

    public List<Player> getTrorkPlayers() {
        return trorkPlayers;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Location getSpectatorLocation() {
        return spectatorLocation;
    }

    public void setSpectatorLocation(Location spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    public List<Location> getTrorkSpawns() {
        return trorkSpawns;
    }

    public List<Location> getKweebecSpawns() {
        return kweebecSpawns;
    }

    public List<NPC> getRemainingKweebecs() {
        return remainingKweebecs;
    }

    public List<Location> getKweebecNPCSpawns() {
        return kweebecNPCSpawns;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public void setPlayersPerTeam(int playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    public void setEventIndex(int eventIndex) {
        this.eventIndex = eventIndex;
    }

    public int getEventTime() {
        return eventTime;
    }

    public void setEventTime(int eventTime) {
        this.eventTime = eventTime;
    }

    public Game getGame() {
        return this;
    }

    public int getSavedKweebecs() {
        return kweebecNPCs.size() - remainingKweebecs.size();
    }

    public GameTypes getGameType() {
        return gameType;
    }

    public List<NPC> getKweebecNPCs() {
        return kweebecNPCs;
    }

    public void remove() {
        Bukkit.unloadWorld(uuid.toString(), false);
    }
}
