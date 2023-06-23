package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

public interface IPlanningPokerContext {
	
	IDatabaseAdapter createDatabaseAdapter();
	
	IRoomChangeNotifier getOriRoomChangeNotifier();
}
