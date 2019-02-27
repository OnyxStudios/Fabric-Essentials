package nerdhub.fabricessentials.commands;

import nerdhub.fabricessentials.FabricEssentials;
import nerdhub.fabricessentials.utils.DateHelper;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

import java.lang.management.ManagementFactory;

public class TPSCommand {

    public static void registerTPSCommand() {
        CommandRegistry.INSTANCE.register(true, serverCommandSourceCommandDispatcher -> serverCommandSourceCommandDispatcher.register(ServerCommandManager
                .literal("tps")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                    double tps = FabricEssentials.serverTimer.getAverageTPS();
                    TextFormat color;
                    if(tps >= 18.0) {
                        color = TextFormat.GREEN;
                    }else if(tps >= 15.0) {
                        color = TextFormat.YELLOW;
                    }else {
                        color = TextFormat.RED;
                    }

                    playerEntity.addChatMessage(new TranslatableTextComponent("tps.uptime", DateHelper.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())).setStyle(new Style().setColor(TextFormat.GOLD)), false);
                    playerEntity.addChatMessage(new TranslatableTextComponent("tps.tps",  new StringTextComponent(String.valueOf(tps)).setStyle(new Style().setColor(color))), false);
                    playerEntity.addChatMessage(new TranslatableTextComponent("tps.maxmem",  new StringTextComponent(String.valueOf(Runtime.getRuntime().totalMemory() / 1024 / 1024)).setStyle(new Style().setColor(TextFormat.RED))), false);
                    playerEntity.addChatMessage(new TranslatableTextComponent("tps.freemem",  new StringTextComponent(String.valueOf(Runtime.getRuntime().freeMemory() / 1024 / 1024)).setStyle(new Style().setColor(TextFormat.RED))), false);
                    return 1;
                })
        ));
    }
}
