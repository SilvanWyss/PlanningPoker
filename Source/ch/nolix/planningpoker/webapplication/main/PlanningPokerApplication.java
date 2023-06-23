package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.core.document.node.MutableNode;
import ch.nolix.coreapi.functionapi.genericfunctionapi.IElementTakerElementGetter;
import ch.nolix.planningpoker.applicationcontext.PlanningPokerContext;
import ch.nolix.planningpoker.datamodel.schema.SchemaCatalogue;
import ch.nolix.planningpoker.webapplication.view.InitializeSession;
import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.main.Application;
import ch.nolix.system.application.webapplication.WebClient;
import ch.nolix.system.objectdatabase.database.DatabaseAdapter;
import ch.nolix.system.objectdatabase.databaseadapter.NodeDatabaseAdapter;
import ch.nolix.systemapi.objectdatabaseapi.schemaapi.ISchema;

public final class PlanningPokerApplication extends Application<WebClient<IPlanningPokerContext>, IPlanningPokerContext> {
	
	public static final String APPLICATION_NAME = "Planning Poker";
	
	public static PlanningPokerApplication withTemporaryNodeDatabase() {
		
		final var nodeDatabase = new MutableNode();
		
		final var databaseAdapterCreator =
		NodeDatabaseAdapter.forNodeDatabase(nodeDatabase).withName("PlanningPokerDatabase");
		
		return new PlanningPokerApplication(databaseAdapterCreator::usingSchema);
	}
	
	private PlanningPokerApplication(
		final IElementTakerElementGetter<ISchema, DatabaseAdapter> databaseAdapterCreator
	) {
		super(PlanningPokerContext.withDatabaseAdapter(databaseAdapterCreator.getOutput(SchemaCatalogue.SCHEMA)));
	}
	
	@Override
	public String getApplicationName() {
		return APPLICATION_NAME;
	}
	
	@Override
	protected Class<?> getInitialSessionClass() {
		return InitializeSession.class;
	}
}
