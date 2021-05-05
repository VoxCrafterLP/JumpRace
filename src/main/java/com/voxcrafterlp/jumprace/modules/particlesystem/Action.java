package com.voxcrafterlp.jumprace.modules.particlesystem;

import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.modules.particlesystem.enums.ActionType;
import com.voxcrafterlp.jumprace.utils.ItemManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.05.2021
 * Time: 11:51
 * Project: JumpRace
 */

@Getter @Setter
public class Action {

    private int value;
    private ActionType actionType;
    private Inventory configurationInventory;

    public Action() {
        this(ActionType.VELOCITY_BOOST, 1);
    }

    public Action(ActionType actionType, int value) {
        this.value = value;
        this.actionType = actionType;

        if(JumpRace.getInstance().getJumpRaceConfig().isBuilderServer())
            this.buildInventory();
    }

    public void execute(Player player) {
        player.playSound(player.getLocation(), Sound.CREEPER_HISS, 1,1);

        switch (this.actionType) {
            case VELOCITY_BOOST:
                player.setVelocity(new Vector(0, this.value, 0));
                break;
            case JUMP_BOOST:
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,this.value * 20 ,0, false));
                break;
            case SPEED_BOOST:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,this.value * 20 ,0, false));
                break;
        }
    }

    /**
     * Builds the configuration inventory
     */
    public void buildInventory() {
        this.configurationInventory = Bukkit.createInventory(null, 27, JumpRace.getInstance().getLanguageLoader().getTranslationByKey("action-inventory-name"));
        final ItemStack background = new ItemManager(Material.STAINED_GLASS_PANE, 15).setNoName().build();

        for(int i = 0; i<this.configurationInventory.getSize(); i++)
            this.configurationInventory.setItem(i, background);

        this.configurationInventory.setItem(5, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§a+1").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0="));
        this.configurationInventory.setItem(23, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName("§c-1").setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQzNzM0NmQ4YmRhNzhkNTI1ZDE5ZjU0MGE5NWU0ZTc5ZGFlZGE3OTVjYmM1YTEzMjU2MjM2MzEyY2YifX19"));

        this.configurationInventory.setItem(18, new ItemManager(Material.SKULL_ITEM, 3).setDisplayName(JumpRace.getInstance().getLanguageLoader().getTranslationByKey("particles-addeffect-back-name")).addLore(JumpRace.getInstance().getLanguageLoader().buildDescription("particles-addeffect-back-description")).setHeadValueAndBuild("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="));

        this.updateInventory();
    }

    /**
     * Updates the configuration inventory
     */
    public void updateInventory() {
        this.configurationInventory.setItem(14, this.getBookDescription());
        this.configurationInventory.setItem(12, this.actionType.getDisplay());
    }

    /**
     * Builds the info book for the configuration inventory
     * @return Built info book
     */
    private ItemStack getBookDescription() {
        final StringBuilder stringBuilder = new StringBuilder("action-info-description-");

        switch (this.actionType) {
            case VELOCITY_BOOST:
                stringBuilder.append("velocity");
                break;
            case JUMP_BOOST:
                stringBuilder.append("jumpboost");
                break;
            case SPEED_BOOST:
                stringBuilder.append("speed");
                break;
        }

        return new ItemManager(Material.BOOK).setDisplayName(JumpRace.getInstance().getLanguageLoader()
                .getTranslationByKey("action-info-name"))
                .addLore(JumpRace.getInstance().getLanguageLoader().buildDescription(stringBuilder.toString(), String.valueOf(this.value)))
                .build();
    }

}
