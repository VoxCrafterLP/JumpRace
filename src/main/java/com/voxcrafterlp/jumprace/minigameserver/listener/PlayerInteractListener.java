package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.GameState;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 02.03.2021
 * Time: 20:55
 * Project: JumpRace
 */

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.LOBBY) {
            if(event.getItem() == null) return;
            if(event.getItem().getItemMeta().getDisplayName() == null) return;

            if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            if(event.getItem().getItemMeta().getDisplayName().equals("§bTeam Selector")) {
                player.openInventory(JumpRace.getInstance().getInventoryManager().getTeamSelectorInventory());
                player.playSound(player.getLocation(), Sound.CHEST_OPEN,1,1);
                return;
            }
            if(event.getItem().getItemMeta().getDisplayName().equals("§cLeave")) {
                player.kickPlayer(JumpRace.getInstance().getPrefix() + "§7You §bleft §7the game§8.");
                return;
            }
        }

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING) {
            if(event.getAction() == Action.PHYSICAL) {
                if(event.getClickedBlock().getType() == Material.GOLD_PLATE)
                    JumpRace.getInstance().getGameManager().getModuleRows().get(event.getPlayer()).triggerGoldPlate(event.getClickedBlock().getLocation());
                return;
            }

            if(event.getClickedBlock() == null) return;

            if(event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                event.setCancelled(true);
                event.getPlayer().closeInventory();

                JumpRace.getInstance().getGameManager().getChestLoot().openChest(player, event.getClickedBlock().getLocation(),
                        JumpRace.getInstance().getGameManager().getModuleRows().get(player).getCurrentModuleDifficulty());
            }
        }

        if(JumpRace.getInstance().getGameManager().getGameState() == GameState.JUMPING ||
                JumpRace.getInstance().getGameManager().getGameState() == GameState.DEATHMATCH) {

            if(event.getItem() == null) return;
            final ItemStack itemStack = event.getItem();

            switch (itemStack.getType()) {
                case LEATHER_HELMET:
                case GOLD_HELMET:
                case CHAINMAIL_HELMET:
                case IRON_HELMET:
                case DIAMOND_HELMET:
                    this.setArmor(player, ArmorType.HELMET);
                    break;
                case LEATHER_CHESTPLATE:
                case GOLD_CHESTPLATE:
                case CHAINMAIL_CHESTPLATE:
                case IRON_CHESTPLATE:
                case DIAMOND_CHESTPLATE:
                    this.setArmor(player, ArmorType.CHESTPLATE);
                    break;
                case LEATHER_LEGGINGS:
                case GOLD_LEGGINGS:
                case CHAINMAIL_LEGGINGS:
                case IRON_LEGGINGS:
                case DIAMOND_LEGGINGS:
                    this.setArmor(player, ArmorType.LEGGINGS);
                    break;
                case LEATHER_BOOTS:
                case GOLD_BOOTS:
                case CHAINMAIL_BOOTS:
                case IRON_BOOTS:
                case DIAMOND_BOOTS:
                    this.setArmor(player, ArmorType.BOOTS);
                    break;
            }

            if(event.getItem().getType() == Material.WORKBENCH) {
                player.openWorkbench(null, true);
                return;
            }

            if(event.getItem().getType() == Material.ENCHANTMENT_TABLE) {
                final Location enchantingTableLocation = player.getLocation().getBlock().getLocation().clone().add(0.0, -15.0, 0.0); //Temporary enchanting table
                enchantingTableLocation.getBlock().setType(Material.ENCHANTMENT_TABLE);
                player.openEnchanting(enchantingTableLocation, true);
                return;
            }
        }
    }

    private void setArmor(Player player, ArmorType armorType) {
        final ItemStack oldItem = player.getItemInHand();

        switch (armorType) {
            case HELMET:
                player.setItemInHand((player.getInventory().getHelmet() == null) ? null : player.getInventory().getHelmet());
                player.getInventory().setHelmet(oldItem);
                break;
            case CHESTPLATE:
                player.setItemInHand((player.getInventory().getChestplate() == null) ? null : player.getInventory().getChestplate());
                player.getInventory().setChestplate(oldItem);
                break;
            case LEGGINGS:
                player.setItemInHand((player.getInventory().getLeggings() == null) ? null : player.getInventory().getLeggings());
                player.getInventory().setLeggings(oldItem);
                break;
            case BOOTS:
                player.setItemInHand((player.getInventory().getBoots() == null) ? null : player.getInventory().getBoots());
                player.getInventory().setBoots(oldItem);
                break;
        }
    }

    public enum ArmorType {

        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS;

    }

}
