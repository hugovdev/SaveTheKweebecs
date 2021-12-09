package me.hugo.savethekweebecs.events;

import com.google.inject.Inject;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ColorUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class DamageEvents implements Listener {

    @Inject
    private SaveTheKweebecs main;

    @Inject
    public DamageEvents(SaveTheKweebecs main) {
        this.main = main;
    }

    private void killPlayer(Game game, Player attacked, Player attacker) {
        GamePlayer gamePlayer = main.getPlayerManager().getGamePlayer(attacked);

        gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);

        boolean isAttackedTrork = game.getTrorkPlayers().contains(attacked);
        String attackedGradientName = ColorUtil.createGradFromStrings(attacked.getName(), false, (isAttackedTrork ? "51EAEF" : "F9C80D"), (isAttackedTrork ? "5893EB" : "E49817"));

        if (attacker != null && attacker.isOnline()) {
            GamePlayer lastAttackerProfile = main.getPlayerManager().getGamePlayer(attacker);
            lastAttackerProfile.addKill(attacker);

            boolean isAttackerTrork = game.getTrorkPlayers().contains(attacker);
            String attackerGradientName = ColorUtil.createGradFromStrings(attacker.getName(), false, (isAttackerTrork ? "51EAEF" : "F9C80D"), (isAttackerTrork ? "5893EB" : "E49817"));

            game.sendGameMessage(attackerGradientName + " §ehas killed " + attackedGradientName + "§e!");

            attacked.getLocation().getWorld().playEffect(attacked.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            attacked.getLocation().getWorld().playEffect(attacked.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        } else {
            game.sendGameMessage(attackedGradientName + "§e died!");
        }

        respawnPlayer(attacked, game);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.DROPPED_ITEM) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Game game = main.getPlayerGame(player);

        if (game != null && game.getGameState() == GameState.INGAME && player.getHealth() - event.getFinalDamage() <= 0.0) {
            /*
            Player would be dying
             */
            event.setCancelled(true);
            Player lastAttacker = null;
            GamePlayer gamePlayer = main.getPlayerManager().getGamePlayer(player);
            UUID lastAttackerUUID = gamePlayer.getLastAttackedPlayer();

            if (lastAttackerUUID != null && (System.currentTimeMillis() - gamePlayer.getLastAttackTime()) / 1000 <= 10)
                lastAttacker = Bukkit.getPlayer(lastAttackerUUID);

            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
                    event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                    event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
                    event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                return;

            killPlayer(game, player, lastAttacker);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity attacked = event.getEntity();
        Entity attacker = event.getDamager();

        if (!(attacked instanceof Player)) return;
        Player player = (Player) attacked;
        Game game = main.getPlayerGame(player);

        if (!(attacker instanceof Player) && attacker instanceof Projectile && ((Projectile) attacker).getShooter() instanceof Player) {
            Player shooter = (Player) ((Projectile) attacker).getShooter();
            attacker = shooter;
        }

        if (game != null && game.getGameState() == GameState.INGAME && attacker instanceof Player && attacked instanceof Player && attacked != attacker) {
            ItemStack attackerChestplate = ((Player) attacker).getInventory().getChestplate();

            if (attackerChestplate == null || attackerChestplate.getItemMeta() == null || attackerChestplate.getItemMeta().getDisplayName() == null ||
                    attackerChestplate.getItemMeta().getDisplayName().equals(((Player) attacked).getInventory().getChestplate().getItemMeta().getDisplayName())) {
                event.setCancelled(true);
                return;
            }

            GamePlayer gamePlayer = main.getPlayerManager().getGamePlayer(player);
            gamePlayer.setLastAttackedPlayer(attacker.getUniqueId());
            gamePlayer.setLastAttackTime(System.currentTimeMillis());

            if (player.getHealth() - event.getFinalDamage() <= 0.0) {
            /*
            Player would be dying
             */
                event.setCancelled(true);
                Player lastAttacker = null;
                UUID lastAttackerUUID = gamePlayer.getLastAttackedPlayer();

                if (lastAttackerUUID != null && (System.currentTimeMillis() - gamePlayer.getLastAttackTime()) / 1000 <= 10)
                    lastAttacker = Bukkit.getPlayer(lastAttackerUUID);
                killPlayer(game, player, lastAttacker);
            }
        }
    }

    private void respawnPlayer(Player player, Game game) {
        if (player.isOnline()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.closeInventory();

            new BukkitRunnable() {
                int countdown = (game.getTrorkPlayers().contains(player) ? 8 : 5);

                @Override
                public void run() {
                    if (game.getGameState() == GameState.ENDING || !player.isOnline()) {
                        player.sendMessage("§cGame finished while you were respawning!");
                        cancel();
                    } else {
                        if (countdown == 0) {
                            player.closeInventory();
                            game.respawnPlayer(player);
                            player.sendTitle("§a§lYOU RESPAWNED", "Teleported!", 10, 20, 10);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            cancel();
                        } else {
                            player.sendTitle("§c§lYOU DIED", "Respawning in " + countdown + "s", 0, 30, 0);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                        }
                        countdown--;
                    }
                }
            }.runTaskTimer(main, 0L, 20L);
        }
    }

}
