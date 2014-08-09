package libshapedraw.internal.bootstrap;

import java.io.File;

import libshapedraw.internal.bootstrap.LSDBootstrapBase;
import libshapedraw.internal.LSDController;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;

/**
 * Internal class. Mods using the LibShapeDraw API can safely ignore this.
 * Rather, instantiate {@link libshapedraw.LibShapeDraw}.
 * <p>
 * This is a LiteLoader mod (independent, but compatible with Forge/FML) that
 * delegates bootstrapping LibShapeDraw to the wrapper abstraction (see
 * {@link libshapedraw.internal.bootstrap.LSDBootstrapBase}). 
 */
public class LiteModLibShapeDraw extends LSDBootstrapBase implements InitCompleteListener, JoinGameListener {

    @Override
    public void init(File configPath) {
        // Do nothing; wait until onInitCompleted.
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
        //
    }

    @Override
    public void onInitCompleted(Minecraft minecraft, LiteLoader loader) {
        if (isDelegate) {
            onPostInitBootstrap(minecraft);
        }
    }

    @Override
    public void onJoinGame(INetHandler netHandler, S01PacketJoinGame joinGamePacket) {
        if (isDelegate) {
            onJoinGameBootstrap();
        }
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        if (isDelegate) {
            if (inGame) {
                checkRenderHook();
            }
        }
    }

    public static void onGameTickEnd(EventInfo<Minecraft> e)
    {
        LiteModLibShapeDraw lsdMod = null;
        try {
            lsdMod = LiteLoader.getInstance().getMod(LiteModLibShapeDraw.class.getSimpleName());
        } catch (Exception error) {
        }

        if (lsdMod.isDelegate) {
            if ((e.getSource().theWorld != null) && (e.getSource().thePlayer != null)) {
                // game ticks only, not every render frame.
                LSDController.getLog().info("LiteLoader onGameTick");
                lsdMod.onGameTickBootstrap();
            }
        }
    }
}
