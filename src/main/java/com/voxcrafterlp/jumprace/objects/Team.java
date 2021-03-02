package com.voxcrafterlp.jumprace.objects;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import com.voxcrafterlp.jumprace.enums.TeamColor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 26.02.2021
 * Time: 16:58
 * Project: JumpRace
 */

@Getter
public class Team {

    private final TeamColor teamColor;
    private final List<Player> members;

    public Team(TeamColor teamColor) {
        this.teamColor = teamColor;
        this.members = Lists.newCopyOnWriteArrayList();
    }

    public ItemStack getTeamSelectorItem() {
        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        bootsMeta.setDisplayName(this.teamColor.getTeamNameWithColorCode());
        bootsMeta.setColor(this.teamColor.getColor());

        List<String> lore = Lists.newCopyOnWriteArrayList();
        lore.add("§8§m-------------------");

        for(int i = 0; i< JumpRace.getInstance().getJumpRaceConfig().getTeamSize(); i++)
            lore.add(this.members.size() >= (i+1) ? " §8➜" + this.teamColor.getColorCode() + " " + this.members.get(i).getName() : " §8➜");

        lore.add("§8§m-------------------");

        bootsMeta.setLore(lore);

        if(this.members.size() == JumpRace.getInstance().getJumpRaceConfig().getTeamSize()) {
            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            bootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        boots.setItemMeta(bootsMeta);

        return boots;
    }

    public boolean isFull() {
        return this.members.size() == JumpRace.getInstance().getJumpRaceConfig().getTeamSize();
    }

}
