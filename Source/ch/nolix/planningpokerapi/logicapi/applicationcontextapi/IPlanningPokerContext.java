package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.systemapi.applicationapi.applicationcontextapi.IDataAdapterFactory;
import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationContext;

public interface IPlanningPokerContext extends IDataAdapterFactory<IDataAdapter>, IWebApplicationContext {
	
	IRoomChangeNotifier getStoredRoomChangeNotifier();
}
