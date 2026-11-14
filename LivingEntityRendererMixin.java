package me.anastarawneh.mccinametagmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.anastarawneh.mccinametagmod.util.LivingEntityRenderStateExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin extends EntityRendererMixin {
    @Unique
    private static Component label = Component.empty();

    @Unique
    private boolean applyLabel(LivingEntity livingEntity) {
        if (!MCCINametagMod.modEnabled()) return false;
        if (!(livingEntity instanceof Player player) || !player.isLocalPlayer()) return false;

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        boolean visible = !livingEntity.isInvisibleTo(localPlayer);

        boolean hudShown = !minecraft.gui.hud.isHidden();
        var screen = minecraft.gui.screen();
        boolean screenOk = screen == null || screen instanceof ChatScreen
                || (screen instanceof InventoryScreen && MCCINametagMod.getConfig().showInInventory);
        return hudShown && visible && !livingEntity.isVehicle() && screenOk;
    }

    /**
     * The name shown is the same one other players see above your head: the display name with the
     * server's team prefix/suffix (rank, media tag...). If the server only puts the rank in the
     * tab list, the tab list name is used instead.
     */
    @Unique
    private static Component buildLabel(Player player) {
        Component name = player.getDisplayName();
        if (name.getString().equals(player.getName().getString()) && Minecraft.getInstance().getConnection() != null) {
            PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(player.getUUID());
            if (info != null && info.getTabListDisplayName() != null) {
                name = info.getTabListDisplayName();
            }
        }
        return name;
    }

    @Override
    public void extractRenderState(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {
        if (entity instanceof Player player && player.isLocalPlayer()) {
            boolean apply = applyLabel(player);
            ((LivingEntityRenderStateExtension) state).setApplyLabel(apply);
            if (apply) {
                label = buildLabel(player);
                state.nameTagAttachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot(partialTick));
            }
        } else {
            ((LivingEntityRenderStateExtension) state).setApplyLabel(false);
        }
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        if (!((LivingEntityRenderStateExtension) state).getApplyLabel()) return;
        if (state.nameTagAttachment == null) return;
        // Same call vanilla uses for other players' name tags (see-through when not sneaking).
        collector.submitNameTag(poseStack, state.nameTagAttachment, 0, label, !state.isDiscrete, state.lightCoords, camera);
    }
}
