package libshapedraw.internal.bootstrap;

import com.mumfrey.liteloader.core.runtime.Obf;

/**
 * Internal class.  This lists obfuscation entries for use in
 * {@link libshapedraw.internal.bootstrap.LSDTransformer}.
 */
public class LSDObfTable extends Obf
{
	// TODO 1.7.10
	public static LSDObfTable Minecraft = new LSDObfTable("net.minecraft.client.Minecraft", "bao");
	public static LSDObfTable runTick = new LSDObfTable("func_71407_l", "p", "runTick");

	public static LSDObfTable Profiler = new LSDObfTable("net.minecraft.profiler.Profiler", "qi");
	public static LSDObfTable endSection = new LSDObfTable("func_76319_b", "b", "endSection");
	
	// Constructor for when seargeName == mcpName
	protected LSDObfTable(String seargeName, String obfName)
	{
		super(seargeName, obfName);
	}

	protected LSDObfTable(String seargeName, String obfName, String mcpName)
	{
		super(seargeName, obfName, mcpName);
	}
}
