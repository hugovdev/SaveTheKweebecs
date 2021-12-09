package me.hugo.savethekweebecs.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public ItemBuilder setType(Material m) {
        is.setType(m);
        return this;
    }

    public ItemBuilder addBookEnchantment(Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) is.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideEnch() {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(im);
        return this;
    }

    public List<String> getLore() {
        return is.getItemMeta().getLore();
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder setSkullTexture(String url) {
        if (url.isEmpty()) return this;

        SkullMeta headMeta = (SkullMeta) this.is.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] arrayOfByte = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[]{url}).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(arrayOfByte)));
        Method setProfileMethod = null;

        try {
            setProfileMethod = headMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
        } catch (NoSuchMethodException | SecurityException ignored) {}

        try {
            if (setProfileMethod == null) {
                Field profileField = headMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(headMeta, profile);
            } else {
                setProfileMethod.setAccessible(true);
                setProfileMethod.invoke(headMeta, profile);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException e1) {
            e1.printStackTrace();
        }

        this.is.setItemMeta(headMeta);
        return this;
    }

    public static String colorize(List<String> list) {
        return ChatColor.translateAlternateColorCodes('&', list.toString());
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLoreWithWrap(String string, int maxLength) {
        ItemMeta im = is.getItemMeta();
        im.setLore(new ArrayList(Arrays.asList(StringUtility.wordWrap(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string), maxLength))));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        if (ench != null) {
            ItemMeta im = is.getItemMeta();
            im.addEnchant(ench, level, true);
            is.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setUnbreakble(boolean unbreakble) {
        is.getItemMeta().setUnbreakable(unbreakble);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        ArrayList<String> fullLore = new ArrayList();
        for (String s : lore) {
            fullLore.add(StringUtility.format(s));
        }
        im.setLore(fullLore);
        is.setItemMeta(im);
        return this;
    }


    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }
}

