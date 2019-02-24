package nerdhub.fabricessentials.registry;

import nerdhub.fabricessentials.FabricEssentials;
import nerdhub.fabricessentials.commands.*;

public class ModCommands {

    public static void registerCommands() {
        if(FabricEssentials.config.getBoolean("feature-homes")) {
            HomeCommands.registerHomeCommands();
        }

        if(FabricEssentials.config.getBoolean("feature-gamemode")) {
            GamemodeCommands.registerGamemodeCommands();
        }

        if(FabricEssentials.config.getBoolean("feature-time")) {
            TimeCommands.registerTimeCommands();
        }

        if(FabricEssentials.config.getBoolean("feature-weather")) {
            WeatherCommands.registerWeatherCommands();
        }

        if(FabricEssentials.config.getBoolean("feature-warps")) {
            WarpsCommands.registerWarpsCommands();
        }

        if(FabricEssentials.config.getBoolean("feature-fly")) {
            FlyCommands.registerFlyCommands();
        }

        if (FabricEssentials.config.getBoolean("feature-spawn")) {
            SpawnCommands.registerSpawnCommands();
        }
    }
}
