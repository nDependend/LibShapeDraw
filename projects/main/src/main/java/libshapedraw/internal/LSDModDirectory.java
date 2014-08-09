package libshapedraw.internal;

import java.io.File;
import java.lang.reflect.Field;

import libshapedraw.ApiInfo;
import libshapedraw.internal.bootstrap.LSDBootstrapBase;

/**
 * Internal class.
 * <p>
 * This is necessary because it is entirely possible that the LSDController
 * will be instantiated before mod_LibShapeDraw: other mods are allowed to
 * create LibShapeDraw API instances whenever they like.
 * <p>
 * LSDController needs to know where to locate the log file. We have to ask
 * Minecraft directly instead of going through the potentially non-existing
 * mod_LibShapeDraw instance.
 * <p>
 * Placing a static accessor in LSDBootstrapBase keeps the API clean of
 * obfuscated code.
 */
public class LSDModDirectory {
    public static final File DIRECTORY = new File(LSDBootstrapBase.getMinecraftDir(), "mods" + File.separator + ApiInfo.getName());
}
