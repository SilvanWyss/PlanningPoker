package ch.nolix.planningpokerapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.Triggerable;

public interface IRoomSubscriber extends Triggerable {
	
	boolean isActive();
}
