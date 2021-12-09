package me.hugo.savethekweebecs.globalgame;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.utils.gui.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GlobalGame {

    SAVE_THE_KWEEBECS("Save The Kweebecs", "Pick a team and save or defend those kweebecs! Use abilities and kill your enemies to get more gold!",
            14, new ItemStack(Material.WOODEN_AXE), (player, type) -> {
        SaveTheKweebecs.getPlugin().getPlayerManager().sendToLobby(player);
        player.sendTitle("§b§lSAVE THE KWEEBECS", "Hosted by ThoriumCube", 10, 40, 10);
        if(player.isOp()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }, true),
    CREATIVE("Creative", "Build awesome creations with other community members and let your imagination flow!",
            12, new ItemStack(Material.GRASS_BLOCK), (player, type) -> {
        SaveTheKweebecs.getPlugin().getPlayerManager().preparePlayer(player, GameMode.CREATIVE);
        player.teleport(Bukkit.getWorld("creative").getSpawnLocation());
        player.sendTitle("§a§lCREATIVE", "Hosted by ThoriumCube", 10, 40, 10);
        player.getInventory().setItem(8, SaveTheKweebecs.getPlugin().getGameSelector());

        player.setAllowFlight(true);
        player.setFlying(true);

        SaveTheKweebecs.getPlugin().getPlayerManager().getGamePlayer(player).setBoard(null);
    }, true);

    private final String name;
    private final String description;
    private final int slot;
    private final ItemStack icon;
    private ClickAction clickAction;
    private boolean isOpen;

    GlobalGame(String name, String description, int slot, ItemStack icon, ClickAction clickAction, boolean isOpen) {
        this.name = name;
        this.description = description;
        this.slot = slot;
        this.icon = icon;
        this.clickAction = clickAction;
        this.isOpen = isOpen;
    }

    public static GlobalGame fromName(String name) {
        GlobalGame result = null;

        try {
            result = GlobalGame.valueOf(name);
        } catch (IllegalArgumentException e) {
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
