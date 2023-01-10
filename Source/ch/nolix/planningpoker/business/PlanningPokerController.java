package ch.nolix.planningpoker.business;

import ch.nolix.planningpokerapi.businessapi.IDataController;
import ch.nolix.planningpokerapi.businessapi.IPlanningPokerController;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;

public final class PlanningPokerController implements IPlanningPokerController {
	
	public static PlanningPokerController withDatabaseAdapter(final DatabaseAdapter databaseAdapter) {
		return new PlanningPokerController(databaseAdapter);
	}
	
	private final IDataController dataController;
	
	private PlanningPokerController(final DatabaseAdapter databaseAdapter) {
		dataController = DataController.withDatabaseAdapter(databaseAdapter);
	}
	
	@Override
	public IDataController getRefDataController() {
		return dataController;
	}
}
