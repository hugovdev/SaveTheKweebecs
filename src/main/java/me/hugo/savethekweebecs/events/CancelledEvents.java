package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CancelledEvents implements Listener {

    SaveTheKweebecs main;

    public CancelledEvents(SaveTheKweebecs main) {
        this.main = main;
    }

    @EventHandler
    public void listPing(ServerListPingEvent event) {
        event.setMotd(ColorUtil.createGradFromStrings("Save The Kweebecs", false, "12B8D3", "4570D3") + "§a - §b" + SaveTheKweebecs.getPlugin().getGames().size() + " §agames available.\n" +
                "§b" + Bukkit.getOnlinePlayers().size() + " §aplayers playing.");
        event.setMaxPlayers(0);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        Game game = SaveTheKweebecs.getPlugin().getPlayerGame(player);

        if (game == null || game.getGameState() != GameState.INGAME)
            event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile instanceof Arrow) {
            projectile.remove();
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockBurnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Game game = main.getPlayerGame(player);

        if (game == null || game.getGameState() != GameState.INGAME)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (main.getPlayerManager().getGamePlayer(event.getPlayer()).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent event) {
        if(event.getLocation().getWorld().getName().equalsIgnoreCase("creative")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (main.getPlayerManager().getGamePlayer(player).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
            Game game = main.getPlayerGame(player);

            if (game == null || game.getGameState() != GameState.INGAME)
                event.setCancelled(true);
            else if (game.getGameState() == GameState.INGAME) {
                if (event.getBlock().getType().name().contains("FENCE") && player.getInventory().getChestplate().getItemMeta().getDisplayName().toLowerCase().contains("kweebec"))
                    event.setCancelled(false);
                else
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        World worldfrom = event.getFrom();

        for (Player p : world.getPlayers()) {
            p.showPlayer(main, player);
            player.showPlayer(main, p);
        }
        for (Player p : worldfrom.getPlayers()) {
            p.hidePlayer(main, player);
            player.hidePlayer(main, p);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (main.getPlayerManager().getGamePlayer(event.getPlayer()).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (main.getPlayerManager().getGamePlayer((Player) event.getEntity()).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (main.getPlayerManager().getGamePlayer(player).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
            Game game = main.getPlayerGame(player);
            if (game == null || game.getGameState() != GameState.INGAME)
                event.setCancelled(true);
            else {
                if (event.getSlotType() == InventoryType.SlotType.ARMOR)
                    event.setCancelled(true);
            }
        }
    }

}
