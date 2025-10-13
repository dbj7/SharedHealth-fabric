package com.sharedhealth.mixin;

import com.sharedhealth.SharedHealthHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Mixin for healing handling
@Mixin(LivingEntity.class)
public class LivingEntityHealMixin {
    @Inject(method = "heal(F)V", at = @At("HEAD"))

    private void onHeal(float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof ServerPlayerEntity player) {
            SharedHealthHandler.onPlayerHealed(player, amount);
        }
    }
}
