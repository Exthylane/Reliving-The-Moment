package Exy.relivingthemoment.mixin;

import Exy.relivingthemoment.Relivingthemoment;
import Exy.relivingthemoment.SoulDebtStatusEffect;
import Exy.relivingthemoment.WretchLeatherBeltItem;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || self.hasStatusEffect(Relivingthemoment.SOUL_DEBT)) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack beltStack = WretchLeatherBeltItem.getWornBelt(self);
        if (beltStack.getItem() instanceof WretchLeatherBeltItem belt) {
            Optional<ItemStack> totem = belt.removeFirstStack(beltStack);
            if (totem.isPresent() && totem.get().isOf(Items.TOTEM_OF_UNDYING)) {
                if (self instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                    Criteria.USED_TOTEM.trigger(serverPlayerEntity, totem.get());
                }

                self.setHealth(1.0F);
                self.clearStatusEffects();
                self.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 450, 1));
                self.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 40, 1));
                self.addStatusEffect(new StatusEffectInstance(Relivingthemoment.SOUL_DEBT, SoulDebtStatusEffect.COOLDOWN_TICKS, 0,false,false,false));
                self.getWorld().sendEntityStatus(self, EntityStatuses.USE_TOTEM_OF_UNDYING);

                cir.setReturnValue(true);
                return;
            }
        }

        cir.setReturnValue(false);
    }
}
