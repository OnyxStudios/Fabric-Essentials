package nerdhub.fabricessentials.commands;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.BlockPos;

public class SpawnCommands {

    public static void registerSpawnCommands() {
        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> {
            serverCommandSourceCommandDispatcher.register(
                    ServerCommandManager.literal("spawn").executes(context -> {
                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                        BlockPos spawnPos = context.getSource().getWorld().getSpawnPos();

                        playerEntity.networkHandler.teleportRequest(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), playerEntity.pitch, playerEntity.yaw);
                        playerEntity.addChatMessage(new TranslatableTextComponent("spawn.teleported").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                        return 1;
                    }));

            serverCommandSourceCommandDispatcher.register(
                    ServerCommandManager.literal("setspawn")
                            .requires(source -> source.hasPermissionLevel(4))
                            .executes(context -> {
                                ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                                context.getSource().getWorld().setSpawnPos(playerEntity.getPos());
                                playerEntity.addChatMessage(new TranslatableTextComponent("spawn.set").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                                return 1;
                            })
            );
        });
    }
}
