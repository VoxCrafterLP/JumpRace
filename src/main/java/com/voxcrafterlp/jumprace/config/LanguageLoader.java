package com.voxcrafterlp.jumprace.config;

import com.google.common.collect.Lists;
import com.voxcrafterlp.jumprace.JumpRace;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 18.03.2021
 * Time: 19:28
 * Project: JumpRace
 */

@Getter
public class LanguageLoader {

    private File languageFile;
    private final YamlConfiguration configuration;

    /**
     * Load the language from the config file
     * @param path Path and filename
     */
    public LanguageLoader(String path) {
        this.languageFile = new File(path);

        if(!this.languageFile.exists()) {
            Bukkit.getConsoleSender().sendMessage("§4The file " + path + " could not be found! Defaulting to en_US!");
            this.languageFile = new File("plugins/JumpRace/languages/en_US.yml");
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.languageFile);
    }

    /**
     * Get a translation from the language file
     * @param key Key of translation
     * @return Translated string
     */
    public String getTranslationByKey(String key, String... variables) {
        String string = this.configuration.getString(key);
        for(int i = 0; i<variables.length; i++)
            string = string.replace("{" + i + "}", variables[i]);

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get a translation from the language file with the plugin prefix
     * @param key Key of translation
     * @return Translated string with the plugin prefix
     */
    public String getTranslationByKeyWithPrefix(String key, String... variables) {
        String string = this.configuration.getString(key);
        for(int i = 0; i<variables.length; i++)
            string = string.replace("{" + i + "}", variables[i]);

        return JumpRace.getInstance().getPrefix() + ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Build item description
     * @param descriptionKey Key of the translation
     * @return Description of the item
     */
    public String[] buildDescription(String descriptionKey, String... variables) {
        List<String> lore = Lists.newCopyOnWriteArrayList();
        lore.addAll(Arrays.asList("§8§m------------------", " "));
        lore.addAll(Arrays.asList(this.getTranslationByKey(descriptionKey, variables).split("\n")));
        lore.addAll(Arrays.asList(" ", "§8§m------------------"));
        return lore.toArray(new String[0]);
    }
}
