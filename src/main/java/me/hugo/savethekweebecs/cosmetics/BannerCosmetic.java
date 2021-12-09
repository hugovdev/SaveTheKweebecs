package me.hugo.savethekweebecs.cosmetics;

import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.ItemStackBuilder;
import me.hugo.savethekweebecs.utils.gui.Icon;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.nio.ByteOrder;

public enum BannerCosmetic {

    NONE(0, null, null),
    UNITED_KINGDOM(1, "United Kingdom",
            new ItemStackBuilder(Material.BLUE_BANNER, new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.RED, PatternType.CROSS), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.RED, PatternType.STRAIGHT_CROSS))),
    SPAIN(2, "Spain",
            new ItemStackBuilder(Material.RED_BANNER, new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER), new Pattern(DyeColor.YELLOW, PatternType.STRIPE_CENTER))),
    GERMANY(3, "Germany",
            new ItemStackBuilder(Material.RED_BANNER, new Pattern(DyeColor.YELLOW, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))),
    COLOMBIA(4, "Colombia",
            new ItemStackBuilder(Material.BLUE_BANNER, new Pattern(DyeColor.YELLOW, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLUE, PatternType.STRIPE_CENTER))),
    ARGENTINA(5, "Argentina",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.FLOWER), new Pattern(DyeColor.YELLOW, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_RIGHT))),
    FRANCE(6, "France",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP), new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM))),
    ITALY(7, "Italy",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.GREEN, PatternType.HALF_VERTICAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.RED, PatternType.STRIPE_RIGHT))),
    MEXICO(8, "Mexico",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP), new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE))),
    URUGUAY(9, "Uruguay",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_SMALL), new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT), new Pattern(DyeColor.YELLOW, PatternType.SQUARE_TOP_RIGHT))),
    USA(10, "USA",
            new ItemStackBuilder(Material.RED_BANNER, new Pattern(DyeColor.WHITE, PatternType.STRIPE_SMALL), new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.BLUE, PatternType.SQUARE_TOP_RIGHT))),
    LGBT_PRIDE(11, "LGBT Pride",
            new ItemStackBuilder(Material.LIME_BANNER, new Pattern(DyeColor.YELLOW, PatternType.HALF_VERTICAL), new Pattern(DyeColor.ORANGE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLUE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.RED, PatternType.BORDER))),
    TRANS_PRIDE(12, "Trans Pride",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.PINK, PatternType.STRIPE_SMALL), new Pattern(DyeColor.PINK, PatternType.STRIPE_SMALL), new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_RIGHT))),
    HYPIXEL(13, "Hypixel Studios",
            new ItemStackBuilder(Material.BLUE_BANNER, new Pattern(DyeColor.BLUE, PatternType.HALF_VERTICAL), new Pattern(DyeColor.BLUE, PatternType.DIAGONAL_LEFT), new Pattern(DyeColor.BLUE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLUE, PatternType.TRIANGLES_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLUE, PatternType.BORDER), new Pattern(DyeColor.BLUE, PatternType.TRIANGLES_BOTTOM))),
    HYTALE_INFO(14, "HytaleInfo",
            new ItemStackBuilder(Material.BLUE_BANNER, new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE))),
    SCARAK(15, "Scarak",
            new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.LIGHT_BLUE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BROWN, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.ORANGE, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.GREEN, PatternType.SQUARE_BOTTOM_LEFT), new Pattern(DyeColor.GREEN, PatternType.SQUARE_BOTTOM_RIGHT), new Pattern(DyeColor.BLACK, PatternType.BORDER))),
    KWEEBEC(16, "Kweebec",
                   new ItemStackBuilder(Material.BLACK_BANNER, new Pattern(DyeColor.ORANGE, PatternType.STRIPE_CENTER), new Pattern(DyeColor.ORANGE, PatternType.STRIPE_TOP), new Pattern(DyeColor.ORANGE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.ORANGE, PatternType.CROSS), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.LIME, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.LIME, PatternType.TRIANGLES_TOP))),
    TRORKS(17, "Trork",
                    new ItemStackBuilder(Material.WHITE_BANNER, new Pattern(DyeColor.CYAN, PatternType.STRIPE_CENTER), new Pattern(DyeColor.CYAN, PatternType.STRIPE_TOP), new Pattern(DyeColor.CYAN, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.CYAN, PatternType.CROSS), new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM), new Pattern(DyeColor.CYAN, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.CYAN, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BROWN, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BROWN, PatternType.TRIANGLES_TOP)))
    ;

    private final Integer id;
    private final String name;
    private final ItemStackBuilder banner;

    BannerCosmetic(Integer id, String name, ItemStackBuilder banner) {
        this.id = id;
        this.name = name;
        this.banner = banner;
    }

    public static BannerCosmetic fromId(Integer id) {
        for (BannerCosmetic bannerCosmetic : BannerCosmetic.values()) {
            if (bannerCosmetic.getId() == id)
                return bannerCosmetic;
        }
        return null;
    }

    public ItemStack getBanner() {
        return this == NONE ? null : banner.clone().hideAttributes().withDisplayName("§a§lCOSMETIC §a" + name).build();
    }

    public Icon getIcon(GamePlayer gamePlayer, boolean isSelected) {
        Icon icon;

        if (this == NONE) {
            icon = new Icon(new ItemBuilder(Material.BARRIER)
                    .setName("§aNo Banner")
                    .setLore("§7No banner will be",
                            "§7shown when playing!",
                            "",
                            "§eClick to select!")
                    .toItemStack());

            icon.addClickAction((player, type) -> {
                BannerCosmetic lastBanner = gamePlayer.getSelectedBanner();
                gamePlayer.setSelectedBanner(BannerCosmetic.NONE);

                if (gamePlayer.getLastBannerItem() != null)
                    gamePlayer.getBannerMenu().replaceItem(gamePlayer.getLastBannerItem(), lastBanner.getIcon(gamePlayer));

                player.sendMessage("§cYou removed your banner!");
                gamePlayer.giveBannerSelectorItem(player);
                gamePlayer.setLastBannerItem(null);
            });
            return icon;
        }

        icon = new Icon(new ItemBuilder(banner.clone().build())
                .setName("§a" + name)
                .addEnchant((isSelected ? Enchantment.ARROW_INFINITE : null), 1)
                .hideEnch()
                .setLore("§7Select " + name + " as your",
                        "§7banner to wear on battle.",
                        "",
                        (isSelected ? "§aSELECTED" : "§eClick to select!"))
                .toItemStack()).addClickAction(
                (player, type) -> {
                    if (isSelected) {
                        player.sendMessage("§b" + name + " §cis already your banner! (Banners only show §bin-game§c)");
                    } else {
                        BannerCosmetic lastBanner = gamePlayer.getSelectedBanner();
                        gamePlayer.setSelectedBanner(this);

                        if (gamePlayer.getLastBannerItem() != null)
                            gamePlayer.getBannerMenu().replaceItem(gamePlayer.getLastBannerItem(), lastBanner.getIcon(gamePlayer));

                        gamePlayer.getBannerMenu().replaceItem(this.getIcon(gamePlayer, false).itemStack, getIcon(gamePlayer));
                        gamePlayer.updateLastBannerItem();
                        gamePlayer.giveBannerSelectorItem(player);

                        player.sendMessage("§aYou selected §b" + name + "§a as you banner! (Shown in-game)");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                }
        );

        return icon;
    }

    public Icon getIcon(GamePlayer gamePlayer) {
        Icon icon;
        boolean isSelected = gamePlayer.getSelectedBanner() == this;

        if (this == NONE) {
            icon = new Icon(new ItemBuilder(Material.BARRIER)
                    .setName("§aNo Banner")
                    .setLore("§7No banner will be",
                            "§7shown when playing!",
                            "",
                            "§eClick to select!")
                    .toItemStack());

            icon.addClickAction((player, type) -> {
                BannerCosmetic lastBanner = gamePlayer.getSelectedBanner();
                gamePlayer.setSelectedBanner(BannerCosmetic.NONE);

                if (gamePlayer.getLastBannerItem() != null)
                    gamePlayer.getBannerMenu().replaceItem(gamePlayer.getLastBannerItem(), lastBanner.getIcon(gamePlayer));

                player.sendMessage("§cYou removed your banner!");
                gamePlayer.giveBannerSelectorItem(player);
                gamePlayer.setLastBannerItem(null);
            });
            return icon;
        }

        icon = new Icon(new ItemBuilder(banner.clone().build())
                .setName("§a" + name)
                .addEnchant((isSelected ? Enchantment.ARROW_INFINITE : null), 1)
                .hideEnch()
                .setLore("§7Select " + name + " as your",
                        "§7banner to wear on battle.",
                        "",
                        (isSelected ? "§aSELECTED" : "§eClick to select!"))
                .toItemStack()).addClickAction(
                (player, type) -> {
                    if (isSelected) {
                        player.sendMessage("§b" + name + " §cis already your banner! (Banners only show §bin-game§c)");
                    } else {
                        BannerCosmetic lastBanner = gamePlayer.getSelectedBanner();
                        gamePlayer.setSelectedBanner(this);

                        if (gamePlayer.getLastBannerItem() != null)
                            gamePlayer.getBannerMenu().replaceItem(gamePlayer.getLastBannerItem(), lastBanner.getIcon(gamePlayer));

                        gamePlayer.getBannerMenu().replaceItem(this.getIcon(gamePlayer, false).itemStack, getIcon(gamePlayer));
                        gamePlayer.updateLastBannerItem();
                        gamePlayer.giveBannerSelectorItem(player);

                        player.sendMessage("§aYou selected §b" + name + "§a as you banner! (Shown in-game)");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
                    }
                }
        );

        return icon;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
