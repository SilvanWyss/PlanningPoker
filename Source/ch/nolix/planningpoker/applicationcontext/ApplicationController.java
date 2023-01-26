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
	
	private final DatabaseAdapter databaseAdapter;
	
	private final IEventController eventController;
	
	private ApplicationController(
		final DatabaseAdapter databaseAdapter,
		final IEventController eventController
	) {
		
		GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DatabaseAdapter.class).isNotNull();
		GlobalValidator.assertThat(eventController).thatIsNamed("event controller").isNotNull();
				
		this.databaseAdapter = databaseAdapter;
		this.eventController = eventController;
	}
	
	@Override
	public IDataController createDataController() {
		return DataController.usingDatabaseAdapter(databaseAdapter);
	}
	
	@Override
	public IEventController getRefEventController() {
		return eventController;
	}
}
