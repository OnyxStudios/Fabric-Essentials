package nerdhub.fabricessentials.mixins;

import nerdhub.fabricessentials.commands.BackCommand;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket.Flag;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "teleportRequest", at = @At("HEAD"))
    private void teleportRequest(double double_1, double double_2, double double_3, float float_1, float float_2, Set<Flag> set_1, CallbackInfo ci) {
        if(BackCommand.previousPlayerPositions.containsKey(player.getUuid()))
            BackCommand.previousPlayerPositions.remove(player.getUuid());

        BackCommand.previousPlayerPositions.put(player.getUuid(), player.getPosVector());
    }
}
