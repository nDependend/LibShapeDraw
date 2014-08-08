package libshapedraw.internal.bootstrap;

import libshapedraw.internal.bootstrap.LSDBootstrapBase;
import libshapedraw.internal.LSDController;
import libshapedraw.ApiInfo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import net.minecraft.client.Minecraft;

/**
 * Internal class. Mods using the LibShapeDraw API can safely ignore this.
 * Rather, instantiate {@link libshapedraw.LibShapeDraw}.
 * <p>
 * This is a Forge/FML mod (independent, but compatible with LiteLoader) that
 * delegates bootstrapping LibShapeDraw to the wrapper abstraction (see
 * {@link libshapedraw.internal.bootstrap.LSDBootstrapBase}). 
 */
@Mod(modid = "@libshapedraw.name@", name = "@libshapedraw.name@", version = "@libshapedraw.version@")
public class FMLModLibShapeDraw extends LSDBootstrapBase {

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(this);
	}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (isDelegate) {
            onPostInitBootstrap(Minecraft.getMinecraft());
        }
    }

    @SubscribeEvent
    public void onJoinGame(ClientConnectedToServerEvent event) {
        if (isDelegate) {
            onJoinGameBootstrap();
        }
    }

    @SubscribeEvent
    public void onTickInGame(TickEvent event) {
        if (isDelegate) {
            Minecraft mc = Minecraft.getMinecraft();
			if ((event.phase == TickEvent.Phase.END) && (event.type == TickEvent.Type.CLIENT) 
                && (mc.theWorld != null && mc.thePlayer != null)) {
				// game ticks only, not every render frame.
				onGameTickBootstrap();
			}
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (isDelegate) {
            Minecraft mc = Minecraft.getMinecraft();
            if ((event.phase == TickEvent.Phase.END)
                && (mc.theWorld != null && mc.thePlayer != null)) {
                checkRenderHook();
            }
        }
    }
}
