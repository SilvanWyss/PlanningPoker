package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class ApplicationContext implements IApplicationContext {
	
	public static ApplicationContext withDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		return new ApplicationContext(databaseAdapter);
	}
	
	private final DatabaseAdapter databaseAdapter;
	
	private final IRoomChangeNotifier roomChangeNotifier = new RoomChangeNotifier();
	
	private ApplicationContext(final DatabaseAdapter databaseAdapter) {
		
		GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DatabaseAdapter.class).isNotNull();
		
		this.databaseAdapter = databaseAdapter;
	}
	
	@Override
	public IDataController createDataController() {
		return DataController.usingDatabaseAdapter(databaseAdapter.getEmptyCopy());
	}
	
	@Override
	public IRoomChangeNotifier getOriRoomChangeNotifier() {
		return roomChangeNotifier;
	}
}
