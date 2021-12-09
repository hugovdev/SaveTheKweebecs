package me.hugo.savethekweebecs.events;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.specialitems.SpecialItem;
import me.hugo.savethekweebecs.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class SpecialItemEvents implements Listener {

    @EventHandler
    public void onSpecialItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemOnHand = player.getEquipment().getItemInMainHand();

        if (itemOnHand == null || !event.getAction().name().contains("CLICK") || event.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if (itemOnHand.hasItemMeta() && itemOnHand.getItemMeta().hasDisplayName() && itemOnHand.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lSNOWBALL BLASTER AMMO")) {
            event.setCancelled(true);
            return;
        }

        SpecialItem specialItem = SaveTheKweebecs.getPlugin().getAbilityManager().getSpecialItemFromItem(itemOnHand);

        if (specialItem != null) {
            event.setCancelled(specialItem.isConsumable());
            Game game = SaveTheKweebecs.getPlugin().getPlayerGame(player);

            if (game != null) {
                if (specialItem.isConsumable()) {
                    if (itemOnHand.getAmount() > 1) {
                        itemOnHand.setAmount(itemOnHand.getAmount() - 1);
                        player.updateInventory();
                    } else {
                        player.getInventory().setItemInMainHand(null);
                    }
                }

                specialItem.getAbility().execute(player, (event.getAction().name().contains("RIGHT") ? ClickType.RIGHT : ClickType.LEFT), game);
            } else {
                player.sendMessage("§cYou need to be playing to use this item!");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            if (event.getEntity().hasMetadata("fireFeet")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        ProjectileSource shooter = event.getEntity().getShooter();

        if (projectile instanceof LargeFireball || projectile instanceof Fireball && shooter instanceof Player) {
            event.setCancelled(true);
            projectile.remove();
            Location location = projectile.getLocation();
            World world = location.getWorld();
            world.spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

            Player player = (Player) event.getEntity().getShooter();
            Game game = SaveTheKweebecs.getPlugin().getPlayerGame(player);

            if (game != null) {
                boolean isTrork = game.getTrorkPlayers().contains(player);

                for (Entity entity : world.getNearbyEntities(location, 5, 3, 5)) {
                    if (entity instanceof Player) {
                        if ((isTrork && !game.getTrorkPlayers().contains(entity)) || !isTrork && game.getTrorkPlayers().contains(entity)) {
                            GamePlayer gamePlayer = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer((Player) entity);

                            gamePlayer.setLastAttackedPlayer(((Player) shooter).getUniqueId());
                            gamePlayer.setLastAttackTime(System.currentTimeMillis());
                            ((Player) entity).damage(6.0);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void onProjectileHit(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof LargeFireball || entity instanceof Fireball) {
            event.setCancelled(true);
        }
    }

}
