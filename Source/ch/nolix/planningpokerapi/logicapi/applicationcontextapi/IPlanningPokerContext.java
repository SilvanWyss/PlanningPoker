package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.systemapi.applicationapi.applicationcontextapi.IDataSupplierFactory;
import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationContext;

public interface IPlanningPokerContext extends IDataSupplierFactory<IDataAdapter>, IWebApplicationContext {

  IRoomChangeNotifier getStoredRoomChangeNotifier();
}
