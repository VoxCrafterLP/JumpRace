package com.voxcrafterlp.jumprace.modules.objects;

import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:15
 * Project: JumpRace
 */

@Getter @Setter
public class Module {

    private String name;
    private ModuleDifficulty moduleDifficulty;
    private ModuleData moduleData;

    private RelativePosition startPoint;
    private RelativePosition endPoint;

    public void build(Location location, boolean isLastModule) {
        for(int x = 0; x < moduleData.getWidth(); x++) {
            for(int y = 0; y < moduleData.getHeight(); y++) {
                for(int z = 0; z < moduleData.getLength(); z++) {
                    int index = y * moduleData.getWidth() * moduleData.getLength() + z * moduleData.getWidth() + x;
                    int b = moduleData.getBlocks()[index] & 0xFF; //make the block unsigned,
                    Material material = Material.getMaterial(b);
                    if(material != Material.AIR) {
                        Block block = new Location(location.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z).getBlock();
                        block.setType(material, true);
                        block.setData(moduleData.getData()[index]);
                    }
                }
            }
        }

        location.getWorld().getBlockAt(calculateLocation(location, startPoint)).setType(Material.GOLD_BLOCK);
        location.getWorld().getBlockAt(calculateLocation(location, endPoint)).setType(isLastModule ? Material.EMERALD_BLOCK : Material.GOLD_BLOCK);
    }
    
    private Location calculateLocation(Location location, RelativePosition relativePosition) {
        return new Location(location.getWorld(), (location.getX() + relativePosition.getRelativeX()), (location.getY() + relativePosition.getRelativeY()), (location.getZ()) + relativePosition.getRelativeZ());
    }

}
