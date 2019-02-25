package nerdhub.fabricessentials.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import nerdhub.fabricessentials.data.WarpsPersistentState;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.BlockPos;

public class WarpsCommands {

    public static void registerWarpsCommands() {
        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("warp")
                .then(ServerCommandManager.argument("name", StringArgumentType.string()).executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    WarpsPersistentState state = WarpsPersistentState.get(context.getSource().getWorld());
                    BlockPos warpPos = state.getWarp(StringArgumentType.getString(context, "name"));
                    playerEntity.networkHandler.teleportRequest(warpPos.getX(), warpPos.getY(), warpPos.getZ(), playerEntity.pitch, playerEntity.yaw);

                    playerEntity.addChatMessage(new TranslatableTextComponent("warps.warped", StringArgumentType.getString(context, "name")).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    return 1;
                })));

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("setwarp")
                .requires(source -> source.hasPermissionLevel(4))
                .then(ServerCommandManager.argument("name", StringArgumentType.string()).executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    WarpsPersistentState state = WarpsPersistentState.get(context.getSource().getWorld());
                    state.addWarp(StringArgumentType.getString(context, "name"), playerEntity.getPos());

                    playerEntity.addChatMessage(new TranslatableTextComponent("warps.setwarp", StringArgumentType.getString(context, "name")).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    return 1;
                })));

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("delwarp")
                .then(ServerCommandManager.argument("name", StringArgumentType.string()).executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    WarpsPersistentState state = WarpsPersistentState.get(context.getSource().getWorld());
                    String name = StringArgumentType.getString(context, "name");

                    if(state.hasWarp(name)) {
                        state.removeWarp(name);
                        playerEntity.addChatMessage(new TranslatableTextComponent("warps.delwarp", name).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    }else {
                        playerEntity.addChatMessage(new TranslatableTextComponent("warps.nonexistent", name).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), true);
                    }
                    return 1;
                })));

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("warps").executes(context -> {
            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
            WarpsPersistentState state = WarpsPersistentState.get(context.getSource().getWorld());

            StringTextComponent homesTextComponent = new StringTextComponent(String.join(", ", state.warps.keySet()));
            homesTextComponent.setStyle(new Style().setColor(TextFormat.GOLD));

            playerEntity.addChatMessage(homesTextComponent, false);
            return 1;
        }));
    }
}
