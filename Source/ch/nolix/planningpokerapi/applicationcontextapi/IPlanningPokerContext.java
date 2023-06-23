package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IPlanningPokerContext {
	
	IDataController createDataController();
	
	IRoomChangeNotifier getOriRoomChangeNotifier();
}
