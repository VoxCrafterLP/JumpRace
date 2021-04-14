package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagList;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 21.12.2020
 * Time: 00:26
 * Project: JumpRace
 */

@Getter @Setter
public class ModuleData {

    private short width;
    private short height;
    private short length;

    private byte[] blocks;
    private byte[] data;

    private NBTTagList tileEntities;

}
