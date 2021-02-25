package com.voxcrafterlp.jumprace.minigameserver.commands;

import com.voxcrafterlp.jumprace.JumpRace;
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

        if(!player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getSetupPermission())) {
            player.sendMessage(JumpRace.getInstance().getPrefix() + "&7You §care not permitted §7to execute this command!");
            return false;
        }

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "lobby":

                        break;
                    default:
                        this.listCommands(player);
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "map":
                        final String mapname = args [1];
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
        player.sendMessage("§8===================================");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/setup lobby§8: §7Set the lobby location");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/setup map <map>§8: §7Setup a pvp map");
        player.sendMessage("§8===================================");
    }
}
