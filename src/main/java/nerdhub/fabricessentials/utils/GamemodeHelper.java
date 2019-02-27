package nerdhub.fabricessentials.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.GameMode;

import java.util.Collection;
import java.util.Iterator;

//Basically methods called from the GameModeCommand class bcs they were private
public class GamemodeHelper {

    public static int method_13387(CommandContext<ServerCommandSource> commandContext_1, Collection<ServerPlayerEntity> collection_1, GameMode gameMode_1) {
        int int_1 = 0;
        Iterator var4 = collection_1.iterator();

        while(var4.hasNext()) {
            ServerPlayerEntity serverPlayerEntity_1 = (ServerPlayerEntity)var4.next();
            if (serverPlayerEntity_1.interactionManager.getGameMode() != gameMode_1) {
                serverPlayerEntity_1.setGameMode(gameMode_1);
                method_13390(commandContext_1.getSource(), serverPlayerEntity_1, gameMode_1);
                ++int_1;
            }
        }

        return int_1;
    }

    public static void method_13390(ServerCommandSource serverCommandSource_1, ServerPlayerEntity serverPlayerEntity_1, GameMode gameMode_1) {
        TextComponent textComponent_1 = new TranslatableTextComponent("gameMode." + gameMode_1.getName(), new Object[0]);
        if (serverCommandSource_1.getEntity() == serverPlayerEntity_1) {
            serverCommandSource_1.sendFeedback(new TranslatableTextComponent("commands.gamemode.success.self", new Object[]{textComponent_1}), true);
        } else {
            if (serverCommandSource_1.getWorld().getGameRules().getBoolean("sendCommandFeedback")) {
                serverPlayerEntity_1.appendCommandFeedback(new TranslatableTextComponent("gameMode.changed", new Object[]{textComponent_1}));
            }

            serverCommandSource_1.sendFeedback(new TranslatableTextComponent("commands.gamemode.success.other", new Object[]{serverPlayerEntity_1.getDisplayName(), textComponent_1}), true);
        }

    }
}
