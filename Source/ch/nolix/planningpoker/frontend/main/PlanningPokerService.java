package ch.nolix.planningpoker.frontend.main;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.backend.dataadapter.DataAdapter;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IRoomChangeNotifier;
import ch.nolix.system.graphic.image.Image;
import ch.nolix.systemapi.graphicapi.imageapi.IImage;

public final class PlanningPokerService implements IPlanningPokerService {

  private static final String APPLICATION_LOGO_RESOURCE_PATH = "image/poker_card.jpg";

  public static final IImage APPLICATION_LOGO = Image
    .fromResource(APPLICATION_LOGO_RESOURCE_PATH)
    .withWidthAndHeight(300, 400);

  private final ch.nolix.systemapi.objectdataapi.adapterapi.IDataAdapter internalDatabaseAdapter;

  private final IRoomChangeNotifier roomChangeNotifier = new RoomChangeNotifier();

  private PlanningPokerService(final ch.nolix.systemapi.objectdataapi.adapterapi.IDataAdapter databaseAdapter) {

    GlobalValidator.assertThat(databaseAdapter).thatIsNamed(DataAdapter.class).isNotNull();

    this.internalDatabaseAdapter = databaseAdapter;
  }

  public static PlanningPokerService withDatabaseAdapter(
    final ch.nolix.systemapi.objectdataapi.adapterapi.IDataAdapter databaseAdapter) {
    return new PlanningPokerService(databaseAdapter);
  }

  @Override
  public IImage getApplicationLogo() {
    return APPLICATION_LOGO;
  }

  @Override
  public IDataAdapter createDataSupplier() {
    return DataAdapter.usingDatabaseAdapter(internalDatabaseAdapter.createEmptyCopy());
  }

  @Override
  public IRoomChangeNotifier getStoredRoomChangeNotifier() {
    return roomChangeNotifier;
  }
}
