package com.voxcrafterlp.jumprace.builderserver.commands;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 12:49
 * Project: JumpRace
 */

public class JumpRaceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("edit")) {
                final String moduleName = args[1];
                boolean found = false;
                Module module = null;

                for(Module modules : JumpRace.getInstance().getModuleLoader().getModuleList()) {
                    if(modules.getName().equalsIgnoreCase(moduleName)) {
                        found = true;
                        module = modules;
                    }
                }

                if(!found) {
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§cInvalid module!");
                    return false;
                }

                if(!player.getWorld().getName().equalsIgnoreCase("jumprace")) {
                    player.sendMessage(JumpRace.getInstance().getPrefix() + "§aTeleporting to JumpRace world...");
                    player.teleport(new Location(Bukkit.getWorld("jumprace"), 0, 100, 0));
                }

                new ModuleEditor(player, module).startEditor();
            }
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("newmodule")) {
                new ModuleSetup(player);
            }
        }

        return false;
    }
}
