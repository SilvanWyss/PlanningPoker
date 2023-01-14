package ch.nolix.planningpokerapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;

public interface IEventController {
	
	void noteRoomChange(String roomIdentification);
	
	void registerSubscriberForRoomChange(String roomIdentification, CloseStateRequestableTriggerable subscriber);
}
