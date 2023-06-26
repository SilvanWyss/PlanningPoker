package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationContext;

public interface IPlanningPokerContext extends IWebApplicationContext {
	
	IDatabaseAdapter createDatabaseAdapter();
	
	IRoomChangeNotifier getOriRoomChangeNotifier();
}
