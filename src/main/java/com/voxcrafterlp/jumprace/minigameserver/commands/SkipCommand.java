package com.voxcrafterlp.jumprace.minigameserver.commands;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.objects.Countdown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 24.04.2021
 * Time: 11:30
 * Project: JumpRace
 */

public class SkipCommand implements CommandExecutor {

    private static final int MAX_MODULES = 10;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        final Player player = (Player) commandSender;
        final Countdown countdown = JumpRace.getInstance().getGameManager().getJumpingCountdown();

        if(JumpRace.getInstance().getGameManager().getGameState() != GameState.JUMPING || countdown.getTimeLeft() <= 10) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-skip-not-available"));
            return false;
        }

        if(JumpRace.getInstance().getGameManager().getModuleRows().get(player).getModulesCompleted() != MAX_MODULES) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-skip-not-available"));
            return false;
        }

        countdown.setTimeLeft(10); //Set the countdown to 10 seconds
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("message-command-skip-success"));
        return true;
    }
}
