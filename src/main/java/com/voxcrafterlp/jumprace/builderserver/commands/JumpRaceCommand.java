package com.voxcrafterlp.jumprace.builderserver.commands;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 12:49
 * Project: JumpRace
 */

public class JumpRaceCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) return false;

        final Player player = (Player) commandSender;

        if(!player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getBuilderPermission())
                && !player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getAdminPermission())) {
            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("no-permissions"));
            return false;
        }

        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "newmodule":
                        if(!JumpRace.getInstance().getEditorSessions().isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("already-editing"));
                            return false;
                        }
                        if(!ModuleSetup.getActiveSetups().isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("already-editing"));
                            return false;
                        }

                        new ModuleSetup(player);
                        break;
                    case "list":
                        final List<Module> modules = JumpRace.getInstance().getModuleLoader().getModuleList();

                        if(modules.isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("no-modules-found"));
                            return false;
                        }

                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-list"));
                        modules.forEach(module -> player.sendMessage(JumpRace.getInstance().getPrefix() + "§8- §b" + module.getName()));
                        break;
                    case "reload":
                        JumpRace.getInstance().getModuleLoader().reloadModules();

                        if(JumpRace.getInstance().getModuleLoader().getModuleList().isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("no-modules-found"));
                            return false;
                        }

                        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-reload-success",
                                String.valueOf(JumpRace.getInstance().getModuleLoader().getModuleList().size())));
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
                        final Module module = JumpRace.getInstance().getModuleLoader().getModuleList().stream().filter(modules -> modules.getName().equalsIgnoreCase(moduleName)).findAny().orElse(null);

                        if(module == null) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("invalid-module"));
                            return false;
                        }

                        if(!JumpRace.getInstance().getEditorSessions().isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("already-editing"));
                            return false;
                        }
                        if(!ModuleSetup.getActiveSetups().isEmpty()) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("already-editing"));
                            return false;
                        }

                        if(!player.getWorld().getName().equalsIgnoreCase("jumprace")) {
                            player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("teleport"));
                            player.teleport(new Location(Bukkit.getWorld("jumprace"), 0, 100, 0));
                        }

                        new ModuleEditor(player, module).startEditor();
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
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-help-1"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-help-2"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-help-3"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-help-4"));
        player.sendMessage(JumpRace.getInstance().getLanguageLoader().getTranslationByKeyWithPrefix("command-jumprace-help-5"));
        player.sendMessage("§8===================================");
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) return Collections.emptyList();
        final Player player = (Player) commandSender;

        if(command.getName().equalsIgnoreCase("jumprace")
                || command.getName().equalsIgnoreCase("jr")){

            if(!player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getBuilderPermission())
                    && !player.hasPermission(JumpRace.getInstance().getJumpRaceConfig().getAdminPermission()))
                return Collections.emptyList();

            final List<String> completions = Lists.newCopyOnWriteArrayList();

            int length = args.length;

            for(String string : args) {
                if(string.equals("") || string.equals(" "))
                    length--;
            }

            switch (length) {
                case 0:
                    completions.add("newmodule");
                    completions.add("list");
                    completions.add("edit");
                    completions.add("reload");
                    break;
                case 1:
                    if(args[0].equalsIgnoreCase("edit")) {
                        JumpRace.getInstance().getModuleLoader().getModuleList()
                                .forEach(module -> completions.add(module.getName()));

                        return completions;
                    }

                    final String search = args[0].toLowerCase();
                    Stream.of("newmodule", "list", "edit", "reload")
                            .filter(string -> string.toLowerCase().startsWith(search))
                            .forEach(completions::add);
                    break;
                case 2:
                    if(args[0].equalsIgnoreCase("edit")) {
                        final String searchModule = args[1].toLowerCase();
                        JumpRace.getInstance().getModuleLoader().getModuleList().stream()
                                .filter(module -> module.getName().toLowerCase().startsWith(searchModule))
                                .forEach(module -> completions.add(module.getName()));
                    }
                    break;
                default:
                    System.out.println("test3");
                    break;
            }
            return completions;
        }
        return Collections.emptyList();
    }
}
