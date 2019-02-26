package nerdhub.fabricessentials.commands;

import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.text.StringTextComponent;

public class ClearChatCommands {

    public static void registerClearChatCommands() {
        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> {
            serverCommandSourceCommandDispatcher.register(
                    ServerCommandManager.literal("cc")
                    .executes(context -> {
                        for (int i = 0; i < 100; i++) {
                            context.getSource().getPlayer().addChatMessage(new StringTextComponent(" "), false);
                        }
                       return 0;
                    })
            );

            serverCommandSourceCommandDispatcher.register(
                    ServerCommandManager.literal("cca")
                            .requires(source -> source.hasPermissionLevel(4))
                            .executes(context -> {
                                for (int i = 0; i < 100; i++) {
                                    context.getSource().getPlayer().addChatMessage(new StringTextComponent(" "), false);
                                }
                                return 0;
                            })
            );
        });
    }
}
