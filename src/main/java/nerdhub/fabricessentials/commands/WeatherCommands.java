package nerdhub.fabricessentials.commands;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

public class WeatherCommands {

    public static void registerWeatherCommands() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager.literal("toggledownfall")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            if (playerEntity.world.hasRain(playerEntity.getPos())) {
                                context.getSource().getWorld().getLevelProperties().setClearWeatherTime(6000);
                                context.getSource().getWorld().getLevelProperties().setRainTime(0);
                                context.getSource().getWorld().getLevelProperties().setThunderTime(0);
                                context.getSource().getWorld().getLevelProperties().setRaining(false);
                                context.getSource().getWorld().getLevelProperties().setThundering(false);
                                playerEntity.addChatMessage(new TranslatableTextComponent("weather.clearweather").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            } else {
                                context.getSource().getWorld().getLevelProperties().setClearWeatherTime(0);
                                context.getSource().getWorld().getLevelProperties().setRainTime(6000);
                                context.getSource().getWorld().getLevelProperties().setThunderTime(6000);
                                context.getSource().getWorld().getLevelProperties().setRaining(true);
                                context.getSource().getWorld().getLevelProperties().setThundering(false);
                                playerEntity.addChatMessage(new TranslatableTextComponent("weather.rainweather").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            }

                            return 1;
                        })
        ));
    }
}
