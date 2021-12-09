package me.hugo.savethekweebecs.utils.gui;

import me.hugo.savethekweebecs.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaginatedGUI {

    private List<MenuHandler> pages;
    private String title;
    private Integer index;
    private ItemStack indicator;
    private MenuHandler previousMenu;
    private int size;
    private String pageFormat;

    public PaginatedGUI(int size, ItemStack indicator, String title, String pageFormat, MenuHandler previousMenu) {
        this.title = title;
        this.previousMenu = previousMenu;
        this.size = size;
        this.indicator = indicator;
        this.pageFormat = pageFormat;
        this.pages = new ArrayList<>();

        index = 0;
    }

    public PaginatedGUI(int size, ItemStack indicator, String title, PageFormat pageFormat, MenuHandler previousMenu) {
        this.previousMenu = previousMenu;
        this.title = title;
        this.size = size;
        this.indicator = indicator;
        this.pageFormat = pageFormat.getFormat();
        this.pages = new ArrayList<>();

        index = 0;
    }

    public int[] lookUpItem(ItemStack item) {
        int page = 0;
        for (MenuHandler menuHandler : new ArrayList<>(pages)) {
            Inventory inventory = menuHandler.getInventory();
            int slot = inventory.first(item);

            if (slot != -1) {
                return new int[]{page, slot};
            }
            page++;
        }
        return null;
    }

    public boolean replaceItem(ItemStack replacing, Icon icon) {
        boolean found = false;
        for (MenuHandler menuHandler : new ArrayList<>(pages)) {
            Inventory inventory = menuHandler.getInventory();
            int slot = inventory.first(replacing);

            if (slot != -1) {
                menuHandler.setIcon(slot, icon);
                found = true;
                break;
            }
        }
        return found;
    }

    public void replaceAll(ItemStack replacing, Icon icon) {
        for (MenuHandler menuHandler : new ArrayList<>(pages)) {
            Inventory inventory = menuHandler.getInventory();
            int slot = inventory.first(replacing);

            if (slot != -1) {
                menuHandler.setIcon(slot, icon);
            }
        }
    }

    public void changeIndicator(ItemStack newItem) {
        this.indicator = newItem;
        for (MenuHandler page : pages) {
            page.setIndicator(newItem);
        }
    }

    public void setItem(Icon icon, int page, int slot) {
        if (pages.size() == 0)
            createNewPage();
        pages.get(page).setIcon(slot, icon);
    }

    public int[] addItem(Icon icon) {
        if (pages.size() == 0)
            createNewPage();

        int i = -1;
        int index = 0;

        for (MenuHandler menuHandler : new ArrayList<>(pages)) {
            List<Integer> slotList = menuHandler.getFormatSlots();
            int lastSlot = slotList.get(slotList.size() - 1);

            if (menuHandler.getInventory().getItem(lastSlot) == null) {
                i = menuHandler.addItem(icon);
                break;
            } else if (pages.size() - 1 == index) {
                i = createNewPage().addItem(icon);
                index++;
                break;
            }
            index++;
        }

        return new int[]{index, i};
    }

    public void open(Player player) {
        player.openInventory(pages.get(0).getInventory());
    }

    public void open(Player player, int index) {
        player.openInventory(pages.get(index).getInventory());
    }

    public MenuHandler createNewPage() {
        final int page = index + 1;
        final int previousIndex = index - 1;
        final Integer thatIndex = Integer.valueOf(index);

        MenuHandler newPage = new MenuHandler(size, title, pageFormat, indicator);

        newPage.setIcon(newPage.getInventory().getSize() - 6,
                new Icon(new ItemBuilder(page == 1 && previousMenu == null ? Material.DARK_OAK_DOOR : Material.ARROW).setName("&a" + (page == 1 ? (previousMenu == null ? "Close" : "Go Back") : "Previous Page &7(" + index + ")")).toItemStack()).addClickAction((player, type) -> {
                    if (page != 1) {
                        open(player, previousIndex);
                    } else if (previousMenu != null)
                        player.openInventory(previousMenu.getInventory());
                    else
                        player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0f, 1.0f);
                }));

        if (index >= 1) {
            MenuHandler lastMenuHandler = pages.get(previousIndex);
            lastMenuHandler.setIcon(lastMenuHandler.getInventory().getSize() - 4, new Icon(new ItemBuilder(Material.ARROW)
                    .setName("&aNext Page &7(" + page + ")").toItemStack()).addClickAction((player, type) -> {
                open(player, thatIndex);
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1.0f, 1.0f);
            }));
        }

        pages.add(newPage);

        index++;
        return newPage;
    }

    public void reset() {
        pages.clear();

        index = 0;
    }

    public MenuHandler getFirstPage() {
        return pages.get(0);
    }

    public MenuHandler getPage(int page) {
        return pages.get(page);
    }

    public enum PageFormat {

        ONE_ROW("---------"
                + "-XXXXXXX-"),

        ONE_ROW_WITHOUT_SIDES("---------"
                + "--XXXXX--"),
        TWO_ROWS("---------"
                + "-XXXXXXX-"
                + "-XXXXXXX-"),

        TWO_ROWS_TO_THE_LEFT("---------"
                + "-XXXXXX--"
                + "-XXXXXX--"),

        THREE_ROWS("---------"
                + "-XXXXXXX-"
                + "-XXXXXXX-"
                + "-XXXXXXX-"),
        THREE_ROWS_WITHOUT_SIDES("---------"
                + "--XXXXX--"
                + "--XXXXX--"
                + "--XXXXX--"),

        ONE_UPGRADE("---------"
                + "-X--X----"),

        THREE_UPGRADES("---------"
                + "-X-XXX---"),

        FOUR_UPGRADES("---------"
                + "-X-XXXX--"),

        FIVE_UPGRADES("---------" +
                "-X-XXXXX-"),

        SIX_UPGRADES("---------" +
                "X-XXXXXX-");

        private String format;

        PageFormat(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }
}

