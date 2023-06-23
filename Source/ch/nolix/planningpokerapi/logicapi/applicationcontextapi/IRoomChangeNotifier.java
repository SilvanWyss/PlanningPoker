package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

public interface IRoomChangeNotifier {
	
	void noteRoomChange(String roomId);
	
	void registerRoomSubscriberIfNotRegistered(String roomId, IRoomSubscriber roomSubscriber);
}
