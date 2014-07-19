package libshapedraw.internal.bootstrap;

import java.io.File;

import libshapedraw.internal.bootstrap.LSDBootstrapBase;
import libshapedraw.internal.LSDController;

import com.mumfrey.liteloader.InitCompleteListener;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.core.LiteLoader;

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
    public LiteModLibShapeDraw() {
        controller = LSDController.getInstance();
        controller.initialize(this);
    }

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
        onPostInitBootstrap(minecraft);
    }

    @Override
    public void onJoinGame(INetHandler netHandler, S01PacketJoinGame joinGamePacket) {
        onJoinGameBootstrap();
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        // game ticks only, not every render frame.
        if (inGame && clock) {
            onTickBootstrap();
        }
    }
}
