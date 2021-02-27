package com.voxcrafterlp.jumprace.objects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.utils.TitleUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 27.02.2021
 * Time: 09:06
 * Project: JumpRace
 */

@Getter @Setter
public class Countdown {

    private final Type type;
    private int timeLeft, taskID;
    private final Runnable runnable;
    private boolean running;

    public Countdown(Type type, Runnable runnable) {
        this.type = type;
        this.timeLeft = type.getDuration();
        this.runnable = runnable;
        this.running = false;
    }

    public void startCountdown() {
        this.running = true;

        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JumpRace.getInstance(), () -> {
            if(this.timeLeft == 0) {
                this.finish();
                Bukkit.getScheduler().cancelTask(this.taskID);
                return;
            }

            switch (timeLeft) {
                case 60:
                case 30:
                case 15:
                case 10:
                case 5:
                    if(this.type == Type.LOBBY)
                        Bukkit.getOnlinePlayers().forEach(player -> new TitleUtil().sendTitle(player, "§bJumpRace", 10, 30, 5));
                case 4:
                case 3:
                case 2:
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The game " + ((this.type == Type.LOBBY) ? "§bstarts" : "§cends") +
                                " §7in " + ((this.type == Type.LOBBY) ? "§b" : "§c") + this.timeLeft +" seconds§8.");
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 10, 10);
                    });
                    break;
                case 1:
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7The game " + ((this.type == Type.LOBBY) ? "§bstarts" : "§cends") +
                                " §7in " + ((this.type == Type.LOBBY) ? "§b" : "§c") + this.timeLeft +" second§8.");
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 10, 10);
                    });
                    break;
            }

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setLevel(this.timeLeft);
                player.setExp(this.timeLeft * ((float) 1/60));
            });

            this.timeLeft--;
        }, 0, 20);
    }

    public void reset(boolean run) {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.running = false;
        this.timeLeft = this.type.getDuration();

        if(run) startCountdown();
    }

    private void finish() {
        this.running = false;
        this.runnable.run();
    }

    @Getter
    public enum Type {

        LOBBY(60),
        ENDING(15);

        private final int duration;

        Type(int duration) {
            this.duration = duration;
        }

    }
}
