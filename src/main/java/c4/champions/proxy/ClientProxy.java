package c4.champions.proxy;

import c4.champions.Champions;
import c4.champions.client.EventHandlerClient;
import c4.champions.client.fx.ParticleRank;
import c4.champions.client.layer.LayerShielding;
import c4.champions.client.renderer.RenderArcticSpark;
import c4.champions.common.entity.EntityArcticSpark;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Champions.MODID, value = Side.CLIENT)
public class ClientProxy implements IProxy {

    private static final Random rand = new Random();

    @Override
    public void preInit(FMLPreInitializationEvent evt) {
        RenderingRegistry.registerEntityRenderingHandler(EntityArcticSpark.class, RenderArcticSpark.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    @Override
    public void postInit(FMLPostInitializationEvent evt) {
        Map<Class <? extends Entity> , Render<? extends Entity >> renderMap = Minecraft.getMinecraft().getRenderManager()
                .entityRenderMap;

        for (Render<? extends Entity> render : renderMap.values()) {

            if (render instanceof RenderLiving) {
                RenderLiving livingRender = (RenderLiving)render;
                ModelBase model = livingRender.getMainModel();
                livingRender.addLayer(new LayerShielding(livingRender, model));
            }
        }
    }

    @Override
    public void generateRankParticles(EntityLiving living, int color) {
        Minecraft.getMinecraft().effectRenderer.addEffect(
                new ParticleRank(living.world,
                        living.posX + (living.getRNG().nextDouble() - 0.5D) * (double)living.width,
                        living.posY + living.getRNG().nextDouble() * (double)living.height,
                        living.posZ + (living.getRNG().nextDouble() - 0.5D) * (double)living.width, 0, 0, 0,
                        color));
    }
}