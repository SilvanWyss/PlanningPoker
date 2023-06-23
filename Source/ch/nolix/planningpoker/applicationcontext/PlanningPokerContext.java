package ch.nolix.planningpoker.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;

public final class PlanningPokerContext implements IPlanningPokerContext {
	
	public static PlanningPokerContext withDatabaseAdapter(
		final ch.nolix.system.objectdatabase.database.DatabaseAdapter databaseAdapter
	) {
		return new PlanningPokerContext(databaseAdapter);
	}
	
	private final ch.nolix.system.objectdatabase.database.DatabaseAdapter internalDatabaseAdapter;
	
	private final IRoomChangeNotifier roomChangeNotifier = new RoomChangeNotifier();
	
	private PlanningPokerContext(final ch.nolix.system.objectdatabase.database.DatabaseAdapter databaseAdapter) {
		
		GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DatabaseAdapter.class).isNotNull();
		
		this.internalDatabaseAdapter = databaseAdapter;
	}
	
	@Override
	public IDatabaseAdapter createDatabaseAdapter() {
		return DatabaseAdapter.usingDatabaseAdapter(internalDatabaseAdapter.getEmptyCopy());
	}
	
	@Override
	public IRoomChangeNotifier getOriRoomChangeNotifier() {
		return roomChangeNotifier;
	}
}
