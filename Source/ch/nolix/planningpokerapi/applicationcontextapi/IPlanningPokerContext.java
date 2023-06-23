package ch.nolix.planningpokerapi.applicationcontextapi;

public interface IPlanningPokerContext {
	
	IDatabaseAdapter createDatabaseAdapter();
	
	IRoomChangeNotifier getOriRoomChangeNotifier();
}
