package me.hugo.savethekweebecs.game;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.world.SlimeWorld;
import me.hugo.savethekweebecs.SaveTheKweebecs;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class GameMap {

    private String slimeWorldName;
    private SlimeWorld slimeWorld;
    private World world;

    public GameMap(String slimeWorldName) {
        this.slimeWorldName = slimeWorldName;
        GameMap gameMap = this;
        SaveTheKweebecs main = SaveTheKweebecs.getPlugin();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    SlimePlugin slimePlugin = main.getSlimePlugin();
                    SlimeWorld slimeWorld = slimePlugin.loadWorld(main.getSlimePlugin().getLoader("file"), slimeWorldName, true, main.getWorldPropertyManager().DEFAULT_PROPERTIES);
                    setSlimeWorld(slimeWorld);
                } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException ex) {
                    SaveTheKweebecs.getPlugin().getLogger().info("ERROR AL CARGAR EL MUNDO " + slimeWorldName);
                    ex.printStackTrace();
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new Game(gameMap, main);
                    }
                }.runTask(main);
            }
        }.runTaskAsynchronously(main);

    }

    public String getSlimeWorldName() {
        return slimeWorldName;
    }

    public void setSlimeWorldName(String slimeWorldName) {
        this.slimeWorldName = slimeWorldName;
    }

    public SlimeWorld getSlimeWorld() {
        return slimeWorld;
    }

    public void setSlimeWorld(SlimeWorld slimeWorld) {
        this.slimeWorld = slimeWorld;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
