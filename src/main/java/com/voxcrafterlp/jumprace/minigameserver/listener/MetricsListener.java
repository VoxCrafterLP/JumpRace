package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.api.events.PlayerCompleteModuleEvent;
import com.voxcrafterlp.jumprace.api.events.TeamWinEvent;
import org.bstats.charts.SingleLineChart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.04.2021
 * Time: 23:22
 * Project: JumpRace
 */

public class MetricsListener implements Listener {

    private static int modulesCompleted = 0;

    @EventHandler
    public void onComplete(PlayerCompleteModuleEvent event) {
        modulesCompleted++;
    }

    @EventHandler
    public void onWin(TeamWinEvent event) {
        JumpRace.getInstance().getMetrics().addCustomChart(new SingleLineChart("modules_completed", () -> modulesCompleted));
    }

}
