package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IRoomChangeNotifier {
	
	void noteRoomChange(String roomId);
	
	void registerRoomSubscriber(String roomId, IRoomSubscriber roomSubscriber);
}
