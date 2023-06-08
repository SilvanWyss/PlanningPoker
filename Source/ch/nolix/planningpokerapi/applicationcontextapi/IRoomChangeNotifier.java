package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IRoomChangeNotifier {
	
	void noteRoomChange(String roomId);
	
	void registerRoomSubscriberIfNotRegistered(String roomId, IRoomSubscriber roomSubscriber);
}
