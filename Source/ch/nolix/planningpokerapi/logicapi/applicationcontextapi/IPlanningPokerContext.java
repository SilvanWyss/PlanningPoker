package ch.nolix.planningpokerapi.logicapi.applicationcontextapi;

import ch.nolix.coreapi.programcontrolapi.datasupplierapi.IDataSupplierFactory;
import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationService;

public interface IPlanningPokerContext extends IDataSupplierFactory<IDataAdapter>, IWebApplicationService {

  IRoomChangeNotifier getStoredRoomChangeNotifier();
}
