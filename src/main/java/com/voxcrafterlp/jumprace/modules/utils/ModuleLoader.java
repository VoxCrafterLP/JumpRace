package com.voxcrafterlp.jumprace.modules.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.ModuleData;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 20.12.2020
 * Time: 23:55
 * Project: JumpRace
 */

@Getter
public class ModuleLoader {

    private final boolean builderServer;
    private final File modulesFolder;
    private final List<Module> moduleList;
    private Module defaultModule;

    public ModuleLoader(boolean builderServer) {
        this.builderServer = builderServer;
        this.moduleList = Lists.newCopyOnWriteArrayList();
        this.modulesFolder = new File("plugins/JumpRace/modules/");
    }

    public void loadModules() throws IOException {
        for(File file : Objects.requireNonNull(this.modulesFolder.listFiles())) {
            if(file.isDirectory()) {
                File moduleProperties = new File(file.getAbsolutePath() + File.separator + "module.json");
                File moduleSchematic = new File(file.getAbsolutePath() + File.separator + "module.schematic");

                if(!(moduleProperties.exists() && moduleSchematic.exists())) {
                    Bukkit.getConsoleSender().sendMessage("§cInvalid module found in directory " + file.getName() + ".");
                    return;
                }

                String propertiesString = readModuleProperties(moduleProperties);

                if(!isValidJson(propertiesString)) {
                    Bukkit.getConsoleSender().sendMessage("§cInvalid module found in directory " + file.getName() + ".");
                    return;
                }

                JSONObject properties = new JSONObject(propertiesString);

                Module module = new Module(properties.getString("name"),
                        properties.getString("builder"),
                        ModuleDifficulty.getModuleDifficultyByConfigName(properties.getString("difficulty")),
                        this.getModuleDataFromFile(moduleSchematic),
                        new RelativePosition(properties.getJSONObject("startpoint")),
                        new RelativePosition(properties.getJSONObject("endpoint")), false);


                if(!module.getName().equals("default")) {
                    Bukkit.getConsoleSender().sendMessage("§aSuccessfully loaded module " + module.getName() + ".");
                    this.moduleList.add(module);
                } else
                    this.defaultModule = module;
            }
        }
        if(this.moduleList.isEmpty())
            Bukkit.getConsoleSender().sendMessage("§cNo modules found");
    }

    private String readModuleProperties(File moduleProperties) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(moduleProperties.getPath()));
        List<String> lines = Files.readAllLines(moduleProperties.toPath());

        StringBuilder stringBuilder = new StringBuilder();
        lines.forEach(stringBuilder::append);

        bufferedReader.close();
        return stringBuilder.toString();
    }

    private ModuleData getModuleDataFromFile(File file) {
        try {
            ModuleData moduleData = new ModuleData();
            FileInputStream fileInputStream = new FileInputStream(file);

            Object nbtData = NBTCompressedStreamTools.class.getMethod("a", InputStream.class).invoke(null, fileInputStream);
            Method getShort  = nbtData.getClass().getMethod("getShort", String.class);
            Method getByteArray = nbtData.getClass().getMethod("getByteArray", String.class);

            moduleData.setWidth((short) getShort.invoke(nbtData, "Width"));
            moduleData.setHeight((short) getShort.invoke(nbtData, "Height"));
            moduleData.setLength((short) getShort.invoke(nbtData, "Length"));

            moduleData.setBlocks((byte[]) getByteArray.invoke(nbtData, "Blocks"));
            moduleData.setData((byte[]) getByteArray.invoke(nbtData, "Data"));

            fileInputStream.close();
            return moduleData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean isValidJson(String string) {
        try {
            new JSONObject(string);
            return true;
        } catch (JSONException exception) {
            return false;
        }
    }

}

