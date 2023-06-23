package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.Triggerable;

public interface IRoomSubscriber extends Triggerable {
	
	boolean isActive();
}
