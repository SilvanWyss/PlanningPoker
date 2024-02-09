package ch.nolix.planningpoker.webapplication.main;

import ch.nolix.core.document.node.FileNode;
import ch.nolix.core.document.node.MutableNode;
import ch.nolix.planningpoker.datamodel.schema.SchemaCatalogue;
import ch.nolix.planningpoker.logic.applicationcontext.PlanningPokerContext;
import ch.nolix.planningpoker.webapplication.view.InitializeSession;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.main.Application;
import ch.nolix.system.application.webapplication.WebClient;
import ch.nolix.system.objectdata.dataadapter.NodeDataAdapter;
import ch.nolix.systemapi.objectdataapi.dataadapterapi.IDataAdapter;

public final class PlanningPokerApplication
extends Application<WebClient<IPlanningPokerContext>, IPlanningPokerContext> {

  public static final String APPLICATION_NAME = "Planning Poker";

  private PlanningPokerApplication(final IDataAdapter databaseAdapter) {
    super(PlanningPokerContext.withDatabaseAdapter(databaseAdapter));
  }

  public static PlanningPokerApplication withFileNodeDatabase(final String filePath) {

    final var fileNodeDatabase = new FileNode(filePath);

    final var databaseAdapter = NodeDataAdapter
      .forNodeDatabase(fileNodeDatabase)
      .withName("PlanningPokerDatabase")
      .andSchema(SchemaCatalogue.SCHEMA);

    return new PlanningPokerApplication(databaseAdapter);
  }

  public static PlanningPokerApplication withInMemoryNodeDatabase() {

    final var nodeDatabase = new MutableNode();

    final var databaseAdapter = NodeDataAdapter
      .forNodeDatabase(nodeDatabase)
      .withName("PlanningPokerDatabase")
      .andSchema(SchemaCatalogue.SCHEMA);

    return new PlanningPokerApplication(databaseAdapter);
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
