package me.anastarawneh.mccinametagmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Empty hooks on EntityRenderer. LivingEntityRendererMixin overrides them, so the call
 * (dispatched virtually from the head of the vanilla methods) lands in its implementation.
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    public void extractRenderState(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {}

    @Inject(method = "submit", at = @At("HEAD"))
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {}
}
