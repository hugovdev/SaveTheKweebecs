package me.hugo.savethekweebecs.game.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class GameKit {

    private HashMap<Integer, ItemStack> kitItems;

    public GameKit() {
        kitItems = new HashMap<>();
    }

    public void addItem(ItemStack itemStack, Integer slot) {
        kitItems.put(slot, itemStack);
    }

    public void give(Player player, boolean clearInventory) {
        PlayerInventory inventory = player.getInventory();

        if(clearInventory) {
            inventory.clear();
            inventory.setArmorContents(null);
        }

        for(Integer slot : kitItems.keySet()) {
            inventory.setItem(slot, kitItems.get(slot));
        }
    }

}
