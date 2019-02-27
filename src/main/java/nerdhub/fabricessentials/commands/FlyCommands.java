package nerdhub.fabricessentials.commands;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

public class FlyCommands {

    public static void registerFlyCommands() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager.literal("fly")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                            if (!playerEntity.canFly()) {
                                playerEntity.abilities.allowFlying = true;
                                playerEntity.addChatMessage(new TranslatableTextComponent("fly.enabled").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            } else {
                                playerEntity.abilities.allowFlying = false;
                                playerEntity.abilities.flying = false;
                                playerEntity.addChatMessage(new TranslatableTextComponent("fly.disabled").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            }
                            return 1;
                        })
        ));
    }
}
