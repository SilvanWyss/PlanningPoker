package ch.nolix.planningpoker.frontend.main;

import ch.nolix.core.document.node.FileNode;
import ch.nolix.core.document.node.MutableNode;
import ch.nolix.planningpoker.backend.datamodel.SchemaCatalogue;
import ch.nolix.planningpoker.frontend.session.InitializeSession;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.main.Application;
import ch.nolix.system.application.webapplication.WebClient;
import ch.nolix.system.objectdata.adapter.NodeDataAdapter;
import ch.nolix.systemapi.objectdataapi.adapterapi.IDataAdapter;

public final class PlanningPokerApplication
extends Application<WebClient<IPlanningPokerService>, IPlanningPokerService> {

  public static final String APPLICATION_NAME = "Planning Poker";

  private PlanningPokerApplication(final IDataAdapter databaseAdapter) {
    super(PlanningPokerService.withDatabaseAdapter(databaseAdapter));
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

    final var nodeDatabase = MutableNode.createEmpty();

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
