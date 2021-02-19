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

        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "newmodule":
                        new ModuleSetup(player);
                        break;
                    case "list":

                        break;
                    default:
                        this.listCommands(player);
                        break;
                }
                break;

            case 2:
                switch (args[0].toLowerCase()) {
                    case "edit":
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

                        if(JumpRace.getInstance().getEditorSessions().containsKey(player)) {
                            player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You are §calready editing §7a module§8.");
                            return false;
                        }

                        if(!player.getWorld().getName().equalsIgnoreCase("jumprace")) {
                            player.sendMessage(JumpRace.getInstance().getPrefix() + "§aTeleporting to JumpRace world...");
                            player.teleport(new Location(Bukkit.getWorld("jumprace"), 0, 100, 0));
                        }

                        new ModuleEditor(player, module).startEditor();
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
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/jumprace§8: §7Shows this window");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/jumprace newmodule§8: §7Create a new module");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/jumprace list§8: §7Shows a list of all modules");
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§b/jumprace edit <module>§8: §7Edit a module");
        player.sendMessage("§8===================================");
    }

}
