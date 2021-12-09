package me.hugo.savethekweebecs.utils;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class ItemStackBuilder implements Cloneable {

    private ItemStack itemStack;

    public ItemStackBuilder(String title, String author, List<String> pages) {
        this.itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        bookMeta.setAuthor(author);
        bookMeta.setTitle(title);
        bookMeta.setPages(pages);
        this.itemStack.setItemMeta(bookMeta);
    }

    public ItemStackBuilder(Material material, Color color) {
        this.itemStack = new ItemStack(material);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.itemStack.setItemMeta(leatherArmorMeta);
    }

    public ItemStackBuilder(Material baseColor, Pattern... patterns) {
        this.itemStack = new ItemStack(baseColor);
        BannerMeta bannerMeta = (BannerMeta) this.itemStack.getItemMeta();
        for (Pattern pattern : patterns) {
            bannerMeta.addPattern(pattern);
        }
        this.itemStack.setItemMeta(bannerMeta);
    }

    public ItemStackBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemStackBuilder(Material material, int data) {
        this.itemStack = new ItemStack(material, 1, (byte) data);
    }


    public ItemStackBuilder(Material material, int data, int amount) {
        this.itemStack = new ItemStack(material, amount, (byte) data);
    }

    public ItemStackBuilder(ItemStack itemClone) {
        this.itemStack = itemClone.clone();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemMeta getItemMeta() {
        return getItemStack().hasItemMeta() ? getItemStack().getItemMeta() : null;
    }

    public ItemStackBuilder withDisplayName(String displayName) {
        if (displayName != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addLore(List<String> lore) {
        if (lore != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
            itemLore.addAll(lore);
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public ItemStackBuilder withLore(List<String> lore) {
        if (lore != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        return this;
    }

    public ItemStackBuilder withLore(String... lore) {
        return withLore(Arrays.asList(lore));
    }

    public ItemStackBuilder withAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder withDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemStackBuilder withEnchantments(HashMap<Enchantment, Integer> enchantments) {
        return withEnchantments((Map<Enchantment, Integer>) enchantments);
    }

    public ItemStackBuilder withEnchantments(Map<Enchantment, Integer> enchantments) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        for (Enchantment enchantment : enchantments.keySet()) {
            itemMeta.addEnchant(enchantment, enchantments.get(enchantment), true);
        }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder withFlags(List<ItemFlag> itemFlags) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        for (ItemFlag flag : itemFlags) {
            itemMeta.addItemFlags(flag);
        }
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder hideAttributes() {
        ItemMeta im = itemStack.getItemMeta();
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        im.setUnbreakable(true);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemStackBuilder withFlags(ItemFlag... itemFlags) {
        return withFlags(Arrays.asList(itemFlags));
    }

    public ItemStack build() {
        return itemStack;
    }

    @Override
    public ItemStackBuilder clone() {
        return new ItemStackBuilder(getItemStack().clone());
    }

}

