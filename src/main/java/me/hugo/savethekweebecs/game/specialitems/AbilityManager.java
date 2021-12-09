package me.hugo.savethekweebecs.game.specialitems;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AbilityManager {

    HashMap<ItemStack, SpecialItem> itemAbilities;

    public AbilityManager() {
        itemAbilities = new HashMap<>();

        for (SpecialItem specialItem : SpecialItem.values()) {
            if (specialItem.getAbility() != null) itemAbilities.put(specialItem.getAbilityItem(), specialItem);
        }
    }

    public SpecialItem getSpecialItemFromItem(ItemStack itemStack) {
        return itemAbilities.get(itemStack);
    }

}
