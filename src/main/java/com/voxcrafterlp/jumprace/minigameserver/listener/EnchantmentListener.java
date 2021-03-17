package com.voxcrafterlp.jumprace.minigameserver.listener;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 17.03.2021
 * Time: 10:05
 * Project: JumpRace
 */

public class EnchantmentListener implements Listener {

    private final ItemStack lapis;

    public EnchantmentListener() {
        final Dye dye = new Dye();
        dye.setColor(DyeColor.BLUE);
        this.lapis = new ItemManager(dye.toItemStack()).setAmount(3).build();
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if(event.getInventory() instanceof EnchantingInventory)
            event.getInventory().setItem(1, this.lapis);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getInventory() instanceof EnchantingInventory) {
            event.getInventory().setItem(1, null);
            final Location enchantingTableLocation = event.getPlayer().getLocation().getBlock().getLocation().clone().add(0.0, -15.0, 0.0); //Remove temporary enchanting table
            enchantingTableLocation.getBlock().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getInventory() instanceof EnchantingInventory) {
            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getType() == Material.INK_SACK) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(JumpRace.getInstance(), () -> {
            final Player player = event.getEnchanter();
            event.getInventory().setItem(1, this.lapis);
            player.setLevel(player.getLevel() + event.whichButton() + 1);
        }, 2);
    }

    @EventHandler
    public void onPrepare(PrepareItemEnchantEvent event){
        event.getExpLevelCostsOffered()[0] = 1;
        event.getExpLevelCostsOffered()[1] = 2;
        event.getExpLevelCostsOffered()[2] = 3;
    }

}
