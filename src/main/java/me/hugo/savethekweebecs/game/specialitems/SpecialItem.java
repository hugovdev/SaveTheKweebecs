package me.hugo.savethekweebecs.game.specialitems;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.player.PlayerManager;
import me.hugo.savethekweebecs.utils.ColorUtil;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.gui.Icon;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public enum SpecialItem {

    FIRE_BALL("Fireball", "Launch a powerful fireball that creates an explosion which damages the enemy team!",
            new ItemStack(Material.FIRE_CHARGE), "#ff3a1b", "#d94730", 10, (player, type, game) -> {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setShooter(player);
        fireball.setIsIncendiary(false);
        fireball.setBounce(false);
        fireball.setVisualFire(true);
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
    }, true),
    GOLDEN_APPLE("Golden Apple", "Get a golden apple to heal yourself in battle!",
            new ItemStack(Material.GOLDEN_APPLE), "#e5f11b", "#79ff00", 15, null, false),
    EXTRA_HELMET("Hard Helmet", "Protect your head with a helmet! You might not look fancy but you have some extra protection! (You don't keep it when dying)",
            new ItemStack(Material.CHAINMAIL_HELMET), "#1bbaf1", "#00c6ff", 25, (player, type, game) -> {
        player.sendMessage("§aYou've been equiped with a chainmail helmet!");
        player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
    }, true),
    /*TOXIC_MUSHROOM("Toxic Mushroom", "Hit somebody with this in your hands and give them a wither effect that will end up killing them!",
            new ItemStack(Material.CRIMSON_FUNGUS), "#00952d", "#00ff15", 25, null, false),*/
    MED_KIT("Med Kit", "Get 3 healing potions for you or your allies!",
            new ItemStack(Material.RED_TULIP), "#e5f11b", "#79ff00", 30, (player, type, game) -> {
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1.0f, 1.0f);

        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION, 3);
        PotionMeta meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setColor(Color.RED);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1, false, true, false), true);
        itemStack.setItemMeta(meta);

        player.getInventory().addItem(itemStack);
    }, true),
    FIRE_FEET("Fire Feet", "Activate special boots that leave a trail of fire behind, great for when someone is trying to hunt you down!",
            new ItemStack(Material.IRON_BOOTS), "#f16d1b", "#ffa600", 30, (player, type, game) -> {
        player.sendMessage("§c§lYou used fire feet!");
        player.setMetadata("fireFeet", new FixedMetadataValue(SaveTheKweebecs.getPlugin(), "fireFeet"));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 2));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);

        new BukkitRunnable() {
            List<Location> fire = new ArrayList<>();
            int count = 0;

            @Override
            public void run() {
                if (count < 10 * 4 && game.getGameState() == GameState.INGAME) {
                    Location playerFeet = player.getLocation();
                    if (playerFeet.getBlock().getType() == Material.AIR) {
                        playerFeet.getBlock().setType(Material.FIRE);
                        fire.add(playerFeet);
                    }
                    count++;
                } else {
                    cancel();
                    for (Location location : fire) {
                        location.getBlock().setType(Material.AIR);
                    }
                    player.removeMetadata("fireFeet", SaveTheKweebecs.getPlugin());
                    player.setFireTicks(0);
                }
            }
        }.runTaskTimer(SaveTheKweebecs.getPlugin(), 0L, 2L);
    }, true),
    KWEEBEC_BURST("Kweebec Energy Burst", "§8Only Works with Kweebecs\n\n§7Release a huge burst of energy killing you and everyone 5 blocks around you.",
            new ItemStack(Material.OAK_SAPLING), "#1ead49", "#046e0d", 45, (player, type, game) -> {
        boolean isTrork = game.getTrorkPlayers().contains(player);
        PlayerManager playerManager = SaveTheKweebecs.getPlugin().getPlayerManager();
        GamePlayer gamePlayer = playerManager.getGamePlayer(player);

        if (isTrork) {
            player.sendMessage("§cThis item can only be used by §bKweebecs§c!");
            player.sendMessage("§eGold refunded!");
            gamePlayer.setGold(gamePlayer.getGold() + 45);
            gamePlayer.getPlayerBoard().set("Gold: §e" + gamePlayer.getGold() + " ⛃", 6);
        } else {
            player.sendMessage("§aKweebec energy burst released!");
            Location location = player.getLocation();

            game.sendGameMessage("§c" + player.getName() + " §eused Kweebec Energy Burst!");

            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1.0f);
            location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
            /*
            Kill players around and drops
             */
            int killedPlayers = 0;

            for (Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
                if (entity instanceof Player) {
                    Player damaged = (Player) entity;
                    if (game.getTrorkPlayers().contains(damaged)) {
                        GamePlayer damagedGP = playerManager.getGamePlayer(damaged);
                        damaged.sendMessage("§b" + player.getName() + "'s §akweebec energy burst killed you!");

                        damagedGP.setLastAttackedPlayer(player.getUniqueId());
                        damagedGP.setLastAttackTime(System.currentTimeMillis());

                        damaged.damage(100.0);
                        killedPlayers++;
                    }
                }
            }

            player.damage(100.0);
            player.sendMessage("§aYour Kweebec Energy Boost ability killed §b" + killedPlayers + "§a players!");
        }
    }, true),
    SUPER_STRENGTH("Super Strength", "Activate a strength boost that will give you a massive damage and speed oputput for 5s!",
            new ItemStack(Material.BLAZE_POWDER), "#f16d1b", "#ffa600", 50, (player, type, game) -> {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 5, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1));
        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
        player.sendMessage("§cYou activated the strength boost!");
    }, true),
    SNOWBALL_SHOOTER("Snowball Blaster", "Get a snowball shooter and 5 explosive snowballs as bullets!\n\nOnly collides with blocks.",
            new ItemStack(Material.IRON_HORSE_ARMOR), "#f16d1b", "#ffa600", 60, (player, type, game) -> {

        if (!player.getInventory().contains(Material.SNOWBALL))
            player.getInventory().addItem(new ItemBuilder(Material.SNOWBALL, 5).setName("§c§lSNOWBALL BLASTER AMMO").toItemStack());

        int amount = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack is : inv.all(Material.SNOWBALL).values()) {
            if (is != null && is.getType() == Material.SNOWBALL) {
                amount = amount + is.getAmount();
            }
        }

        if (amount > 0) {
            for (ItemStack itemStack : inv.getContents()) {
                if (itemStack != null && itemStack.getType() == Material.SNOWBALL) {
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    break;
                }
            }
            amount--;

            if (amount == 0) {
                player.getInventory().setItemInMainHand(null);
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                player.sendMessage("§cYour §bSnowball Blaster§c broke!");
            }

            /*
            Shoot actual weapon
             */
            ArmorStand bullet = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            bullet.getEquipment().setHelmet(new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("https://textures.minecraft.net/texture/1dfd7724c69a024dcfc60b16e00334ab5738f4a92bafb8fbc76cf15322ea0293").toItemStack());
            bullet.setVisible(false);
            bullet.setInvulnerable(true);
            bullet.setCollidable(false);
            bullet.setGravity(false);

            new BukkitRunnable() {
                int time = 0;
                Vector vecTo = player.getEyeLocation().getDirection().normalize().multiply(1.5).add(new Vector(0, -1.6, 0));

                @Override
                public void run() {
                    if (time <= 70) {
                        Location location = bullet.getEyeLocation().clone().add(vecTo);
                        bullet.teleport(location);

                        location = location.clone().add(0, 1, 0);

                        if (location.getBlock().getType() != Material.AIR) {
                            cancel();
                            World world = location.getWorld();
                            world.spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
                            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 1.0f);
                            bullet.remove();

                            boolean isTrork = game.getTrorkPlayers().contains(player);

                            for (Entity entity : world.getNearbyEntities(location, 5, 3, 5)) {
                                if (entity instanceof Player) {
                                    if ((isTrork && !game.getTrorkPlayers().contains(entity)) || !isTrork && game.getTrorkPlayers().contains(entity)) {
                                        GamePlayer gamePlayer = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer((Player) entity);

                                        gamePlayer.setLastAttackedPlayer(player.getUniqueId());
                                        gamePlayer.setLastAttackTime(System.currentTimeMillis());
                                        ((Player) entity).damage(7.0);
                                    }
                                }
                            }
                        }
                        time++;
                    } else {
                        cancel();
                        if (!bullet.isDead()) {
                            bullet.remove();
                        }
                    }
                }
            }.runTaskTimer(SaveTheKweebecs.getPlugin(), 0L, 2L);

            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.7f);
        } else {
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            player.sendMessage("§cYour §bSnowball Blaster§c broke!");
        }
    }, false);

    private final String name;
    private final String description;
    private final ItemStack item;
    private final String nameColor;
    private final String nameSecondColor;
    private final int price;
    private final SpecialItemAction ability;
    private final boolean consumable;

    private final ItemStack abilityItem;

    SpecialItem(String name, String description, ItemStack item, String nameColor, String nameSecondColor, int price, SpecialItemAction ability, boolean consumable) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.nameColor = nameColor;
        this.nameSecondColor = nameSecondColor;
        this.price = price;
        this.ability = ability;
        this.consumable = consumable;

        this.abilityItem = new ItemBuilder(item.clone()).setName((nameColor.contains("#") ? ColorUtil.createGradFromStrings(name, true, nameColor.replace("#", ""), nameSecondColor.replace("#", "")) : nameColor + name) + (ability == null ? "" : " &8| &7Right Click"))
                .setLoreWithWrap("&7" + description + "\n\n&eClick to use!", 30)
                .hideEnch()
                .toItemStack();
    }

    public Icon getShopIcon(GamePlayer gamePlayer) {
        boolean hasEnoughGold = gamePlayer.getGold() >= this.price;
        String colorizedName = (hasEnoughGold ? "§a" : "§c") + this.name;

        Icon icon = new Icon(new ItemBuilder(item.clone())
                .setName(colorizedName)
                .setLoreWithWrap("&7" + this.description + "\n\n&7Cost: §e" + this.price + " ⛃\n\n" +
                        (hasEnoughGold ? "&eClick to buy!" : "&cNot enough gold!"), 30)
                .hideEnch()
                .toItemStack())
                .addClickAction((player, type, main) -> {
                    if (hasEnoughGold) {
                        if (player.getInventory().containsAtLeast(abilityItem, 1)) {
                            player.sendMessage("§cYou already have §b" + name + "§c, use it before buying more!");
                        } else {
                            player.closeInventory();
                            player.getInventory().addItem(this.abilityItem);
                            gamePlayer.setGold(gamePlayer.getGold() - this.price);
                            gamePlayer.setTotalGoldSpent(gamePlayer.getTotalGoldSpent() + this.price);
                            player.sendMessage("§aYou bought §b" + name + " §afor §e" + this.price + " ⛃" + "§a!");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                            gamePlayer.getPlayerBoard().set("Gold: §e" + gamePlayer.getGold() + " ⛃", 6);
                        }
                    } else {
                        player.sendMessage("§cYou don't have enough gold to buy §b" + name + "§c!");
                    }
                });

        return icon;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getNameColor() {
        return nameColor;
    }

    public String getNameSecondColor() {
        return nameSecondColor;
    }

    public int getPrice() {
        return price;
    }

    public SpecialItemAction getAbility() {
        return ability;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public ItemStack getAbilityItem() {
        return abilityItem;
    }
}
