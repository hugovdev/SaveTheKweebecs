package me.hugo.savethekweebecs.player;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.cosmetics.BannerCosmetic;
import me.hugo.savethekweebecs.cosmetics.Suit;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.game.gametype.GameType;
import me.hugo.savethekweebecs.game.specialitems.SpecialItem;
import me.hugo.savethekweebecs.game.specialitems.SpecialItemAction;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.utils.ColorUtil;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.StringUtility;
import me.hugo.savethekweebecs.utils.gui.ClickAction;
import me.hugo.savethekweebecs.utils.gui.Icon;
import me.hugo.savethekweebecs.utils.gui.MenuHandler;
import me.hugo.savethekweebecs.utils.gui.PaginatedGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class GamePlayer {

    BPlayerBoard playerBoard;
    Player player;

    int kills;
    int kweebecsSaved;
    int totalGoldSpent;
    int deaths;

    int gold;

    UUID lastAttackedPlayer;
    long lastAttackTime;

    ItemStack lastBannerItem;
    ItemStack lastSuitItem;

    Suit suit;
    BannerCosmetic selectedBanner;

    PaginatedGUI bannerMenu;
    PaginatedGUI suitMenu;

    MenuHandler gameSelectorMenu;
    GlobalGame currentGame;

    HashMap<String, Long> cooldowns;

    public GamePlayer(Player player) {
        this.player = player;
        this.playerBoard = Netherboard.instance().createBoard(player, "§b§l" + "SAVE THE KWEEBECS");
        this.cooldowns = new HashMap<>();

        this.kills = 0;
        this.kweebecsSaved = 0;
        this.totalGoldSpent = 0;
        this.deaths = 0;

        this.gold = 0;
        this.selectedBanner = BannerCosmetic.NONE;
        this.suit = Suit.NONE;

        this.bannerMenu = new PaginatedGUI(9 * 4, SaveTheKweebecs.getPlugin().getPlayerManager().BANNER_MENU_INDICATOR, "Banner Wardrobe", PaginatedGUI.PageFormat.TWO_ROWS, null);
        this.suitMenu = new PaginatedGUI(9 * 5, SaveTheKweebecs.getPlugin().getPlayerManager().SUIT_MENU_INDICATOR, "Suit Wardrobe", PaginatedGUI.PageFormat.ONE_ROW_WITHOUT_SIDES, null);

        for (BannerCosmetic banner : BannerCosmetic.values()) this.bannerMenu.addItem(banner.getIcon(this));
        
        for (Suit suit : Suit.values()) {
            Icon suitIcon = (this.suit == suit ? suit.SELECTED_ICON : suit.NOT_SELECTED_ICON);
            Icon toggleIcon = (this.suit == suit ? suit.SELECTED_TOGGLE : suit.NOT_SELECTED_TOGGLE);

            int[] i = this.suitMenu.addItem(suitIcon);
            this.suitMenu.setItem(toggleIcon, i[0], i[1] + 9);
        }

        this.currentGame = SaveTheKweebecs.getPlugin().getDefaultGame();

        this.gameSelectorMenu = new MenuHandler(9 * 3, "Game Selector", "", null);
        updateGameSelector();
    }

    public void updateGameSelector() {
        for (GlobalGame globalGame : GlobalGame.values()) {
            gameSelectorMenu.setIcon(globalGame.getSlot(), new Icon(new ItemBuilder(globalGame.getIcon())
                    .setName((globalGame.isOpen() ? (this.currentGame == globalGame ? "&c" : "&a") : "&c") + globalGame.getName())
                    .hideEnch()
                    .setLoreWithWrap("&7" + globalGame.getDescription() + "\n\n" +
                            (this.currentGame == globalGame ? "&cYou're already here!" : (globalGame.isOpen() ? "&eClick to go!" : "&cGame is currently closed!")), 30).toItemStack())
                    .addClickAction((player, type) -> {
                        if (SaveTheKweebecs.getPlugin().getPlayerGame(player) != null) {
                            player.sendMessage("§cPlease leave the §bmap§c you're on to join another §bgame§c!");
                        } else {
                            if (!globalGame.isOpen()) {
                                player.sendMessage("§cThis game is currently closed! Come back later!");
                            } else {
                                if (this.currentGame != globalGame) {
                                    this.currentGame = globalGame;
                                    updateGameSelector();
                                    globalGame.getClickAction().execute(player, type);
                                } else {
                                    player.sendMessage("§cYou're already playing §b" + globalGame.getName() + "§c!");
                                }
                            }
                        }
                    }));
        }
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public void giveBannerSelectorItem(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        playerInventory.setItem(1,
                new ItemBuilder((selectedBanner == null || selectedBanner == BannerCosmetic.NONE ? new ItemStack(Material.RED_BANNER) : selectedBanner.getBanner()))
                        .setName("§aBanner Wardrobe §7(Right Click)")
                        .hideEnch()
                        .setLoreWithWrap("&7Choose from a wide variety of banners which one you want to wear on battle!\n\n" +
                                "&eClick to open!", 35)
                        .toItemStack()
        );
    }

    public void setCurrentGame(GlobalGame currentGame) {
        this.currentGame = currentGame;
    }

    public GlobalGame getCurrentGame() {
        return currentGame;
    }

    public void openShopMenu(Player player) {
        PaginatedGUI shopMenu = new PaginatedGUI(9 * 6, SaveTheKweebecs.getPlugin().getPlayerManager().SHOP_MENU_INDICATOR, "Item Shop", PaginatedGUI.PageFormat.THREE_ROWS_WITHOUT_SIDES, null);

        for (SpecialItem specialItem : SpecialItem.values()) {
            shopMenu.addItem(specialItem.getShopIcon(this));
        }

        shopMenu.open(player);
    }

    public boolean isOnCooldown(String cooldownName) {
        return cooldowns.get(cooldownName) == null || (cooldowns.get(cooldownName) - System.currentTimeMillis()) / 1000 <= 0;
    }

    public int getRemainingCooldown(String cooldownName) {
        return (int) (cooldowns.get(cooldownName) - System.currentTimeMillis()) / 1000;
    }

    public void setCooldown(String cooldownName, int cooldownTime) {
        cooldowns.put(cooldownName, System.currentTimeMillis() + cooldownTime * 1000);
    }

    public Player getPlayer() {
        return player;
    }

    public void setBoard(Game game) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (game == null) {
            playerBoard.setAll("§7Lobby " + dateFormat.format(new Date()),
                    "§e",
                    "Welcome to",
                    "Hytale Thankmas!",
                    "§c",
                    "Lobby: §a#1",
                    "§a",
                    "Players: §a" + Bukkit.getOnlinePlayers().size(),
                    "§b",
                    "§eDonate to charity!");
            return;
        }

        GameState gameState = game.getGameState();
        GameType gameType = game.getGameType().getGameType();

        if (gameState == GameState.WAITING || gameState == GameState.STARTING) {
            playerBoard.setAll("§7Teams " + dateFormat.format(new Date()),
                    "§c",
                    "Mode: " + gameType.getTypeColor() + gameType.getTypeName(),
                    "§r",
                    gameState == GameState.WAITING ? "Waiting..." : ("Starting in §a" + game.getEventTime() + "s"),
                    "§6",
                    "Map: §a" + game.getMapName(),
                    "Players: §a" + game.getPlayerList().size() + "/" + game.getMaxPlayers(),
                    "§b",
                    "§eDonate to charity!");
        } else if (gameState == GameState.INGAME) {
            playerBoard.setAll("§7Teams " + dateFormat.format(new Date()),
                    "§a",
                    game.getGameType().getGameType().getEventList().get(game.getEventIndex()).name + " §a" + StringUtility.getDurationString(game.getEventTime()),
                    "§6",
                    "Saved: §a" + game.getSavedKweebecs() + "/" + game.getKweebecNPCs().size(),
                    "Kills: §a" + this.kills,
                    "§o",
                    "Gold: §e" + this.gold + " ⛃",
                    "§u",
                    "Map: §a" + game.getMapName(),
                    "Mode: " + gameType.getTypeColor() + gameType.getTypeName(),
                    "§8",
                    "§eDonate to charity!");
        } else if (gameState == GameState.ENDING) {
            playerBoard.setAll("§7Teams " + dateFormat.format(new Date()),
                    "§c",
                    "Trorks: §a" + game.getTrorkPlayers().size(),
                    "Kweebecs: §a" + game.getKweebecPlayers().size(),
                    "§a",
                    "Game finished!",
                    "§6",
                    "Map: §a" + game.getMapName(),
                    "§u",
                    "§eDonate to charity!");
        }
    }

    public BPlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public void addKill(Player player) {
        kills++;
        gold = gold + 5;
        playerBoard.set("Kills: §a" + this.kills, 8);
        playerBoard.set("Gold: §e" + this.gold + " ⛃", 6);
        player.sendMessage("§e+§l5§e gold!");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

    public BannerCosmetic getSelectedBanner() {
        return selectedBanner;
    }

    public void setSelectedBanner(BannerCosmetic selectedBanner) {
        this.selectedBanner = selectedBanner;
    }

    public void updateLastBannerItem() {
        lastBannerItem = selectedBanner.getIcon(this).itemStack;
    }

    public void updateLastSuitItem(Icon newIcon) {
        lastSuitItem = newIcon.itemStack;
    }

    public ItemStack getLastBannerItem() {
        return lastBannerItem;
    }

    public void setLastBannerItem(ItemStack lastBannerItem) {
        this.lastBannerItem = lastBannerItem;
    }

    public MenuHandler getGameSelectorMenu() {
        return gameSelectorMenu;
    }

    public PaginatedGUI getSuitMenu() {
        return suitMenu;
    }

    public PaginatedGUI getBannerMenu() {
        return bannerMenu;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getKweebecsSaved() {
        return kweebecsSaved;
    }

    public void setKweebecsSaved(int kweebecsSaved) {
        this.kweebecsSaved = kweebecsSaved;
    }

    public int getTotalGoldSpent() {
        return totalGoldSpent;
    }

    public void setTotalGoldSpent(int totalGoldSpent) {
        this.totalGoldSpent = totalGoldSpent;
    }

    public UUID getLastAttackedPlayer() {
        return lastAttackedPlayer;
    }

    public void setLastAttackedPlayer(UUID lastAttackedPlayer) {
        this.lastAttackedPlayer = lastAttackedPlayer;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }
}
