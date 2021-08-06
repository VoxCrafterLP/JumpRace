# JumpRace

[![build](https://github.com/VoxCrafterLP/JumpRace/actions/workflows/maven.yml/badge.svg)](https://github.com/VoxCrafterLP/JumpRace/actions/workflows/maven.yml)
![GitHub release (latest by date)](https://img.shields.io/github/downloads/VoxCrafterLP/JumpRace/total?label=Downloads)
![GitHub](https://img.shields.io/github/license/VoxCrafterLP/JumpRace)
![GitHub milestone](https://img.shields.io/github/milestones/progress/VoxCrafterLP/JumpRace/3)

This is a spigot based jumpandrun minigame. It's almost like the gamemode 'JumpLeague' on the gommehd.net server but with a few changes. The plugin contains its own building system, which can be used to build your own modules.

**Important**! It is highly recommended to use this plugin in a cloud-system based environment like [CloudNET](https://cloudnetservice.eu/). 

## Features

- Module randomizer
- Module building system
- Quick editor
- API
- Holograms
- Multiple maps
- Fully customizable
- Multiple languages

## Installation

This plugin only requires a properly set up spigot server, but it's recommended to use the latest build of the 1.8.8.
You can learn here how to use the [latest build](https://www.spigotmc.org/wiki/buildtools/#1-8-8).

1. Download the latest JAR file from the [releases tab](https://github.com/VoxCrafterLP/JumpRace/releases).
2. Move the JAR file into your plugins folder.
3. Restart your server.
4. Check the console to ensure that the plugin is working properly.
5. Locate and open the config file in `/plugins/JumpRace/config.yml`.
6. Configure the plugin to your needs.
7. Have fun!

## API

### Maven dependency

```xml

<dependency>
  <groupId>com.voxcrafterlp</groupId>
  <artifactId>JumpRace</artifactId>
  <version>1.1.0-RELEASE</version>
  <scope>provided</scope>
</dependency>

<repository>
  <id>voxcrafter-repo</id>
  <url>https://repo.voxcrafter.dev/repository/maven-releases/</url>
</repository>
```

### Events

**Available events**:
- ModuleFailEvent
- PlayerCompleteModuleEvent
- PlayerReachGoalEvent
- TeamWinEvent
  
**Example**
```java
public class Example implements Listener {

    private static final HashMap<Player, Integer> fails = new HashMap<>();

    @EventHandler
    public void onFail(ModuleFailEvent event) {
        final Player player = event.getPlayer();

        if(fails.containsKey(player))
            fails.replace(player, fails.get(player) + 1);
        else
            fails.put(player, 1);
    }

    @EventHandler
    public void onReach(PlayerReachGoalEvent event) {
        final Player player = event.getPlayer();

        if(fails.containsKey(player)) {
            player.sendMessage("§fYou failed §c" + fails.get(player) + " times.");
        } else
            player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    }

}
```

## Languages

At the moment these languages are available:
```
- en_US (American English) (default)
- de_DE (German)
- de_CH (Swiss German)
```

If you want to add your own language file, you can copy an existing translation file from the `/plugins/JumpRace/languages` folder and translate it. 
**Important! You have to encode the translation file in ISO-8859-1.**
To enable the translation, you have to switch the language file in the config.


## Contributers

- [MakeItGame](https://www.youtube.com/channel/UCk8ROONMzJ3wlZ66vyyffJg) (German translation)
- [Lezurex](https://github.com/Lezurex) (Swiss German translation)

## Bugreports and features

If you found a bug or you want to request a feature, feel free to [create an issue](https://github.com/VoxCrafterLP/JumpRace/issues/new).

## License
This project is licensed under the GNU GPL v3 and may be used accordingly. Further information can be found [here](https://github.com/VoxCrafterLP/JumpRace/blob/main/LICENSE).
