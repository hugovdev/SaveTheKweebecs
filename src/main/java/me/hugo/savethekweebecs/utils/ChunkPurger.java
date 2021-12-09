package me.hugo.savethekweebecs.utils;

import me.hugo.savethekweebecs.SaveTheKweebecs;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public abstract class ChunkPurger {
    private long threshold;
    private boolean save;
    private boolean force;
    private Iterator<Chunk> it;
    private int[] m;

    public ChunkPurger(long threshold, boolean save, boolean force) {
        m = new int[]{0};
        this.threshold = threshold;
        this.save = save;
        this.force = force;
    }

    public void purge(List<Chunk> list) {
        if (it == null) {
            m[0] = 0;
            long mx = System.currentTimeMillis();

            new BukkitRunnable() {
                @Override
                public void run() {
                    Iterator<Chunk> it = list.iterator();
                    long msx = System.currentTimeMillis();
                    int[] lx = new int[]{0};

                    while (System.currentTimeMillis() - msx < threshold && it.hasNext()) {
                        Chunk c = it.next();

                        for (Entity i : c.getEntities()) {
                            if (i instanceof Player) {
                                onPlayerInUnloadingChunk((Player) i);
                            }
                        }

                        new BukkitRunnable() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void run() {
                                lx[0]++;
                                m[0]++;
                                c.unload(save);
                                ((CraftWorld) c.getWorld()).getHandle().getChunkProvider().getChunkAt(c.getX(), c.getZ(), false);
                                ((CraftWorld) c.getWorld()).refreshChunk(c.getX(), c.getZ());
                            }
                        }.runTaskLater(SaveTheKweebecs.getPlugin(), 2L);
                    }

                    if (!it.hasNext()) {
                        cancel();
                        it = null;
                        onPurgeComplete(m[0], System.currentTimeMillis() - mx, 0);
                    }
                }
            }.runTaskTimer(SaveTheKweebecs.getPlugin(), 0L, 0L);

            return;
        }
    }

    public boolean isRunning() {
        return it != null;
    }

    public abstract void onPlayerInUnloadingChunk(Player p);

    public abstract void onPurgeComplete(int purgedChunks, long time, int cpt);

    public long getThreshold() {
        return threshold;
    }
}