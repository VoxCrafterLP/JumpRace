package com.voxcrafterlp.jumprace.builderserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.builderserver.objects.ModuleSetup;
import com.voxcrafterlp.jumprace.modules.enums.EditorMode;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.utils.ModuleEditor;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 03:25
 * Project: JumpRace
 */

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getInventory() == null) return;
        if(event.getInventory().getName() == null) return;

        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(event.getInventory().getName().equals("§cModule difficulty")) {
            if(ModuleSetup.getActiveSetups().containsKey(player)) {
                ModuleSetup setup = ModuleSetup.getActiveSetups().get(player);

                if(setup.getSetupStep() != 2) return;

                switch (event.getSlot()) {
                    case 10:
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §aEASY§7.");
                        setup.setModuleDifficulty(ModuleDifficulty.EASY);
                        setup.nextStep();

                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                        player.closeInventory();
                        break;
                    case 12:
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §6NORMAL§7.");
                        setup.setModuleDifficulty(ModuleDifficulty.NORMAL);
                        setup.nextStep();

                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                        player.closeInventory();
                        break;
                    case 14:
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §cHARD§7.");
                        setup.setModuleDifficulty(ModuleDifficulty.HARD);
                        setup.nextStep();

                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                        player.closeInventory();
                        break;
                    case 16:
                        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You set the module difficulty to §4VERY HARD§7.");
                        setup.setModuleDifficulty(ModuleDifficulty.VERY_HARD);
                        setup.nextStep();

                        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 10,10);
                        player.closeInventory();
                        break;
                    default:
                        player.playSound(player.getLocation(), Sound.NOTE_BASS,1,1);
                        break;
                }

                event.setCancelled(true);
            }
        }

        if(event.getInventory().getName().equals("§cSettings")) {
            event.setCancelled(true);

            if(event.getCurrentItem().getType() == Material.REDSTONE_COMPARATOR) {
                final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(player);
                session.getSettings().setEditorMode(EditorMode.PERFORMANCE);
                session.getSettings().updateInventory();
                session.getModule().stopParticles();

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You changed the editor mode to §c" + session.getSettings().getEditorMode().name() + "§8.");
                return;
            }
            if(event.getCurrentItem().getType() == Material.DIODE) {
                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You §ccan't §7switch to the quick editor§8!");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2, 2);
                return;
            }
            if(event.getCurrentItem().getType() == Material.NAME_TAG) {
                final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(player);

                new AnvilGUI.Builder()
                        .onComplete((user, input) -> {
                            if(input.equalsIgnoreCase("default")) {
                                player.sendMessage(JumpRace.getInstance().getPrefix() + "§cInvalid name");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2, 2);
                                return AnvilGUI.Response.text("");
                            }

                            session.getModule().setName(input);
                            user.sendMessage(JumpRace.getInstance().getPrefix() + "§7You changed the name to§8: §a" + input + "§8.");
                            user.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
                            return AnvilGUI.Response.close();
                        })
                        .text(session.getModule().getName())
                        .itemLeft(new ItemManager(Material.NAME_TAG).setDisplayName(session.getModule().getName()).build())
                        .title("Rename")
                        .plugin(JumpRace.getInstance())
                        .open(player);
                return;
            }
            if(event.getCurrentItem().getType() == Material.ANVIL) {
                Inventory inventory = Bukkit.createInventory(null, 27, "§cNew module difficulty");

                for(int i = 0; i<27; i++)
                    inventory.setItem(i, new ItemManager(Material.STAINED_GLASS_PANE,15).setNoName().build());

                inventory.setItem(10, new ItemManager(Material.WOOL,5).setDisplayName("§aEasy").build());
                inventory.setItem(12, new ItemManager(Material.WOOL,1).setDisplayName("§6Normal").build());
                inventory.setItem(14, new ItemManager(Material.WOOL,14).setDisplayName("§cHard").build());
                inventory.setItem(16, new ItemManager(Material.WOOL,15).setDisplayName("§4Very hard").build());

                player.sendMessage(JumpRace.getInstance().getPrefix() + "§7Please choose the new §bdifficulty §7of the §bmodule§8.");
                player.openInventory(inventory);
                player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 2);
                return;
            }
            if(event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(player);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
                session.startEditorSetup();
            }
            if(event.getCurrentItem().getType() == Material.BARRIER) {
                final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(player);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);

                session.resetToPreviousInventory();
                Bukkit.getScheduler().cancelTask(session.actionbarTaskID);
                session.getModule().stopParticles();
                final Location[] borders = (session.getSettings().getEditorMode() == EditorMode.QUICK) ? session.getModule().getModuleBorders() : null;
                session.clearArea(borders);
                JumpRace.getInstance().getEditorSessions().remove(player);
            }
        }

        if(event.getInventory().getName().equals("§cNew module difficulty")) {
            event.setCancelled(true);
            final ModuleEditor session = JumpRace.getInstance().getEditorSessions().get(player);

            if(event.getSlot() == 10)
                changeModuleDifficulty(player, session, ModuleDifficulty.EASY);
            if(event.getSlot() == 12)
                changeModuleDifficulty(player, session, ModuleDifficulty.NORMAL);
            if(event.getSlot() == 14)
                changeModuleDifficulty(player, session, ModuleDifficulty.HARD);
            if(event.getSlot() == 16)
                changeModuleDifficulty(player, session, ModuleDifficulty.VERY_HARD);
        }
    }

    private void changeModuleDifficulty(Player player, ModuleEditor session, ModuleDifficulty moduleDifficulty) {
        session.getModule().setModuleDifficulty(moduleDifficulty);
        player.sendMessage(JumpRace.getInstance().getPrefix() + "§7You §asuccessfully §7changed the module difficulty to§8: " + moduleDifficulty.getDisplayName());
        player.playSound(player.getLocation(), Sound.LEVEL_UP,1,1);
        session.getSettings().openInventory();
    }

}
