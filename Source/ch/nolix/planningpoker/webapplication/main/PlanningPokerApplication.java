package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.core.document.node.FileNode;
import ch.nolix.core.document.node.MutableNode;
import ch.nolix.planningpoker.datamodel.schema.SchemaCatalogue;
import ch.nolix.planningpoker.logic.applicationcontext.PlanningPokerContext;
import ch.nolix.planningpoker.webapplication.view.InitializeSession;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.main.Application;
import ch.nolix.system.application.webapplication.WebClient;
import ch.nolix.system.objectdatabase.database.DataAdapter;
import ch.nolix.system.objectdatabase.dataadapter.NodeDataAdapter;

public final class PlanningPokerApplication
extends Application<WebClient<IPlanningPokerContext>, IPlanningPokerContext> {

  public static final String APPLICATION_NAME = "Planning Poker";

  public static PlanningPokerApplication withFileNodeDatabase(final String filePath) {

    final var fileNodeDatabase = new FileNode(filePath);

    final var databaseAdapter = NodeDataAdapter
      .forNodeDatabase(fileNodeDatabase)
      .withName("PlanningPokerDatabase")
      .usingSchema(SchemaCatalogue.SCHEMA);

    return new PlanningPokerApplication(databaseAdapter);
  }

  public static PlanningPokerApplication withInMemoryNodeDatabase() {

    final var nodeDatabase = new MutableNode();

    final var databaseAdapter = NodeDataAdapter
      .forNodeDatabase(nodeDatabase)
      .withName("PlanningPokerDatabase")
      .usingSchema(SchemaCatalogue.SCHEMA);

    return new PlanningPokerApplication(databaseAdapter);
  }

  private PlanningPokerApplication(final DataAdapter databaseAdapter) {
    super(PlanningPokerContext.withDatabaseAdapter(databaseAdapter));
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
