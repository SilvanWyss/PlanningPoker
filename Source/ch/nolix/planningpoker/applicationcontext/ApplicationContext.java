package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.planningpokerapi.applicationcontextapi.IEventController;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class ApplicationContext implements IApplicationContext {
	
	public static ApplicationContext withDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		return new ApplicationContext(databaseAdapter);
	}
	
	private final DatabaseAdapter databaseAdapter;
	
	private final IEventController eventController = new EventController();
	
	private ApplicationContext(final DatabaseAdapter databaseAdapter) {
		
		GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DatabaseAdapter.class).isNotNull();
		
		this.databaseAdapter = databaseAdapter;
	}
	
	@Override
	public IApplicationController createApplicationController() {
		return ApplicationController.withDatabaseAdapterAndEventController(createDatabaseAdapter(), eventController);
	}
	
	private DatabaseAdapter createDatabaseAdapter() {
		return databaseAdapter.getEmptyCopy();
	}
}
