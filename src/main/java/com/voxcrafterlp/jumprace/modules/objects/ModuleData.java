package com.voxcrafterlp.jumprace.modules.objects;

import lombok.Getter;
import lombok.Setter;

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

    public ModuleData(short width, short height, short length, byte[] blocks, byte[] data) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.blocks = blocks;
        this.data = data;
    }

    public ModuleData() {}

}
