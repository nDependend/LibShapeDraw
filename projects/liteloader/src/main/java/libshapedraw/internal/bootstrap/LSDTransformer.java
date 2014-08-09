package libshapedraw.internal.bootstrap;

import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.InjectionPoint;
import com.mumfrey.liteloader.transformers.event.inject.BeforeInvoke;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

import static libshapedraw.internal.bootstrap.LSDObfTable.*;

/**
 * Internal class.  This transformer injects a 20 tps callback at the same
 * location as FML's post-client tick event, so that
 * {@link libshapedraw.event.LSDGameTickEvent} will behave consistently across
 * the two loaders.
 */
public class LSDTransformer extends EventInjectionTransformer
{
	@Override
	protected void addEvents()
	{
		Event onGameTickEnd = Event.getOrCreate("onGameTickEnd", false);
		MethodInfo mdRunTick = new MethodInfo(Minecraft, runTick, Void.TYPE);
		// the last endSection is where FML fires the post-client tick event
		InjectionPoint lastEndSection = new BeforeInvoke(new MethodInfo(Profiler, endSection, Void.TYPE), 1);

		this.addEvent(onGameTickEnd, mdRunTick, lastEndSection);

		onGameTickEnd.addListener(new MethodInfo("libshapedraw.internal.bootstrap.LiteModLibShapeDraw", "onGameTickEnd"));
	}
}
