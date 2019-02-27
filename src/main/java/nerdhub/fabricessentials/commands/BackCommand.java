package nerdhub.fabricessentials.commands;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;

public class BackCommand {

    public static Map<UUID, Vec3d> previousPlayerPositions = Maps.newHashMap();

    public static void registerBackCommand() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(ServerCommandManager
                .literal("back")
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    if(previousPlayerPositions.containsKey(playerEntity.getUuid())) {
                        Vec3d pos = previousPlayerPositions.get(playerEntity.getUuid());
                        playerEntity.networkHandler.teleportRequest(pos.x, pos.y, pos.z, playerEntity.pitch, playerEntity.yaw);
                        playerEntity.addChatMessage(new TranslatableTextComponent("back.success").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    }else {
                        playerEntity.addChatMessage(new TranslatableTextComponent("back.nullentry").setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);
                    }

                    return 1;
                })));
    }
}
