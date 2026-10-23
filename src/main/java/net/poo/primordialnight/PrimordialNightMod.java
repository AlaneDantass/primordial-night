package net.poo.primordialnight;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.poo.primordialnight.block.ModBlocks;
import net.poo.primordialnight.entity.ModEntities;
import net.poo.primordialnight.entity.client.YmaPytuRenderer;
import net.poo.primordialnight.item.ModCreativeModeTabs;
import net.poo.primordialnight.item.ModItems;
import net.poo.primordialnight.sound.ModSounds;
import org.slf4j.Logger;

/**
 * Main orchestrator class of the mod (Romulo Henri Best Practice #1:
 * The main class ONLY orchestrates registrations and contains no game logic).
 *
 * Reference Repository: Forge-Tutorial-1.20.X-29-attackAnimation (TutorialMod.java)
 *                       and rise-modpack (BurneriteMod.java / RubyOreMod.java)
 */
@Mod(PrimordialNightMod.MOD_ID)
public class PrimordialNightMod {

    public static final String MOD_ID = "primordialnight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimordialNightMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // TODO (Dev 4): Add ModItems (VOIDBLADE_SWORD, END_COMPASS, YMA_PYTU_SPAWN_EGG) to creative tabs
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.YMA_PYTU_BOSS.get(), YmaPytuRenderer::new);
        }
    }
}
