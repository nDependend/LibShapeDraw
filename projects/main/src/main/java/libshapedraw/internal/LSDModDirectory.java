package libshapedraw.internal;

import java.io.File;
import java.lang.reflect.Field;

import libshapedraw.ApiInfo;
import net.minecraft.client.Minecraft;

import com.google.common.io.Files;

/**
 * Internal class. This is the one exception to the rule that no Minecraft
 * classes are referenced outside of mod_LibShapeDraw, making the API clean and
 * free of obfuscated code.
 * <p>
 * This is necessary because it is entirely possible that the LSDController
 * will be instantiated before mod_LibShapeDraw: other mods are allowed to
 * create LibShapeDraw API instances whenever they like.
 * <p>
 * LSDController needs to know where to locate the log file. We have to ask
 * Minecraft directly instead of going through the potentially non-existing
 * mod_LibShapeDraw.
 * <p>
 * We can't easily make this a static method on mod_LibShapeDraw either because
 * that class lives in the root package for non-dev environments: there's no way
 * to reference it other than reflection.
 */
public class LSDModDirectory {
    public static final File DIRECTORY = new File(getMinecraftDir(), "mods" + File.separator + ApiInfo.getName());

    /**
     * Use reflection to invoke the static method. This supports both normal
     * play (reobfuscated minecraft.jar) and development (deobfuscated classes
     * in MCP).
     */
    private static File getMinecraftDir() {
        try {
            Field f;
            try {
                f = Minecraft.class.getDeclaredField("w");
            } catch (NoSuchFieldException e) {
                try {
                    f = Minecraft.class.getDeclaredField("field_71412_D");
                } catch (NoSuchFieldException e2) {
                    f = Minecraft.class.getDeclaredField("mcDataDir");
                }
            }
            Minecraft mc = Minecraft.getMinecraft();
            if (mc != null) {
                return (File) f.get(mc);
            } else {
                // For the test suite, we return a temporary directory
                File tempDir = Files.createTempDir();
                File modsDir = new File(tempDir, "mods");
                if (!modsDir.exists()) {
                    modsDir.mkdir();
                }
                return tempDir;
            }
        } catch (Exception e) {
            throw new LSDInternalReflectionException("unable to reflect Minecraft.mcDataDir", e);
        }
    }
}
