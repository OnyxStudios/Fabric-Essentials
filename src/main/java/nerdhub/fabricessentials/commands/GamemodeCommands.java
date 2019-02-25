package nerdhub.fabricessentials.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import nerdhub.fabricessentials.utils.GamemodeHelper;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.GameMode;

import java.util.*;

public class GamemodeCommands {

    public static void registerGamemodeCommands() {
        Map<String, GameMode> gamemodesMap = new HashMap<String, GameMode>() {{
            put("gmc", GameMode.CREATIVE);
            put("gms", GameMode.SURVIVAL);
            put("gma", GameMode.ADVENTURE);
            put("gmsp", GameMode.SPECTATOR);
        }};

        gamemodesMap.forEach((name, gameMode) -> CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal(name)
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    context.getSource().getPlayer().setGameMode(gameMode);
                    return 1;
                })));

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> {
            ArgumentBuilder<ServerCommandSource, ?> builder = serverCommandSourceCommandDispatcher.getRoot().getChild("gamemode").createBuilder();

            for (GameMode mode : GameMode.values()) {
                if (mode != GameMode.INVALID) {
                    builder.then(ServerCommandManager.literal(String.valueOf(mode.ordinal()))
                            .executes(context -> GamemodeHelper.method_13387(context, Collections.singleton(context.getSource().getPlayer()), mode))
                            .then(ServerCommandManager.argument("target", EntityArgumentType.multiplePlayer())
                                    .executes(source -> GamemodeHelper.method_13387(source, EntityArgumentType.method_9310(source, "target"), mode))));
                }
            }
            serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder<ServerCommandSource>) builder);
        });

        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> ServerCommandManager.literal("gm")
                .requires(source -> source.hasPermissionLevel(4))
                .then(ServerCommandManager.argument("mode", IntegerArgumentType.integer())
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            switch (IntegerArgumentType.getInteger(context, "mode")) {
                                case 0:
                                    playerEntity.setGameMode(GameMode.SURVIVAL);
                                    break;
                                case 1:
                                    playerEntity.setGameMode(GameMode.CREATIVE);
                                    break;
                                case 3:
                                    playerEntity.setGameMode(GameMode.ADVENTURE);
                                    break;
                                case 4:
                                    playerEntity.setGameMode(GameMode.SPECTATOR);
                                    break;
                                default:
                                    playerEntity.setGameMode(GameMode.SURVIVAL);
                                    break;
                            }

                            return 1;
                        }))
                .then(ServerCommandManager.argument("mode", StringArgumentType.string())
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                            switch (StringArgumentType.getString(context, "mode").toLowerCase()) {
                                case "s":
                                    playerEntity.setGameMode(GameMode.SURVIVAL);
                                    break;
                                case "c":
                                    playerEntity.setGameMode(GameMode.CREATIVE);
                                    break;
                                case "a":
                                    playerEntity.setGameMode(GameMode.ADVENTURE);
                                    break;
                                case "sp":
                                    playerEntity.setGameMode(GameMode.SPECTATOR);
                                    break;
                                default:
                                    playerEntity.addChatMessage(new TranslatableTextComponent("gamemode.invalidgm", StringArgumentType.getString(context, "mode")).setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), true);
                                    break;
                            }

                            return 1;
                        })));
    }
}
