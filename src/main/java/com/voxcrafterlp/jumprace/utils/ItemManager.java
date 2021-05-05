package com.voxcrafterlp.jumprace.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 29.12.2020
 * Time: 03:18
 * Project: JumpRace
 */

public class ItemManager {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemManager(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemManager(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemManager(final Material material, int id) {
        this.itemStack = new ItemStack(material, 1, (byte) id);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemManager setAmount(final int value) {
        this.itemStack.setAmount(value);
        return this;
    }

    public ItemManager setDisplayName(final String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemManager addDisplayName(final String displayName) {
        this.itemMeta.setDisplayName(this.itemMeta.getDisplayName() + displayName);
        return this;
    }

    public ItemManager setNoName() {
        this.itemMeta.setDisplayName(" ");
        return this;
    }

    public ItemManager addLore(final String... strings) {
        this.itemMeta.setLore(Arrays.asList(strings));
        return this;
    }

    public ItemManager addLore(final List<String> stringList) {
        this.itemMeta.setLore(stringList);
        return this;
    }

    public ItemManager setUnbreakable(final boolean unbreakable) {
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemManager addItemFlag(final ItemFlag itemFlag) {
        this.itemMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemManager addEnchantment(final Enchantment enchantment, final int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemManager setSubID(int id) {
        this.itemStack.setTypeId(id);
        return this;
    }

    public ItemManager setType(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemStack setHeadOwnerAndBuild(String owner) {
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        skullMeta.setOwner(owner);
        this.itemStack.setItemMeta(skullMeta);
        return this.itemStack;
    }

    public ItemStack setHeadValueAndBuild(String value) {
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);

        gameProfile.getProperties().put("textures", new Property("textures", value));

        try {
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.itemStack.setItemMeta(skullMeta);
        return this.itemStack;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }

}

