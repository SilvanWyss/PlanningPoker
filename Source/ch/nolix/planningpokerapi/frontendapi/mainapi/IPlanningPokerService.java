package ch.nolix.planningpokerapi.frontendapi.mainapi;

import ch.nolix.coreapi.programcontrolapi.datasupplierapi.IDataSupplierFactory;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationService;

public interface IPlanningPokerService extends IDataSupplierFactory<IDataAdapter>, IWebApplicationService {

  IRoomChangeNotifier getStoredRoomChangeNotifier();
}
