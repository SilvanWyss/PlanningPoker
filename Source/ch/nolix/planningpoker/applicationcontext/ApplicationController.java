package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IEventController;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class ApplicationController implements IApplicationController {
	
	public static ApplicationController withDatabaseAdapterAndEventController(
		final DatabaseAdapter databaseAdapter,
		final IEventController eventController
	) {
		return new ApplicationController(databaseAdapter, eventController);
	}
	
	private final IDataController dataController;
	
	private final IEventController eventController;
	
	private ApplicationController(
		final DatabaseAdapter databaseAdapter,
		final IEventController eventController
	) {
		
		GlobalValidator.assertThat(eventController).thatIsNamed("event controller").isNotNull();
		
		dataController = DataController.withDatabaseAdapter(databaseAdapter);
		this.eventController = eventController;
	}
	
	@Override
	public IDataController getRefDataController() {
		return dataController;
	}
	
	@Override
	public IEventController getRefEventController() {
		return eventController;
	}
}
