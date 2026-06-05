package Exy.relivingthemoment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SoulDebtStatusEffect extends StatusEffect {

    public static final int COOLDOWN_TICKS = 20 * 60;

    protected SoulDebtStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x3a0a5c);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

    }

    public static float getDelta(LivingEntity entity) {
        var instance = entity.getStatusEffect(Relivingthemoment.SOUL_DEBT);
        if (instance == null) return 0f;
        return (float) instance.getDuration() / COOLDOWN_TICKS;
    }
}
