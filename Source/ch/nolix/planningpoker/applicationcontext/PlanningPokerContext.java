package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class PlanningPokerContext implements IPlanningPokerContext {
	
	public static PlanningPokerContext withDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		return new PlanningPokerContext(databaseAdapter);
	}
	
	private final DatabaseAdapter databaseAdapter;
	
	private final IRoomChangeNotifier roomChangeNotifier = new RoomChangeNotifier();
	
	private PlanningPokerContext(final DatabaseAdapter databaseAdapter) {
		
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
