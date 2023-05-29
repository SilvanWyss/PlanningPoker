package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class ApplicationController implements IApplicationController {
	
	public static ApplicationController withDatabaseAdapterAndEventController(
		final DatabaseAdapter databaseAdapter,
		final IRoomChangeNotifier eventController
	) {
		return new ApplicationController(databaseAdapter, eventController);
	}
	
	private final DatabaseAdapter databaseAdapter;
	
	private final IRoomChangeNotifier eventController;
	
	private ApplicationController(
		final DatabaseAdapter databaseAdapter,
		final IRoomChangeNotifier eventController
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
	public IRoomChangeNotifier getOriEventController() {
		return eventController;
	}
}
