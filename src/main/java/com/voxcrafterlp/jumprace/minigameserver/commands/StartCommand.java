package com.voxcrafterlp.jumprace.minigameserver.commands;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.objects.Countdown;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.03.2021
 * Time: 00:03
 * Project: JumpRace
 */

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        final Player player = (Player) commandSender;

        if(!player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getStartPermission())
                && !player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getAdminPermission())) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("no-permission"));
            return false;
        }

        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-start-not-available"));
            return false;
        }

        final Countdown countdown = JumpRace.getInstance().getGameManager().getLobbyCountdown();

        if(!countdown.isRunning()) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-start-not-running"));
            return false;
        }

        if(countdown.getTimeLeft() < 15) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-start-already-starting"));
            return false;
        }

        countdown.setTimeLeft(15);
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-start-success"));
        player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);

        return false;
    }
}
