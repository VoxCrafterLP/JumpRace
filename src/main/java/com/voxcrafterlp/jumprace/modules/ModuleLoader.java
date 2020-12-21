package com.voxcrafterlp.jumprace.modules;

import java.io.File;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:55
 * Project: JumpRace
 */

public class ModuleLoader {

    private final boolean builderServer;
    private final File modulesFolder;

    public ModuleLoader(boolean builderServer) {
        this.builderServer = builderServer;

        this.modulesFolder = new File("plugins/JumpRace/modules/");
    }

    public void loadModules() {

    }

}

