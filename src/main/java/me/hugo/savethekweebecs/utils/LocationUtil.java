package me.hugo.savethekweebecs.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class LocationUtil {

    public static String getStringByLocation(Location loc) {
        String key = loc.getX() + " , " + loc.getY() + " , " + loc.getZ() + " , " + loc.getPitch() + " , " + loc.getYaw();
        return key;
    }

    public static Location getLocationByString(String key, World world) {
        String[] split = key.split(" , ");
        if (split.length == 5) {
            Location loc = new Location(world, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[4]), Float.parseFloat(split[3]));
            return loc;
        }
        return null;
    }
}
