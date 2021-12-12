package me.hugo.savethekweebecs.cosmetics;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.ItemBuilder;
import me.hugo.savethekweebecs.utils.gui.ClickAction;
import me.hugo.savethekweebecs.utils.gui.Icon;
import me.hugo.savethekweebecs.utils.gui.PaginatedGUI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum Suit {

    NONE("No Suit", null, null, null, null, new ItemStack(Material.BARRIER)),
    KWEEBEC("Kweebec Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/fa66479997b98874c65f6a08dbfb6c32380fed0aa27af58cb1c66cabed5da281").setName("§eKweebec Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.ORANGE).setName("§6Kweebec Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.YELLOW).setName("§6Kweebec Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.ORANGE).setName("§6Kweebec Boots").hideEnch().toItemStack(),
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/fa66479997b98874c65f6a08dbfb6c32380fed0aa27af58cb1c66cabed5da281").toItemStack()),
    TRORK("Trork Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ea4f778cd8c8b8bf391e3943409f88afb41ae3d70a93ea74acd2f10123b7de46").setName("§cTrork Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.AQUA).setName("§bTrork Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.SILVER).setName("§bTrork Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.AQUA).setName("§bTrork Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ea4f778cd8c8b8bf391e3943409f88afb41ae3d70a93ea74acd2f10123b7de46").toItemStack()),
    FERAN("Feran Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ef5e489339742db0b2d8da4c00a9f79d72b28fcfeebcaab68c5f1abe2750bc80").setName("§eFeran Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.fromRGB(245, 211, 118)).setName("§eFeran Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.fromRGB(184, 157, 83)).setName("§eFeran Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.fromRGB(245, 211, 118)).setName("§eFeran Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ef5e489339742db0b2d8da4c00a9f79d72b28fcfeebcaab68c5f1abe2750bc80").toItemStack()),
    SCARAK("Scarak Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/e0da7ff2a579fcabc3c541359607fd12907b87abbd0035c44cb3dc251b62461b").setName("§eScarak Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.fromRGB(153, 102, 51)).setName("§eScarak Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.fromRGB(102, 51, 0)).setName("§eScarak Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.fromRGB(153, 102, 51)).setName("§eScarak Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/e0da7ff2a579fcabc3c541359607fd12907b87abbd0035c44cb3dc251b62461b").toItemStack()),
    OUTLANDER("Outlander Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/5859900257f8e3659fef87d5d6371383e4cfc7f0242f70aa9c95d96d68d0afb4").setName("§cOutlander Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.fromRGB(153, 102, 51)).setName("§cOutlander Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.WHITE).setName("§cOutlander Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.fromRGB(153, 102, 51)).setName("§cOutlander Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/5859900257f8e3659fef87d5d6371383e4cfc7f0242f70aa9c95d96d68d0afb4").toItemStack()),
    FEN_STALKER("Fen Stalker Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/68e74c127daf575791b39390d1a7b9cab610de038152d747a5ff9a3c140982cd").setName("§aFen Stalker Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.LIME).setName("§aFen Stalker Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.GREEN).setName("§aFen Stalker Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.LIME).setName("§aFen Stalker Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/68e74c127daf575791b39390d1a7b9cab610de038152d747a5ff9a3c140982cd").toItemStack()),
    PIGEON("Pigeon Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/1feebeafbd71ebc23311cf7af66dd1cef5ac677d34b0feac71dbb5d04a61e54e").setName("§ePigeon Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.GRAY).setName("§ePigeon Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.BLACK).setName("§ePigeon Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.GRAY).setName("§ePigeon Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/1feebeafbd71ebc23311cf7af66dd1cef5ac677d34b0feac71dbb5d04a61e54e").toItemStack()),
    GAIA("Gaia Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/a23fbea0b2d2c5713caa75242d996282c0c7f3407ae99b3aff974440ea3123ae").setName("§aGaia Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.LIME).setName("§aGaia Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.LIME).setName("§aGaia Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.WHITE).setName("§aGaia Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/a23fbea0b2d2c5713caa75242d996282c0c7f3407ae99b3aff974440ea3123ae").toItemStack()),
    VARYN("Varyn Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ad7354929d1bc74f2c471b0040fcbf19b74c10593a76eb2d50eff9b4b030c3eb").setName("§cVaryn Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.PURPLE).setName("§cVaryn Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.LIME).setName("§cVaryn Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.PURPLE).setName("§cVaryn Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/ad7354929d1bc74f2c471b0040fcbf19b74c10593a76eb2d50eff9b4b030c3eb").toItemStack()),
    AMONG_US("Among Us Suit",
            new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/8e2a1e4f74bcae3f110cb20b2898ddc6d373d127c8f7753bcaf394c542c0ab00").setName("§aAmong Us Suit").toItemStack(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.LIME).setName("§aAmong Us Chestplate").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherArmorColor(Color.LIME).setName("§aAmong Us Leggings").hideEnch().toItemStack(),
            new ItemBuilder(Material.LEATHER_BOOTS).setLeatherArmorColor(Color.LIME).setName("§aAmong Us Boots").hideEnch().toItemStack(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("http://textures.minecraft.net/texture/8e2a1e4f74bcae3f110cb20b2898ddc6d373d127c8f7753bcaf394c542c0ab00").toItemStack());

    private final String suitName;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;
    private final ItemStack icon;

    public final Icon SELECTED_ICON;
    public final Icon NOT_SELECTED_ICON;
    public final Icon SELECTED_TOGGLE;
    public final Icon NOT_SELECTED_TOGGLE;

    private ClickAction clickAction;

    Suit(String suitName, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack icon) {
        this.suitName = suitName;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.icon = icon;

        this.clickAction = getClickAction();

        this.SELECTED_ICON = getSuitIcon(true);
        this.NOT_SELECTED_ICON = getSuitIcon(false);

        this.SELECTED_TOGGLE = getToggleIcon(true);
        this.NOT_SELECTED_TOGGLE = getToggleIcon(false);
    }

    private ClickAction getClickAction() {
        Suit currentSuit = this;

        return (player, type) -> {
            GamePlayer gamePlayer = SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player);

            if (gamePlayer.getSuit() == currentSuit) {
                if (currentSuit != Suit.NONE) {
                    player.sendMessage("§aUnequipped §b" + currentSuit.suitName + "§a!");
                    Suit.NONE.equip(player);

                    PaginatedGUI suitMenu = gamePlayer.getSuitMenu();

                    gamePlayer.setSuit(Suit.NONE);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);

                    suitMenu.replaceItem(SELECTED_TOGGLE.itemStack, NOT_SELECTED_TOGGLE);
                    suitMenu.replaceItem(SELECTED_ICON.itemStack, NOT_SELECTED_ICON);
                    suitMenu.replaceItem(Suit.NONE.NOT_SELECTED_ICON.itemStack, Suit.NONE.SELECTED_ICON);
                    suitMenu.replaceItem(Suit.NONE.NOT_SELECTED_TOGGLE.itemStack, Suit.NONE.SELECTED_TOGGLE);
                } else {
                    player.sendMessage("§cYou have no suit on!");
                }
            } else {
                Suit lastSuit = gamePlayer.getSuit();
                currentSuit.equip(player);
                if (currentSuit == NONE) player.sendMessage("§aRemoved your suit!");
                else player.sendMessage("§aSelected §b" + suitName + "§a!");

                PaginatedGUI suitMenu = gamePlayer.getSuitMenu();

                gamePlayer.setSuit(currentSuit);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);

                suitMenu.replaceItem(currentSuit.NOT_SELECTED_ICON.itemStack, currentSuit.SELECTED_ICON);
                suitMenu.replaceItem(currentSuit.NOT_SELECTED_TOGGLE.itemStack, currentSuit.SELECTED_TOGGLE);
                suitMenu.replaceItem(lastSuit.SELECTED_ICON.itemStack, lastSuit.NOT_SELECTED_ICON);
                suitMenu.replaceItem(lastSuit.SELECTED_TOGGLE.itemStack, lastSuit.NOT_SELECTED_TOGGLE);
            }
        };
    }

    public Icon getSuitIcon(boolean selected) {
        return new Icon(new ItemBuilder(icon.clone())
                .setName("§a" + suitName)
                .setLoreWithWrap((this == Suit.NONE ? ("§7Remove your current suit.") :
                        ("§7Select §f" + suitName + "§7 as your suit!\n\n" + (selected ? "§aSELECTED" : "§eClick to equip!"))), 35)
                .hideEnch().toItemStack()).addClickAction(clickAction);
    }

    public void equip(Player player) {
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }

    public Icon getToggleIcon(boolean selected) {
        return new Icon(new ItemBuilder((selected ? Material.LIME_DYE : Material.GRAY_DYE))
                .setName("§a" + suitName)
                .setLoreWithWrap((this == Suit.NONE ? ("§7Remove your current suit.") :
                        ("§7Select §f" + suitName + "§7 as your suit!\n\n" + (selected ? "§aSELECTED" : "§eClick to equip!"))), 35)
                .hideEnch().toItemStack()).addClickAction(clickAction);
    }
}
