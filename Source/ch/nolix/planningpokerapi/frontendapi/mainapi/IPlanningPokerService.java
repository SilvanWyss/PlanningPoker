package ch.nolix.planningpokerapi.frontendapi.mainapi;

import ch.nolix.coreapi.programcontrolapi.adapterapi.IAdapterFactory;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.systemapi.applicationapi.webapplicationapi.IWebApplicationService;

public interface IPlanningPokerService extends IAdapterFactory<IDataAdapter>, IWebApplicationService {

  IRoomChangeNotifier getStoredRoomChangeNotifier();
}
