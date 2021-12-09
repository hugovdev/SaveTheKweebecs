package me.hugo.savethekweebecs.utils;

import me.hugo.savethekweebecs.utils.messages.DefaultFontInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

    public static final String DARK_STAR = "★";
    public static final String WHITE_STAR = "☆";
    public static final String CIRCLE_BLANK_STAR = "✪";
    public static final String BIG_BLOCK = "█";
    public static final String SMALL_BLOCK = "▌";
    public static final String SMALL_DOT = "•";
    public static final String LARGE_DOT = "●";
    public static final String HEART = "♥";
    public static final String SMALL_ARROWS_RIGHT = "»";
    public static final String SMALL_ARROWS_LEFT = "«";
    public static final String ALERT = "⚠";
    public static final String RADIOACTIVE = "☢";
    public static final String BIOHAZARD = "☣";
    public static final String PLUS = "✚";
    public static final String BIG_HORIZONTAL_LINE = "▍";
    public static final String SMALL_HORIZONTAL_LINE = "▏";
    public static final String PLAY = "▶";
    public static final String GOLD_ICON = "❂";
    public static final String CROSS = "✖";
    public static final String SLIM_CROSS = "✘";
    public static final String BOXED_CROSS = "☒";
    public static final String CHECKMARK = "✔";
    public static final String BOXED_CHECKMARK = "☑";
    public static final String LETTER = "✉";
    public static final String BLACK_CHESS_KING = "♚";
    public static final String BLACK_CHESS_QUEEN = "♛";
    public static final String SKULL_AND_CROSSBONES = "☠";
    public static final String WHITE_FROWNING_FACE = "☹";
    public static final String WHITE_SMILING_FACE = "☺";
    public static final String BLACK_SMILING_FACE = "☻";
    public static final String PICK = "⛏";

    public static String format(String text) {
        return text == null ? null : net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");

    public static String colorize(String message) {
        Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', message));
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
        }

        return matcher.appendTail(buffer).toString();
    }

    private final static int CENTER_PX = 154;

    public static void sendCenteredMessage(Player player, String message){
        if(message == null || message.equals("")) player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    public static String buildFinalLoreLine(String line) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&e▶ " + line + " ◀");
    }

    public static String[] split(final String s, final String sep) {
        // convert a String s to an Array, the elements
        // are delimited by sep
        final Vector<Integer> tokenIndex = new Vector<Integer>(10);
        final int len = s.length();
        int i;

        // Find all characters in string matching one of the separators in 'sep'
        for (i = 0; i < len; i++)
            if (sep.indexOf(s.charAt(i)) != -1)
                tokenIndex.addElement(i);

        final int size = tokenIndex.size();
        final String[] elements = new String[size + 1];

        // No separators: return the string as the first element
        if (size == 0)
            elements[0] = s;
        else {
            // Init indexes
            int start = 0;
            int end = (tokenIndex.elementAt(0)).intValue();
            // Get the first token
            elements[0] = s.substring(start, end);

            // Get the mid tokens
            for (i = 1; i < size; i++) {
                // update indexes
                start = (tokenIndex.elementAt(i - 1)).intValue() + 1;
                end = (tokenIndex.elementAt(i)).intValue();
                elements[i] = s.substring(start, end);
            }
            // Get last token
            start = (tokenIndex.elementAt(i - 1)).intValue() + 1;
            elements[i] = (start < s.length()) ? s.substring(start) : "";
        }

        return elements;
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public static String getDurationString(int seconds) {

        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private static String result(long result) {
        return result < 10 ? "0" + result : String.valueOf(result);
    }

    public static String getFormatTime(long time) {
        if (time > 0) {
            time = time / 1000;
            long days, hours, minutes, seconds;
            days = time / 86400;
            seconds = time % 86400;
            hours = seconds / 3600;
            seconds = seconds % 3600;
            minutes = seconds / 60;
            seconds = seconds % 60;
            String formatTime = "";
            if (days != 0) formatTime += result(days) + "d";
            if (hours != 0) formatTime += (formatTime.length() == 0 ? "" : " ") + (result(hours) + "h");
            if (minutes != 0) formatTime += (formatTime.length() == 0 ? "" : " ") + (result(minutes) + "m");
            if (seconds != 0) formatTime += (formatTime.length() == 0 ? "" : " ") + (result(seconds) + "s");
            return formatTime;
        } else return "00d 00h 00m 00s";
    }


    public static int getPercentage(int max, int current) {
        int total, score;
        int percentage;
        total = max;
        score = current;
        percentage = (score * 100 / total);
        return percentage;
    }

    public static String parseAndFormat(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static String[] wordWrap(String rawString, int lineLength) {
        // A null string is a single line
        rawString = format(rawString);
        if (rawString == null) {
            return new String[]{""};
        }

        // A string shorter than the lineWidth is a single line
        if (rawString.length() <= lineLength && !rawString.contains("\n")) {
            return new String[]{rawString};
        }

        char[] rawChars = (rawString + ' ').toCharArray(); // add a trailing space to trigger pagination
        StringBuilder word = new StringBuilder();
        StringBuilder line = new StringBuilder();
        List<String> lines = new LinkedList<String>();
        int lineColorChars = 0;

        for (int i = 0; i < rawChars.length; i++) {
            char c = rawChars[i];

            // skip chat color modifiers
            if (c == ChatColor.COLOR_CHAR) {
                word.append(ChatColor.getByChar(rawChars[i + 1]));
                lineColorChars += 2;
                i++; // Eat the next character as we have already processed it
                continue;
            }

            if (c == ' ' || c == '\n') {
                if (line.length() == 0 && word.length() > lineLength) { // special case: extremely long word begins a line
                    for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(partialWord);
                    }
                } else if (line.length() + 1 + word.length() - lineColorChars == lineLength) { // Line exactly the correct length...newline
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append(word);
                    lines.add(line.toString());
                    line = new StringBuilder();
                    lineColorChars = 0;
                } else if (line.length() + 1 + word.length() - lineColorChars > lineLength) { // Line too long...break the line
                    for (String partialWord : word.toString().split("(?<=\\G.{" + lineLength + "})")) {
                        lines.add(line.toString());
                        line = new StringBuilder(partialWord);
                    }
                    lineColorChars = 0;
                } else {
                    if (line.length() > 0) {
                        line.append(' ');
                    }
                    line.append(word);
                }
                word = new StringBuilder();

                if (c == '\n') { // Newline forces the line to flush
                    lines.add(line.toString());
                    line = new StringBuilder();
                }
            } else {
                word.append(c);
            }
        }

        if (line.length() > 0) { // Only add the last line if there is anything to add
            lines.add(line.toString());
        }

        // Iterate over the wrapped lines, applying the last color from one line to the beginning of the next
        if (lines.get(0).length() == 0 || lines.get(0).charAt(0) != ChatColor.COLOR_CHAR) {
            lines.set(0, ChatColor.WHITE + lines.get(0));
        }
        for (int i = 1; i < lines.size(); i++) {
            final String pLine = lines.get(i - 1);
            final String subLine = lines.get(i);

            char color = pLine.charAt(pLine.lastIndexOf(ChatColor.COLOR_CHAR) + 1);
            if (subLine.length() == 0 || subLine.charAt(0) != ChatColor.COLOR_CHAR) {
                lines.set(i, ChatColor.getByChar(color) + subLine);
            }
        }

        return lines.toArray(new String[lines.size()]);
    }

    public static String[] toArrayList(String string) {
        String[] lines = wordWrap(string, 30);
        int totalPages = lines.length / 10 + (lines.length % 10 == 0 ? 0 : 1);
        int actualPageNumber = 1 <= totalPages ? 1 : totalPages;
        int from = (actualPageNumber - 1) * 10;
        int to = from + 10 <= lines.length ? from + 10 : lines.length;
        return wordWrap(string, 30);
    }

    public static String toRoman(int number) {
        if(number == 0)
            return "0";
        return String.valueOf(new char[number]).replace('\0', 'I')
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }

    public static double round(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static double roundDown(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        return bd.doubleValue();
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm aa");
        return sdf.format(new Date());
    }

    public static String getRandomString(int length) {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    public static String addLinebreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input);
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > maxLineLength) {
                output.append("_nl");
                lineLen = 0;
            }
            output.append(word);
            lineLen += word.length();
        }
        return output.toString();
    }

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

    public static String getProgressBar(double current, double max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
        double percent = current / max;

        double completedBars = totalBars * percent;
        double notCompletedBars = totalBars - completedBars;

        double progressBars = Math.round(completedBars);
        double leftOver = Math.round(notCompletedBars);

        StringBuilder sb = new StringBuilder();

        sb.append(ChatColor.translateAlternateColorCodes('&', completedColor));
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }

        sb.append(ChatColor.translateAlternateColorCodes('&', notCompletedColor));
        for (int i = 0; i < leftOver; i++) {
            sb.append(symbol);
        }

        return sb.toString();
    }

}
