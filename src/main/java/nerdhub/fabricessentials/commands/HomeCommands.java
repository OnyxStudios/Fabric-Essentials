package nerdhub.fabricessentials.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import nerdhub.fabricessentials.FabricEssentials;
import nerdhub.fabricessentials.data.PlayerHomePersistentState;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.BlockPos;

public class HomeCommands {

    public static int maxPlayerHomes = FabricEssentials.config.getInt("player-homes-count");

    public static void registerHomeCommands() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager
                        .literal("sethome")
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());
                            state.addPlayerHome(playerEntity.getUuid(), "home", playerEntity.getPos());
                            return 1;
                        }).then(ServerCommandManager.argument("homename", StringArgumentType.string()).executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());

                    if (context.getSource().hasPermissionLevel(4) || state.getHomes(playerEntity.getUuid()).size() < maxPlayerHomes) {
                        state.addPlayerHome(playerEntity.getUuid(), StringArgumentType.getString(context, "homename"), playerEntity.getPos());
                        playerEntity.addChatMessage(new TranslatableTextComponent("homes.sethome", StringArgumentType.getString(context, "homename")).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    } else {
                        if (maxPlayerHomes == 1) {
                            state.clearPlayerHomes(playerEntity.getUuid());
                            state.addPlayerHome(playerEntity.getUuid(), "home", playerEntity.getPos());
                            playerEntity.addChatMessage(new TranslatableTextComponent("homes.sethome, home").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                        } else {
                            playerEntity.addChatMessage(new TranslatableTextComponent("homes.maximumlimit").setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);
                        }
                    }
                    return 1;
                }))));

        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager
                        .literal("homes")
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());

                            StringTextComponent homesTextComponent = new StringTextComponent(String.join(", ", state.getHomes(playerEntity.getUuid())));
                            homesTextComponent.setStyle(new Style().setColor(TextFormat.GOLD));

                            playerEntity.addChatMessage(homesTextComponent, false);
                            return state.getHomes(playerEntity.getUuid()).size();
                        })));

        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager.literal("home")
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());

                            BlockPos pos = state.getPlayerHome(playerEntity.getUuid(), "home");
                            if (pos != null) {
                                playerEntity.networkHandler.teleportRequest(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, playerEntity.yaw, playerEntity.pitch);
                                playerEntity.addChatMessage(new TranslatableTextComponent("homes.teleporthome", "home").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            } else {
                                playerEntity.addChatMessage(new TranslatableTextComponent("homes.nonexist", "home").setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);
                                return 0;
                            }

                            return 1;
                        }).then(ServerCommandManager.argument("homename", StringArgumentType.string()).executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());

                    BlockPos pos = state.getPlayerHome(playerEntity.getUuid(), StringArgumentType.getString(context, "homename"));

                    if (pos != null) {
                        playerEntity.networkHandler.teleportRequest(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, playerEntity.yaw, playerEntity.pitch);
                        playerEntity.addChatMessage(new TranslatableTextComponent("homes.teleporthome", StringArgumentType.getString(context, "homename")).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    } else {
                        playerEntity.addChatMessage(new TranslatableTextComponent("homes.nonexist", StringArgumentType.getString(context, "homename")).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);
                        return 0;
                    }

                    return 1;
                }))
        ));

        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(
                ServerCommandManager
                        .literal("delhome")
                        .then(ServerCommandManager.argument("homename", StringArgumentType.string()).executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            PlayerHomePersistentState state = PlayerHomePersistentState.get(context.getSource().getWorld());
                            String homeName = StringArgumentType.getString(context, "homename");

                            if (state.doesPlayerHaveHome(playerEntity.getUuid(), homeName)) {
                                state.deletePlayerHome(playerEntity.getUuid(), homeName);
                                playerEntity.addChatMessage(new TranslatableTextComponent("homes.removehome", homeName).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                            }

                            return 1;
                        }))
        ));
    }
}
