package nerdhub.fabricessentials.commands;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlyCommands {

    public static void registerFlyCommands() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("fly")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                    if(!playerEntity.canFly()) {
                        playerEntity.abilities.allowFlying = true;
                    }else {
                        playerEntity.abilities.allowFlying = false;
                        playerEntity.abilities.flying = false;
                    }
                    return 1;
                }));
    }
}
