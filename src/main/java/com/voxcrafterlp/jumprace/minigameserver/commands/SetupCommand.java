package com.voxcrafterlp.jumprace.minigameserver.commands;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.minigameserver.setup.objects.MapSetup;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 25.02.2021
 * Time: 14:45
 * Project: JumpRace
 */

public class SetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;

        if(!player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getSetupPermission())
                && !player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getAdminPermission())) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("no-permission"));
            return false;
        }

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "lobby":
                        JumpRace.getInstance().getLocationManager().setLobbyLocation(player.getLocation().clone());
                        JumpRace.getInstance().getLocationManager().saveData();

                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1,1);
                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-lobby-success"));
                        break;
                    default:
                        this.listCommands(player);
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "map":
                        new MapSetup(player, args[1]);
                        break;
                    default:
                        this.listCommands(player);
                        break;
                }
                break;
            default:
                this.listCommands(player);
                break;
        }

        return false;
    }

    private void listCommands(Player player) {
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("setup-help-1"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-help-2"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("setup-help-3"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("setup-help-4"));
    }
}
