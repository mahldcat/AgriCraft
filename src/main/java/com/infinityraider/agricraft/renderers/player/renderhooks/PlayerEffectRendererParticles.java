package com.infinityraider.agricraft.renderers.player.renderhooks;

import com.infinityraider.agricraft.renderers.tessellation.ITessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class PlayerEffectRendererParticles extends PlayerEffectRenderer {
    protected final ResourceLocation texture;
    private int counter = 0;

    protected PlayerEffectRendererParticles() {
        this.texture = getParticleTexture();
    }

    protected abstract ResourceLocation getParticleTexture();

    protected abstract short getSpawnDelay();

    protected abstract Particle getParticle(EntityPlayer player, float partialTick);

    @Override
    void renderEffects(ITessellator tessellator, EntityPlayer player, RenderPlayer renderer, float partialTick) {
        short delay = getSpawnDelay();
        counter++;
        if (counter >= delay) {
            Minecraft.getMinecraft().effectRenderer.addEffect(getParticle(player, partialTick));
            counter = 0;
        }
    }
}