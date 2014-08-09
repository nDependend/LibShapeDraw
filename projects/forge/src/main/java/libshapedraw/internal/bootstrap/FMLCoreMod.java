package libshapedraw.internal.bootstrap;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

/**
 * To provide compatibility with different install scenarios, this internal
 * class (coupled with an entry in the manifest; see build.gradle) marks
 * LibShapeDraw as an FML coremod.
 * <p>
 * FML coremods can do a number of things that regular FML mods cannot, such as
 * manipulating bytecode. We don't have any use for that feature, so this class
 * is just a dummy implementation.
 * <p>
 * Before the 1.6 vanilla launcher changes, the feature that we needed from
 * coremods was the ability to be visible to other mods via the classpath.
 * Any jars/zips (a.k.a. mod containers) that FML finds in the "mods" directory
 * are loaded, but the mods inside are isolated from mods that live in other
 * containers.
 * <p>
 * This technique is now obsolete thanks to the LaunchClassLoader alternative.
 * <p>
 * The feature that we *do* need is to signal that this jar shouldn't be
 * considered twice with respect to mod discovery.  Normally, this mod's jar
 * would only be discovered in the "mods" directory, but LiteLoader's
 * transformer will also inject the jar into the classpath, leading FML to
 * discover the mod twice.  Including a coremod will signal to FML that this
 * jar should also be ignored if it is found on the classpath, thereby avoiding
 * duplicate detection.  We also need to implement IFMLLoadingPlugin and add a
 * FMLCorePlugin entry to the jar's manifest. We jump through those hoops to
 * support users who use both loaders at once.
 * <p>
 * This coremod will only remain included contingent upon LiteLoader providing
 * a proper 20 tps callback, making
 * {@link libshapedraw.internal.bootstrap.LSDTransformer} unnecessary.  Modders
 * should therefore not expect its isolation-breaking capabilities to exist
 * in future versions.
 */
public class FMLCoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        // do nothing
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
