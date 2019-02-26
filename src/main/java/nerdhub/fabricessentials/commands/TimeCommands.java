package nerdhub.fabricessentials.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import nerdhub.fabricessentials.utils.TimePoll;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.ServerCommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCommands {

    public static TimePoll timePoll;

    public static void registerTimeCommands() {
        CommandRegistry.INSTANCE.register(false, serverCommandSourceCommandDispatcher -> {
            serverCommandSourceCommandDispatcher.register(
                    ServerCommandManager.literal("day")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(context -> {
                        context.getSource().getWorld().setTime(0);
                        context.getSource().getPlayer().addChatMessage(new TranslatableTextComponent("time.day").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                        return 1;
                    }));

            ServerCommandManager.literal("night")
                    .requires(source -> source.hasPermissionLevel(4))
                    .executes(context -> {
                        context.getSource().getWorld().setTime(13000);
                        context.getSource().getPlayer().addChatMessage(new TranslatableTextComponent("time.night").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                        return 1;
                    });

                    ServerCommandManager.literal("poll")
                            .then(ServerCommandManager.argument("time", StringArgumentType.string())
                                    .executes(context -> {
                                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                                        if (timePoll != null) {
                                            if (!timePoll.hasPlayerVoted(playerEntity.getUuid())) {
                                                timePoll.addVote(playerEntity.getUuid(), StringArgumentType.getString(context, "time").toLowerCase());
                                                playerEntity.addChatMessage(new TranslatableTextComponent("time.vote_added").setStyle(new Style().setColor(TextFormat.GOLD)), false);
                                            } else {
                                                context.getSource().getPlayer().addChatMessage(new TranslatableTextComponent("time.voted").setStyle(new Style().setColor(TextFormat.LIGHT_PURPLE)), false);
                                            }
                                        } else {

                                            timePoll = new TimePoll(StringArgumentType.getString(context, "time"));
                                            timePoll.addVote(playerEntity.getUuid(), StringArgumentType.getString(context, "time").toLowerCase());

                                            context.getSource().getMinecraftServer().getPlayerManager().sendToAll(
                                                    new TranslatableTextComponent("time.poll_create", timePoll.getTime())
                                                            .append(new TranslatableTextComponent("time.poll_votes_yes", timePoll.getVotesYes()))
                                                            .append(new TranslatableTextComponent("time.poll_votes_no", timePoll.getVoteNo())).setStyle(new Style().setColor(TextFormat.GOLD)));

                                            Timer timer = new Timer();

                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    String result = "current";
                                                    if (timePoll.didPollWin()) {
                                                        result = timePoll.getTime();
                                                        context.getSource().getWorld().setTime(result.equals("day") ? 0 : 13000);
                                                    }

                                                    timePoll = null;
                                                    context.getSource().getMinecraftServer().getPlayerManager().sendToAll(new TranslatableTextComponent("time.poll_finished", result).setStyle(new Style().setColor(TextFormat.GOLD)));
                                                }
                                            }, 60000);
                                        }
                                        return 1;
                                    }));
        });
    }
}
