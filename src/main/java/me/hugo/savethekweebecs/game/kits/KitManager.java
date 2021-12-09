package me.hugo.savethekweebecs.game.kits;

import me.hugo.savethekweebecs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class KitManager {

    public GameKit TRORK_NORMAL_KIT;
    public GameKit KWEEBEC_NORMAL_KIT;
    public ItemStack GAME_SHOP;

    public KitManager() {
        GAME_SHOP = new ItemBuilder(Material.GOLD_NUGGET)
                .setName("&aItem Shop &e⛃ &7(Right Click)")
                .setLoreWithWrap("&7Buy special items and abilities on this shop. Spend gold and get better gear. Get gold by killing the enemy team.\n\n&eClick to open", 35)
                .toItemStack();

        TRORK_NORMAL_KIT = new GameKit();

        /*
        Trork Kit Items
         */
        TRORK_NORMAL_KIT.addItem(new ItemBuilder(Material.STONE_SWORD, 1)
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .setName("&a&lTrork Sword")
                .setUnbreakble(true)
                .setLoreWithWrap("&7This sword is used by the most mighty Trorks! High damage and high speed!", 35)
                .toItemStack(), 0);

        TRORK_NORMAL_KIT.addItem(new ItemBuilder(Material.CROSSBOW, 1)
                .setName("&b&lKweebec Destroyer &c&lX-3000")
                .setUnbreakble(true)
                .setLoreWithWrap("&7This crossbow can be left charged and used when needed! Let's get those Kweebecs!", 35)
                .toItemStack(), 1);

        TRORK_NORMAL_KIT.addItem(new ItemBuilder(Material.COMPASS, 1)
                .setName("&c&lKweebec Locator")
                .setLoreWithWrap("&7This handy compass lets trorks know when a Kweebec is nearby!\n\n&7Cooldown: &f5s\n\n&eClick to use!", 35)
                .toItemStack(), 2);

        TRORK_NORMAL_KIT.addItem(new ItemStack(Material.COOKED_BEEF, 16), 8);
        TRORK_NORMAL_KIT.addItem(GAME_SHOP, 7);
        TRORK_NORMAL_KIT.addItem(new ItemStack(Material.ARROW, 5), 6);

        TRORK_NORMAL_KIT.addItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE, 1)
                .setName("&c&lTrork Forged Chestplate")
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakble(true)
                .toItemStack(), 38);

        TRORK_NORMAL_KIT.addItem(new ItemBuilder(Material.DIAMOND_BOOTS, 1)
                .setName("&c&lTrork Forged Boots")
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchant(Enchantment.PROTECTION_FALL, 3)
                .setUnbreakble(true)
                .toItemStack(), 36);

        KWEEBEC_NORMAL_KIT = new GameKit();

        /*
        Kweebec Kit Items
         */
        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.STONE_SWORD, 1)
                .setName("&a&lKweebec Sword")
                .setUnbreakble(true)
                .addEnchant(Enchantment.DAMAGE_ALL, 2)
                .setLoreWithWrap("&7This sword uses the power of nature to deal extra damage!\n\n&8&oForged by Gaia", 35)
                .toItemStack(), 0);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.BOW, 1)
                .setName("&b&lKweebec Defender &c&lX-3000")
                .setUnbreakble(true)
                .setLoreWithWrap("&7This crossbow can be left charged and used when needed! Let's get those Kweebecs!", 35)
                .toItemStack(), 1);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.COMPASS, 1)
                .setName("&c&lKweebec Locator")
                .setLoreWithWrap("&7This handy compass lets kweebecs know where their trapped friends are!\n\n&7Cooldown: &f5s\n\n&eClick to use!", 35)
                .toItemStack(), 2);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.WOODEN_AXE, 1)
                .setUnbreakble(true)
                .setName("&a&lRusty Axe &7(Can Break Wood Fences)")
                .toItemStack(), 3);

        KWEEBEC_NORMAL_KIT.addItem(new ItemStack(Material.COOKED_BEEF, 16), 8);
        KWEEBEC_NORMAL_KIT.addItem(GAME_SHOP, 7);
        KWEEBEC_NORMAL_KIT.addItem(new ItemStack(Material.ARROW, 5), 6);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.IRON_CHESTPLATE, 1)
                .setName("&a&lKweebec Chestplate")
                .setUnbreakble(true)
                .toItemStack(), 38);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.DIAMOND_LEGGINGS, 1)
                .setName("&a&lKweebec Leggings")
                .setUnbreakble(true)
                .toItemStack(), 37);

        KWEEBEC_NORMAL_KIT.addItem(new ItemBuilder(Material.IRON_BOOTS, 1)
                .setName("&a&lKweebec Boots")
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakble(true)
                .toItemStack(), 36);
    }

}
