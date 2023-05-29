package ch.nolix.planningpokerapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.CloseStateRequestableTriggerable;

public interface IRoomChangeNotifier {
	
	void noteRoomChange(String roomId);
	
	void registerSubscriberForRoomChange(String roomId, CloseStateRequestableTriggerable subscriber);
}
