package nerdhub.fabricessentials.mixins;

import nerdhub.fabricessentials.commands.BackCommand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource damageSource_1, CallbackInfo ci) {
        if(((LivingEntity) (Object) this) instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) (Object) this;
            if(BackCommand.previousPlayerPositions.containsKey(playerEntity.getUuid()))
                BackCommand.previousPlayerPositions.remove(playerEntity.getUuid());

            BackCommand.previousPlayerPositions.put(playerEntity.getUuid(), playerEntity.getPosVector());
        }
    }
}
