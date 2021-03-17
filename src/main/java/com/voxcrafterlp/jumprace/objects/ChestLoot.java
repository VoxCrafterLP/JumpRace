package com.voxcrafterlp.jumprace.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.*;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 04.03.2021
 * Time: 09:13
 * Project: JumpRace
 */

@Getter
public class ChestLoot {

    private static final int MAX_ITEMS = 5;
    private static final int MIN_ITEMS = 3;
    private final Map<ModuleDifficulty, List<ItemStack>> registeredLoot;

    public ChestLoot() {
        this.registeredLoot = new HashMap<>();
        this.loadLoot();
    }

    private void loadLoot() {
        Arrays.stream(ModuleDifficulty.values()).forEach(moduleDifficulty -> {
            this.registeredLoot.put(moduleDifficulty, Lists.newCopyOnWriteArrayList());
        });

        //=================================================//

        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_HELMET), 25);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_CHESTPLATE), 25);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_LEGGINGS), 25);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_BOOTS), 25);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.WOOD_SWORD), 15);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_INGOT, 2), 10);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.GOLD_INGOT, 3), 10);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.COOKED_BEEF, 3), 50);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.FLINT), 20);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.FEATHER), 20);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.WOOD_SWORD), 20);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.TNT), 15);
        this.addLoot(ModuleDifficulty.EASY, new ItemStack(Material.WEB), 15);

        //=================================================//

        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.CHAINMAIL_HELMET), 30);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.CHAINMAIL_CHESTPLATE), 30);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.CHAINMAIL_LEGGINGS), 30);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.CHAINMAIL_BOOTS), 30);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.STONE_SWORD), 25);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.IRON_INGOT), 10);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.APPLE), 30);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.STICK), 20);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.FEATHER), 15);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.FLINT), 15);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.EXP_BOTTLE, 2), 25);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.TNT), 20);
        this.addLoot(ModuleDifficulty.NORMAL, new ItemStack(Material.WEB), 20);

        //=================================================//

        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.IRON_HELMET), 30);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.IRON_CHESTPLATE), 30);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.IRON_LEGGINGS), 30);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.IRON_BOOTS), 30);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.STONE_SWORD), 10);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.DIAMOND), 15);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.ENCHANTMENT_TABLE), 20);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.WORKBENCH), 30);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.EXP_BOTTLE, 3), 25);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.TNT), 15);
        this.addLoot(ModuleDifficulty.HARD, new ItemStack(Material.WEB), 15);

        //=================================================//

        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.IRON_HELMET), 30);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.IRON_CHESTPLATE), 30);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.IRON_LEGGINGS), 30);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.IRON_BOOTS), 30);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.IRON_SWORD), 25);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.DIAMOND, 2), 25);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.ENCHANTMENT_TABLE), 20);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.WORKBENCH), 30);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.EXP_BOTTLE, 4), 25);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.TNT), 10);
        this.addLoot(ModuleDifficulty.VERY_HARD, new ItemStack(Material.WEB), 10);

        //=================================================//
    }

    private void addLoot(ModuleDifficulty moduleDifficulty, ItemStack itemStack, int percentage) {
        for(int i = 1 ; i < percentage+1 ; i++)
            this.registeredLoot.get(moduleDifficulty).add(itemStack);
    }

    public void openChest(Player player, Location chestLocation, ModuleDifficulty moduleDifficulty) {
        player.playSound(player.getLocation(), Sound.CLICK,3, 2);
        player.playEffect(chestLocation, Effect.EXPLOSION_LARGE, 1);
        chestLocation.getBlock().setType(Material.AIR);

        player.getInventory().addItem(this.getRandomLoot(moduleDifficulty).toArray(new ItemStack[]{}));
    }

    private List<ItemStack> getRandomLoot(ModuleDifficulty moduleDifficulty) {
        List<ItemStack> lootFromDifficulty = Lists.newCopyOnWriteArrayList();
        List<ItemStack> loot = Lists.newCopyOnWriteArrayList();

        this.registeredLoot.forEach((difficulty, list) -> {
            if(difficulty == moduleDifficulty)
                lootFromDifficulty.addAll(list);
        });

        int amount = new Random().nextInt(MAX_ITEMS - MIN_ITEMS + 1) + MIN_ITEMS;

        for(int i = 0; i<amount; i++)
            loot.add(lootFromDifficulty.get(new Random().nextInt(lootFromDifficulty.size())));

        return loot;
    }

}
