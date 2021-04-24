package com.voxcrafterlp.jumprace.modules.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.modules.enums.ModuleDifficulty;
import com.voxcrafterlp.jumprace.modules.objects.Module;
import com.voxcrafterlp.jumprace.modules.objects.ModuleData;
import com.voxcrafterlp.jumprace.modules.objects.RelativePosition;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Arrays;
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

    private final File modulesFolder;
    private final List<Module> moduleList;
    private Module defaultModule;

    public ModuleLoader() {
        this.moduleList = Lists.newCopyOnWriteArrayList();
        this.modulesFolder = new File("plugins/JumpRace/modules/");
    }

    /**
     * Load modules from the modules folder
     * @throws IOException If an I/O error occurred
     */
    public void loadModules() throws IOException {
        Arrays.stream(Objects.requireNonNull(this.modulesFolder.listFiles())).forEach(file -> {
            if(file.isDirectory()) {
                final File moduleProperties = new File(file.getAbsolutePath() + File.separator + "module.json");
                final File moduleSchematic = new File(file.getAbsolutePath() + File.separator + "module.schematic");

                if(!(moduleProperties.exists() && moduleSchematic.exists())) {
                    Bukkit.getConsoleSender().sendMessage("§cInvalid module found in directory " + file.getName() + ".");
                    return;
                }

                final String propertiesString = this.readModuleProperties(moduleProperties);

                if(!this.isValidJson(propertiesString)) {
                    Bukkit.getConsoleSender().sendMessage("§cInvalid module found in directory " + file.getName() + ".");
                    return;
                }

                final JSONObject properties = new JSONObject(propertiesString);

                if(this.moduleList.stream().filter(module -> module.getName().equalsIgnoreCase(properties.getString("name"))).count() != 0) {
                    Bukkit.getConsoleSender().sendMessage("§cModule " + properties.getString("name") + " already exists!");
                    return;
                }

                final Module module = new Module(properties.getString("name"),
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
        });
        if(this.moduleList.isEmpty())
            Bukkit.getConsoleSender().sendMessage("§cNo modules found");
    }

    public void reloadModules() {
        this.moduleList.clear();
        try {
            this.loadModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the content from the properties file
     * @param moduleProperties Properties file
     * @return Content of the the file
     */
    private String readModuleProperties(File moduleProperties) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(moduleProperties.getPath()));
            final List<String> lines = Files.readAllLines(moduleProperties.toPath());

            StringBuilder stringBuilder = new StringBuilder();
            lines.forEach(stringBuilder::append);

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Read module data from the schematic file
     * @param file Schematic file
     * @return Parsed ModuleData
     */
    private ModuleData getModuleDataFromFile(File file) {
        try {
            final ModuleData moduleData = new ModuleData();
            final FileInputStream fileInputStream = new FileInputStream(file);
            final NBTTagCompound nbtData = NBTCompressedStreamTools.a(fileInputStream);;

            moduleData.setWidth( nbtData.getShort("Width"));
            moduleData.setHeight( nbtData.getShort("Height"));
            moduleData.setLength( nbtData.getShort("Length"));

            moduleData.setBlocks(nbtData.getByteArray("Blocks"));
            moduleData.setData(nbtData.getByteArray("Data"));

            moduleData.setTileEntities(nbtData.getList("TileEntities", 10));

            fileInputStream.close();
            return moduleData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check of a string has a valid json format
     * @param string String which should be checked
     * @return If the string is a valid json
     */
    private boolean isValidJson(String string) {
        try {
            new JSONObject(string);
            return true;
        } catch (JSONException exception) {
            return false;
        }
    }

}

