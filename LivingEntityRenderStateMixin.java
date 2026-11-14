package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.util.LivingEntityRenderStateExtension;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements LivingEntityRenderStateExtension {
    public boolean applyLabel;

    @Override
    public boolean getApplyLabel() {
        return applyLabel;
    }

    @Override
    public void setApplyLabel(boolean applyLabel) {
        this.applyLabel = applyLabel;
    }
}
