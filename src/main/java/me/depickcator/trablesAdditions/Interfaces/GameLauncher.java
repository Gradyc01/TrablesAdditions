package me.depickcator.trablesAdditions.Interfaces;

import me.depickcator.trablesAdditions.TrablesAdditions;
import me.depickcator.trablesAdditions.Util.TextUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class GameLauncher {
    protected final TrablesAdditions plugin;
    protected final List<GameSequences> sequence;
    public GameLauncher() {
        plugin = TrablesAdditions.getInstance();
        this.sequence = new ArrayList<>();
//        sequence = new ArrayList<>(settings.getSequence());

    }

    protected abstract List<GameSequences> initSequence();

    public void start() {
        if (canStart()) {
            this.sequence.clear();
            this.sequence.addAll(initSequence());
            loop();
        }
    }

    protected abstract boolean canStart();

//    public abstract void start();

    private void loop() {
        if (sequence.isEmpty()) {
            TextUtil.debugText("GameLauncher", "Sequences Finished");
            end();
            return;
        }
        GameSequences seq = sequence.getFirst();
        TextUtil.debugText("GameLauncher","Running Next Sequence ->" + seq.getSequenceName());
        seq.run(this);
        sequence.removeFirst();
    }


    protected abstract void end();


    /* What each GameStartSequence should call at the end of their run
     * Delays the next sequence by delay ticks
     * delay > 0*/
    public void callback(int delay) {
        TextUtil.debugText("GameLauncher", "Callback called");
        new BukkitRunnable() {
            public void run() {
                loop();
            }
        }.runTaskLater(TrablesAdditions.getInstance(), delay);
    }

    public void callback() {
        callback(40);
    }
}
