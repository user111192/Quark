package vazkii.quark.api.event;

import net.minecraftforge.eventbus.api.Event;
import vazkii.quark.api.IAdvancementModifierDelegate;

public class GatherAdvancementModifiersEvent extends Event {

	public final IAdvancementModifierDelegate delegate;
	
	public GatherAdvancementModifiersEvent(IAdvancementModifierDelegate delegate) {
		this.delegate = delegate;
	}
	
}
