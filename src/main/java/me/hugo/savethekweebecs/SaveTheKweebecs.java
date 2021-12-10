package me.hugo.savethekweebecs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.inject.Injector;
import com.grinderwolf.swm.api.SlimePlugin;
import me.hugo.savethekweebecs.commands.*;
import me.hugo.savethekweebecs.events.*;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameMap;
import me.hugo.savethekweebecs.game.WorldPropertyManager;
import me.hugo.savethekweebecs.game.kits.KitManager;
import me.hugo.savethekweebecs.game.specialitems.AbilityManager;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.PlayerManager;
import me.hugo.savethekweebecs.schedule.GameControllerTask;
import me.hugo.savethekweebecs.schedule.ScoreboardAnimationTask;
import me.hugo.savethekweebecs.utils.*;
import me.hugo.savethekweebecs.utils.gui.MenuHandler;
import me.hugo.savethekweebecs.utils.gui.PaginatedGUI;
import me.hugo.savethekweebecs.utils.npc.NPC;
import me.hugo.savethekweebecs.utils.npc.NPCManager;
import me.hugo.savethekweebecs.utils.skins.SkinManager;
import net.skinsrestorer.api.SkinsRestorerAPI;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SaveTheKweebecs extends JavaPlugin {

    private static SaveTheKweebecs main;
    private HashMap<UUID, Game> playerGame = new HashMap<>();

    private GlobalGame defaultGame;

    private ItemStack teamSelector;
    private ItemStack spectatorBrowser;
    private ItemStack arenaSelector;
    private ItemStack arenaCreator;
    private ItemStack arenaLeaveItem;
    private ItemStack gameSelector;
    private ItemStack suitSelector;

    private KitManager kitManager;
    private PlayerManager playerManager;
    private NPCManager npcManager;
    private SkinManager skinManager;
    private AbilityManager abilityManager;
    private WorldPropertyManager worldPropertyManager;

    private BossBar bossBar;

    private SlimePlugin slimePlugin;

    private Location mainLobby;

    private SkinsRestorerAPI skinsRestorerAPI;

    private ArrayList<Game> games = new ArrayList<>();
    private HashMap<String, GameMap> gameMaps = new HashMap<>();
    private HashMap<NPC, Long> npcInteractionCooldown = new HashMap<>();

    private MenuHandler arenaSetupMenu;
    private PaginatedGUI arenaSelectorMenu;

    @Override
    public void onEnable() {
        this.main = this;
        SaveTheKweebecsModule module = new SaveTheKweebecsModule(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        this.defaultGame = GlobalGame.SAVE_THE_KWEEBECS;
        this.kitManager = new KitManager();
        this.playerManager = new PlayerManager();
        this.npcManager = new NPCManager();
        this.skinsRestorerAPI = SkinsRestorerAPI.getApi();
        this.abilityManager = new AbilityManager();
        this.worldPropertyManager = new WorldPropertyManager();
        this.skinManager = new SkinManager();

        this.bossBar = Bukkit.createBossBar("§e§lHYTALE THANKMAS", BarColor.YELLOW, BarStyle.SEGMENTED_6);

        slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        loadGameItems();
        getPlugin().saveDefaultConfig();

        List<String> gameNames = new ArrayList<>();
        boolean gamesConfigured = true;

        try {
            for (String string : getConfig().getConfigurationSection("games").getKeys(false))
                gameNames.add(string);
        } catch (NullPointerException e) {
            gamesConfigured = false;
            getLogger().info("No games have been configured.");
        }

        arenaSelectorMenu = new PaginatedGUI(9 * 3, new ItemBuilder(Material.PAPER).setName("&aArena Selector")
                .setLore("&7Click on map you want to play on!").toItemStack(), "Arena Selector", PaginatedGUI.PageFormat.ONE_ROW, null);

        for (String slimeMapName : gameNames)
            gameMaps.put(slimeMapName, new GameMap(slimeMapName));

        if (!gamesConfigured) {
            getLogger().info("No games have been loaded.");
            arenaSetupMenu = new MenuHandler(9 * 6, "Arena Setup Menu", "", null);
            getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
            getServer().getPluginManager().registerEvents(new ArenaSetup(), this);
        } else {
            /*
            Load listeners
             */
            getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
            getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
            getServer().getPluginManager().registerEvents(new CancelledEvents(this), this);
            getServer().getPluginManager().registerEvents(new DamageEvents(this), this);
            getServer().getPluginManager().registerEvents(new ChatEvent(), this);
            getServer().getPluginManager().registerEvents(new SpecialItemEvents(), this);

            /*
            Load commands
             */

            EnableGameCommand enableGameCommand = new EnableGameCommand();
            DefaultGameCommand defaultGameCommand = new DefaultGameCommand();
            DisableGameCommand disableGameCommand = new DisableGameCommand();

            getCommand("defaultgame").setExecutor(defaultGameCommand);
            getCommand("defaultgame").setTabCompleter(defaultGameCommand);

            getCommand("enablegame").setExecutor(enableGameCommand);
            getCommand("enablegame").setTabCompleter(enableGameCommand);

            getCommand("disablegame").setExecutor(disableGameCommand);
            getCommand("disablegame").setTabCompleter(disableGameCommand);

            getCommand("games").setExecutor(new GamesCommand());
            getCommand("stk").setExecutor(new SaveTheKweebecsCommand(this));

            mainLobby = LocationUtil.getLocationByString(getConfig().getString("mainLobbyLocation"), Bukkit.getWorld("world"));

            loadKweebecNPCHandler();

            /*
            Load creative world
             */
            World world = Bukkit.createWorld(new WorldCreator("creative").type(WorldType.FLAT).generateStructures(false));
            world.setSpawnLocation(new Location(world, 0.5, 10, 0.5));

            world.setTime(0);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

            WorldBorder wb = world.getWorldBorder();
            wb.setCenter(0, 0);
            wb.setSize(350);

            new GameControllerTask(this).runTaskTimer(this, 0L, 20L);
            new ScoreboardAnimationTask("THANKMAS").runTaskTimerAsynchronously(this, 0L, 1L);
        }
    }

    private void loadKweebecNPCHandler() {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGH, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                Player player = e.getPlayer();
                int entityId = e.getPacket().getIntegers().read(0);
                NPC npc = getNpcManager().getNPC(entityId);

                Long lastClickTime = npcInteractionCooldown.get(npc);

                if (lastClickTime == null || (System.currentTimeMillis() - lastClickTime) / 1000 > 1) {
                    Game game = getPlayerGame(player);

                    if (npc != null && game != null && player.getGameMode() != GameMode.SPECTATOR) {
                        if (player.getInventory().getChestplate().getItemMeta().getDisplayName().toLowerCase().contains("kweebec")) {
                            npcInteractionCooldown.put(npc, System.currentTimeMillis());
                            game.saveKweebec(npc, player);
                        } else
                            player.sendMessage(StringUtility.format("&cProtect this Kweebec from the opposite team! They are &byour meal &cand it is not done yet!"));
                    }
                }
            }
        });
    }

    public GameMap getMap(String mapName) {
        return gameMaps.get(mapName);
    }

    public void setPlayerGame(Player player, Game game) {
        playerGame.put(player.getUniqueId(), game);
    }

    public void removePlayerGame(Player player) {
        playerGame.remove(player.getUniqueId());
    }

    public Game getPlayerGame(Player player) {
        return playerGame.get(player.getUniqueId());
    }

    private void loadGameItems() {
        this.arenaSelector = new ItemBuilder(Material.PAPER).setName("&aMap Selector &7(Right Click)")
                .setLoreWithWrap("&7Open a menu which lets you join maps and games to play or watch!\n\n&eClick to open!", 35).toItemStack();

        this.suitSelector = new ItemBuilder(Material.CHEST).setName("&aSuit Selector &7(Right Click)")
                .setLoreWithWrap("&7Select suits from people on the community and disguise yourself!\n\n&eClick to open!", 35).toItemStack();

        this.gameSelector = new ItemBuilder(Material.NETHER_STAR).setName("&aGame Selector &7(Right Click)")
                .setLoreWithWrap("&7Open a menu which lets you choose the game you want to play!\n\n&eClick to open!", 35).toItemStack();

        this.teamSelector = new ItemBuilder(Material.NETHER_STAR).setName(ColorUtil.createGradFromStrings("Team Selector", false, "12B8D3", "4570D3") + " &7(Right Click)")
                .setLoreWithWrap("&7Choose the team you want to play in! Trork or Kweebec.\n\n" +
                        "&7Trorks defend captured kweebecs from the trork campfire or castle!\n\n" +
                        "&7Kweebecs have to rescue their beloved friends from those mighty trorks!" +
                        "\n\n&eClick to select team!", 35).toItemStack();

        this.spectatorBrowser = new ItemBuilder(Material.COMPASS).setName("&aBrowse People &7(Click)")
                .setLoreWithWrap("&7Choose the person you want to spectate!" +
                        "\n\n&eClick to open!", 35).toItemStack();

        this.arenaCreator = new ItemBuilder(Material.CLOCK).setName("&aArena Creator &7(Right Click)")
                .setLoreWithWrap("&7Open a menu that lets you set up an arena." +
                        "\n\n&eClick to open!", 35).toItemStack();

        this.arenaLeaveItem = new ItemBuilder(Material.RED_BED).setName("&cBack to Lobby &7(Right Click)").toItemStack();
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public HashMap<String, GameMap> getGameMaps() {
        return gameMaps;
    }

    public GlobalGame getDefaultGame() {
        return defaultGame;
    }

    public void setDefaultGame(GlobalGame defaultGame) {
        this.defaultGame = defaultGame;
    }

    public ItemStack getTeamSelector() {
        return teamSelector;
    }

    public ItemStack getSuitSelector() {
        return suitSelector;
    }

    public ItemStack getSpectatorBrowser() {
        return spectatorBrowser;
    }

    public ItemStack getArenaCreator() {
        return arenaCreator;
    }

    public MenuHandler getArenaSetupMenu() {
        return arenaSetupMenu;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ItemStack getArenaSelector() {
        return arenaSelector;
    }

    public static SaveTheKweebecs getPlugin() {
        return main;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public ItemStack getArenaLeaveItem() {
        return arenaLeaveItem;
    }

    public SkinsRestorerAPI getSkinsRestorerAPI() {
        return skinsRestorerAPI;
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

    public PaginatedGUI getArenaSelectorMenu() {
        return arenaSelectorMenu;
    }

    public WorldPropertyManager getWorldPropertyManager() {
        return worldPropertyManager;
    }

    public ItemStack getGameSelector() {
        return gameSelector;
    }

    public void setWorldPropertyManager(WorldPropertyManager worldPropertyManager) {
        this.worldPropertyManager = worldPropertyManager;
    }

    public void setMainLobby(Location mainLobby) {
        this.mainLobby = mainLobby;
    }

    public Location getMainLobby() {
        return mainLobby;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}
