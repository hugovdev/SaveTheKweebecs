package me.hugo.savethekweebecs.player;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.cosmetics.Suit;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private HashMap<UUID, GamePlayer> gamePlayers;

    public final ItemStack SUIT_MENU_INDICATOR;
    public final ItemStack BANNER_MENU_INDICATOR;
    public final ItemStack SHOP_MENU_INDICATOR;

    public PlayerManager() {
        this.gamePlayers = new HashMap<>();
        this.SUIT_MENU_INDICATOR = new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aSuit Wardrobe").setLoreWithWrap("§7Select a suit and wear it on the lobby! Represent your favorite creatures, community, etc.", 35).hideEnch().toItemStack();
        this.BANNER_MENU_INDICATOR = new ItemBuilder(Material.RED_BANNER).setName("§aBanner Wardrobe").setLoreWithWrap("§7Select a banner and wear it in battle! Represent your country, community, etc.", 35).toItemStack();
        this.SHOP_MENU_INDICATOR = new ItemBuilder(Material.GOLD_NUGGET).setName("§aShop").setLoreWithWrap("§7Buy special items with abilities with gold. Get gold by killing enemies.", 35).toItemStack();
    }

    public GamePlayer getGamePlayer(Player player) {
        GamePlayer gamePlayer = gamePlayers.get(player.getUniqueId());

        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(player);
            gamePlayers.put(player.getUniqueId(), gamePlayer);
        }

        return gamePlayer;
    }

    public Collection<GamePlayer> getGamePlayers() {
        return gamePlayers.values();
    }

    public void removeGamePlayer(Player player) {
        gamePlayers.remove(player.getUniqueId());
    }

    public void sendToLobby(Player player) {
        SaveTheKweebecs.getPlugin().removePlayerGame(player);
        getGamePlayer(player).setBoard(null);
        preparePlayer(player, GameMode.ADVENTURE);
        player.getInventory().setItem(0, SaveTheKweebecs.getPlugin().getArenaSelector());
        player.getInventory().setItem(8, SaveTheKweebecs.getPlugin().getGameSelector());
        player.getInventory().setItem(4, SaveTheKweebecs.getPlugin().getSuitSelector());

        GamePlayer gamePlayer = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player);

        /*
        Reset game stats. No permanent stats.
         */
        gamePlayer.setKills(0);
        gamePlayer.setGold(0);
        gamePlayer.setKweebecsSaved(0);
        gamePlayer.setTotalGoldSpent(0);
        gamePlayer.setDeaths(0);

        gamePlayer.giveBannerSelectorItem(player);
        if (gamePlayer.getSuit() != Suit.NONE) gamePlayer.getSuit().equip(player);

        player.teleport(SaveTheKweebecs.getPlugin().getMainLobby());

        removeScoreboardEntries(player);
    }

    private void removeScoreboardEntries(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team ownTeam = scoreboard.getTeam("own");
        Team enemyTeam = scoreboard.getTeam("enemy");

        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);

        if (ownTeam != null)
            for (String entry : ownTeam.getEntries()) ownTeam.removeEntry(entry);
        if (enemyTeam != null)
            for (String entry : enemyTeam.getEntries()) enemyTeam.removeEntry(entry);
    }

    public void preparePlayer(Player player, GameMode gameMode) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);

        if (gameMode != GameMode.CREATIVE) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        player.setExp(0.0f);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setWalkSpeed(0.2f);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setHeldItemSlot(0);

        for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());

        player.setGameMode(gameMode);
        player.updateInventory();
    }

}
