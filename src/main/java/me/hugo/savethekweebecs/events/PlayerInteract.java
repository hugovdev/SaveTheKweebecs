package me.hugo.savethekweebecs.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import me.hugo.savethekweebecs.game.Game;
import me.hugo.savethekweebecs.game.GameState;
import me.hugo.savethekweebecs.globalgame.GlobalGame;
import me.hugo.savethekweebecs.player.GamePlayer;
import me.hugo.savethekweebecs.utils.gui.ClickAction;
import me.hugo.savethekweebecs.utils.gui.Icon;
import me.hugo.savethekweebecs.utils.gui.MenuHandler;
import me.hugo.savethekweebecs.utils.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class PlayerInteract implements Listener {

    private SaveTheKweebecs main;

    public PlayerInteract(SaveTheKweebecs main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemOnHand = player.getEquipment().getItemInMainHand();

        Block block = event.getClickedBlock();
        if (block != null && block.getType() != null && (block.getType().name().startsWith("POTTED_") || block.getType() == Material.FLOWER_POT)) {
            if (main.getPlayerManager().getGamePlayer(player).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
                event.setCancelled(true);
                return;
            }
        }

        if (itemOnHand == null || !event.getAction().name().contains("CLICK") || event.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if (itemOnHand.isSimilar(main.getTeamSelector())) {
            event.setCancelled(true);
            player.openInventory(main.getPlayerGame(player).getTeamSelectorMenu().getInventory());
        } else if (itemOnHand.isSimilar(main.getArenaCreator())) {
            event.setCancelled(true);
            ArenaSetup.updateArenaCreator(player);
            player.openInventory(main.getArenaSetupMenu().getInventory());
        } else if (itemOnHand.isSimilar(main.getArenaSelector())) {
            event.setCancelled(true);
            if (GlobalGame.SAVE_THE_KWEEBECS.isOpen()) {
                main.getArenaSelectorMenu().open(player);
            } else {
                player.sendMessage("§bSave The Kweebecs§c is currently closed! Select another game on the §aGame Selector§c!");
            }
        } else if (itemOnHand.isSimilar(main.getSuitSelector())) {
            event.setCancelled(true);
            main.getPlayerManager().getGamePlayer(player).getSuitMenu().open(player);
        } else if (itemOnHand.isSimilar(main.getBackToLobby())) {
            event.setCancelled(true);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            out.writeUTF("Connect");
            out.writeUTF("lobby_1");

            player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
        } else if (itemOnHand.isSimilar(main.getArenaLeaveItem())) {
            event.setCancelled(true);
            Game game = main.getPlayerGame(player);
            game.leaveGame(player);
        } else if (itemOnHand.isSimilar(main.getKitManager().GAME_SHOP)) {
            event.setCancelled(true);
            Game game = main.getPlayerGame(player);

            if (game != null) {
                main.getPlayerManager().getGamePlayer(player).openShopMenu(player);
            } else {
                player.sendMessage("§cYou must be playing to use this item!");
            }
        } else if (itemOnHand.getType().name().contains("BANNER")) {
            if (main.getPlayerGame(player) == null && main.getPlayerManager().getGamePlayer(player).getCurrentGame() == GlobalGame.SAVE_THE_KWEEBECS) {
                event.setCancelled(true);
                main.getPlayerManager().getGamePlayer(player).getBannerMenu().open(player);
            }
        } else if (itemOnHand.getItemMeta() != null && itemOnHand.getItemMeta().getDisplayName() != null && itemOnHand.getItemMeta().getDisplayName().contains("Kweebec Locator")) {
            Game game = main.getPlayerGame(player);
            GamePlayer gamePlayer = main.getPlayerManager().getGamePlayer(player);

            if (gamePlayer.isOnCooldown("kweebecLocator")) {
                if (game != null && game.getGameState() == GameState.INGAME) {
                    gamePlayer.setCooldown("kweebecLocator", 5);
                    boolean isTrork = game.getTrorkPlayers().contains(player);

                    if (isTrork) {
                        player.sendMessage("§aLocating closest Kweebec...");
                        double closestDistance = Double.MAX_VALUE;
                        Location closest = null;

                        for (Player kweebecPlayer : game.getKweebecPlayers()) {
                            double newDistance = kweebecPlayer.getLocation().distance(player.getLocation());

                            if (kweebecPlayer.getGameMode() != GameMode.SPECTATOR && closestDistance > newDistance) {
                                closestDistance = newDistance;
                                closest = kweebecPlayer.getLocation();
                            }
                        }
                        if (closest != null) {
                            DecimalFormat decimalFormat = new DecimalFormat("#.#");
                            player.setCompassTarget(closest);
                            player.sendMessage("§aDistance from closest Kweebec: §b" + decimalFormat.format(closestDistance) + "§a. Your compass is now pointing to that location.");
                        } else {
                            player.sendMessage("§cGame couldn't find any alive kweebec to track!");
                        }
                    } else {
                        player.sendMessage("§aLocating closest kidnapped Kweebec...");
                        double closestDistance = Double.MAX_VALUE;
                        Location closest = null;

                        for (NPC npc : game.getRemainingKweebecs()) {
                            double newDistance = npc.getLocation().distance(player.getLocation());

                            if (closestDistance > newDistance) {
                                closestDistance = newDistance;
                                closest = npc.getLocation();
                            }
                        }
                        if (closest != null) {
                            DecimalFormat decimalFormat = new DecimalFormat("#.#");
                            player.setCompassTarget(closest);
                            player.sendMessage("§aDistance from closest kidnapped Kweebec: §b" + decimalFormat.format(closestDistance) + "§a. Your compass is now pointing to that location.");
                        } else {
                            player.sendMessage("§cGame couldn't find any kidnapped kweebec to track!");
                        }
                    }
                } else {
                    player.sendMessage("§cYou need to be playing to use this item!");
                }
            } else {
                player.sendMessage("§cWait §e" + gamePlayer.getRemainingCooldown("kweebecLocator") + "s§c before using the §bKweebec Locator§c!");
            }
        }
    }

    @EventHandler
    public void onspectatorLeaveClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            if (itemStack.getType() == Material.RED_BED && player.getGameMode() == GameMode.SPECTATOR) {
                main.getPlayerGame(player).leaveGame(player);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof MenuHandler) {
            event.setCancelled(true);

            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();

                ItemStack itemStack = event.getCurrentItem();
                if (itemStack == null || itemStack.getType() == Material.AIR) return;

                MenuHandler customHolder = (MenuHandler) event.getView().getTopInventory().getHolder();

                Icon icon = customHolder.getIcon(event.getRawSlot());
                if (icon == null) return;

                for (ClickAction clickAction : icon.getClickActions()) {
                    clickAction.execute(player, event.getClick(), main);
                }
            }
        }
    }
}
