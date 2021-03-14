package com.voxcrafterlp.jumprace.objects;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.utils.HologramUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 14.03.2021
 * Time: 13:07
 * Project: JumpRace
 */

@Getter
public class DeathChest {

    private final Location location;
    private final Inventory inventory;
    private int itemsTaken, hologramID;
    private final List<ItemStack> drops;

    private static final int MAX_ITEMS = 3;

    /**
     * Spawns an EnderChest at the player's death location as well as a hologram
     * @param player Player from whom the chest should be created
     * @param drops Inventory content of the player
     */
    public DeathChest(Player player, List<ItemStack> drops) {
        final Location chestLocation = player.getLocation().getBlock().getLocation(); //Gets the block location
        if(chestLocation.getBlock().getType() != Material.AIR)
            chestLocation.add(0.0, 1.0, 0.0); //Prevents the ender chest from breaking the map

        this.itemsTaken = 0;
        this.drops = drops;
        chestLocation.getBlock().setType(Material.ENDER_CHEST);
        this.location = chestLocation;
        this.inventory = this.buildLootChestInventory(player);

        final Location hologramLocation = this.location.clone().add(0.5, 0.1, 0.5);

        final EntityArmorStand hologram = new EntityArmorStand(((CraftWorld) hologramLocation.getWorld()).getHandle());
        hologram.setLocation(hologramLocation.getX(), hologramLocation.getY(), hologramLocation.getZ(), 0, 0);
        hologram.setCustomName("§c✝ §7" + player.getName() + " §c✝");
        hologram.setCustomNameVisible(true);
        hologram.setSmall(true);
        hologram.setInvisible(true);
        this.hologramID = hologram.getId();

        Bukkit.getOnlinePlayers().forEach(players -> new HologramUtil().summonArmorStand(players, hologram));
    }

    /**
     * Extracts the armor & inventory contents of the player's inventory and puts them
     * into the DeathChest inventory
     *
     * @param player The player whose items should be extracted
     * @return Inventory containing the extracted items
     */
    private Inventory buildLootChestInventory(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 45, "§c✝ §7" + player.getName());
        inventory.addItem(this.drops.toArray(new ItemStack[]{}));

        return inventory;
    }

    public void openInventory(Player player) {
        player.openInventory(this.inventory);
        player.playSound(player.getLocation(), Sound.CHEST_OPEN,5,5);
    }

    /**
     * Puts the clicked item in the player's inventory
     * When the maximum number of items that can be taken is reached, the {@link #destroyChest()} method
     *
     * @param player Player who wants to take the item
     * @param itemStack Item which should be be should be taken
     */
    public void takeItem(Player player, ItemStack itemStack) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,3);
        player.getInventory().addItem(itemStack);
        this.inventory.remove(itemStack);

        this.itemsTaken++;

        if(this.itemsTaken == MAX_ITEMS)
            this.destroyChest();
    }

    /**
     * Destroys the chest and closes the inventory of every player who has the chest
     * inventory opened.
     */
    private void destroyChest() {
        this.location.getBlock().setType(Material.AIR);
        Bukkit.getOnlinePlayers().forEach(players -> {
            this.location.getWorld().playEffect(this.location, Effect.SMOKE, 10);
            if(this.inventory.getViewers().contains(players)) {
                players.closeInventory();
                players.playSound(players.getLocation(), Sound.ITEM_BREAK,1,1);
            }

            new HologramUtil().removeArmorStand(players, this.hologramID);
        });

        JumpRace.getInstance().getGameManager().getDeathChests().remove(this);
    }

}
