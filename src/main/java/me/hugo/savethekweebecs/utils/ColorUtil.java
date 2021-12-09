package me.hugo.savethekweebecs.utils;

import me.hugo.savethekweebecs.utils.rainbow.HomogeneousRainbowException;
import me.hugo.savethekweebecs.utils.rainbow.InvalidColourException;
import me.hugo.savethekweebecs.utils.rainbow.NumberRangeException;
import me.hugo.savethekweebecs.utils.rainbow.Rainbow;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorUtil {

    private static final String RAW_GRADIENT_HEX_REGEX = "<\\$#[A-Fa-f0-9]{6}>";

    public static ArrayList<String> createGradient(int count, String[] colours) {
        Rainbow rainbow = new Rainbow();

        try {
            rainbow.setNumberRange(1, count);
            rainbow.setSpectrum(colours);
        } catch (HomogeneousRainbowException | InvalidColourException | NumberRangeException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> hexCodes = new ArrayList<String>();
        for (int i = 1; i <= count; i++) {
            hexCodes.add("#" + rainbow.colourAt(i));
        }
        return hexCodes;
    }

    public static String createGradFromString(String name, String[] colours) {
        int count = name.length();
        if (Math.min(count, colours.length) < 2) {
            return name;
        }

        ArrayList<String> cols = createGradient(count, colours);

        String colourCodes = "";
        for (int i = 0; i < cols.size(); i++) {
            colourCodes += ChatColor.of(cols.get(i)) + "" + name.charAt(i);
        }
        return colourCodes;
    }

    public static String createGradFromStrings(String name, boolean bold, String... colours) {
        int count = name.length();
        if (Math.min(count, colours.length) < 2) {
            return name;
        }

        ArrayList<String> cols = createGradient(count, colours);

        String colourCodes = "";
        for (int i = 0; i < cols.size(); i++) {
            colourCodes += ChatColor.of(cols.get(i)) + (bold ? "§l" : "") + "" + name.charAt(i);
        }
        return colourCodes;
    }

    public static boolean isHexList(List<String> colours) {
        for (String i : colours) {
            if(!i.matches("#[a-fA-F0-9]{6}")){
                return false;
            }
        };
        return true;
    }

    public static boolean valueIn(Object[] arr, Object value) {
        for (Object o : arr) {
            if(o == value){
                return true;
            }
        }
        return false;
    }

    public static String insertFades(String msg, String fromHex, String toHex, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean magic) {
        msg = msg.replaceAll("&k", "");
        msg = msg.replaceAll("&l", "");
        msg = msg.replaceAll("&m", "");
        msg = msg.replaceAll("&n", "");
        msg = msg.replaceAll("&o", "");
        int length = msg.length();
        Color fromRGB = Color.decode(fromHex);
        Color toRGB = Color.decode(toHex);
        double rStep = Math.abs((double) (fromRGB.getRed() - toRGB.getRed()) / length);
        double gStep = Math.abs((double) (fromRGB.getGreen() - toRGB.getGreen()) / length);
        double bStep = Math.abs((double) (fromRGB.getBlue() - toRGB.getBlue()) / length);
        if (fromRGB.getRed() > toRGB.getRed()) rStep = -rStep; //200, 100
        if (fromRGB.getGreen() > toRGB.getGreen()) gStep = -gStep; //200, 100
        if (fromRGB.getBlue() > toRGB.getBlue()) bStep = -bStep; //200, 100
        Color finalColor = new Color(fromRGB.getRGB());
        msg = msg.replaceAll(RAW_GRADIENT_HEX_REGEX, "");
        msg = msg.replace("", "<$>");
        for (int index = 0; index <= length; index++) {
            int red = (int) Math.round(finalColor.getRed() + rStep);
            int green = (int) Math.round(finalColor.getGreen() + gStep);
            int blue = (int) Math.round(finalColor.getBlue() + bStep);
            if (red > 255) red = 255; if (red < 0) red = 0;
            if (green > 255) green = 255; if (green < 0) green = 0;
            if (blue > 255) blue = 255; if (blue < 0) blue = 0;
//            System.out.println(red);
            /*System.out.println(green);
            System.out.println(blue);*/
            finalColor = new Color(red, green, blue);
            String hex = "#" + Integer.toHexString(finalColor.getRGB()).substring(2);
            String formats = "";
            if (bold) formats += ChatColor.BOLD;
            if (italic) formats += ChatColor.ITALIC;
            if (underlined) formats += ChatColor.UNDERLINE;
            if (strikethrough) formats += ChatColor.STRIKETHROUGH;
            if (magic) formats += ChatColor.MAGIC;
            msg = msg.replaceFirst("<\\$>", "" + ChatColor.of(hex) + formats);
        }
        return msg;
    }

}
