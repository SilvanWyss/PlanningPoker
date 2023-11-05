package ch.nolix.planningpoker.logic.applicationcontext;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.graphic.image.Image;
import ch.nolix.systemapi.graphicapi.imageapi.IImage;

public final class PlanningPokerContext implements IPlanningPokerContext {

  private static final String APPLICATION_LOGO_RESOURCE_PATH = "ch/nolix/planningpoker/resource/poker_card.jpg";

  public static final IImage APPLICATION_LOGO = Image
    .fromResource(APPLICATION_LOGO_RESOURCE_PATH)
    .withWidthAndHeight(300, 400);

  private final ch.nolix.system.objectdatabase.database.DataAdapter internalDatabaseAdapter;

  private final IRoomChangeNotifier roomChangeNotifier = new RoomChangeNotifier();

  private PlanningPokerContext(final ch.nolix.system.objectdatabase.database.DataAdapter databaseAdapter) {

    GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DatabaseAdapter.class).isNotNull();

    this.internalDatabaseAdapter = databaseAdapter;
  }

  public static PlanningPokerContext withDatabaseAdapter(
    final ch.nolix.system.objectdatabase.database.DataAdapter databaseAdapter) {
    return new PlanningPokerContext(databaseAdapter);
  }

  @Override
  public IImage getApplicationLogo() {
    return APPLICATION_LOGO;
  }

  @Override
  public IDataAdapter createDataSupplier() {
    return DatabaseAdapter.usingDatabaseAdapter(internalDatabaseAdapter.getEmptyCopy());
  }

  @Override
  public IRoomChangeNotifier getStoredRoomChangeNotifier() {
    return roomChangeNotifier;
  }
}
