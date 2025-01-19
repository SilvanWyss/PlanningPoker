package ch.nolix.planningpoker.webapplication.cardsetcomponent;

import java.util.Locale;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;

final class CardSetController extends Controller<IPlanningPokerService> {

  private final String roomVisitId;

  private final String roomId;

  public CardSetController(final String roomVisitId, final String roomId) {

    GlobalValidator.assertThat(roomVisitId).thatIsNamed("room visit id").isNotBlank();
    GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

    this.roomVisitId = roomVisitId;
    this.roomId = roomId;
  }

  public void deleteEstimateAndTrigger(final String roomVisitId) {

    final var applicationContext = getStoredApplicationContext();

    try (final var databaseAdapter = applicationContext.createDataSupplier()) {

      final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
      roomVisit.deleteEstimate();
      databaseAdapter.saveChanges();

      final var room = roomVisit.getStoredParentRoom();
      applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }

  public String getEstimateCardText(final double estimateInStoryPoints) {

    if (estimateInStoryPoints == 0.5) {
      return "0.5";
    }

    return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
  }

  public String getRoomId() {
    return roomId;
  }

  public String getRoomVisitId() {
    return roomVisitId;
  }

  public IRoomChangeNotifier getStoredRoomChangeNotifier() {
    return getStoredApplicationContext().getStoredRoomChangeNotifier();
  }

  public void setEstimateInStoryPointsAndTrigger(final String roomVisitId, final double estimateInStoryPoints) {

    final var applicationContext = getStoredApplicationContext();

    try (final var databaseAdapter = applicationContext.createDataSupplier()) {

      final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
      roomVisit.setEstimateInStoryPoints(estimateInStoryPoints);
      databaseAdapter.saveChanges();

      final var room = roomVisit.getStoredParentRoom();
      applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }

  public void setInfiniteEstimateAndTrigger(final String roomVisitId) {

    final var applicationContext = getStoredApplicationContext();

    try (final var databaseAdapter = applicationContext.createDataSupplier()) {

      final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
      roomVisit.setInfiniteEstimate();
      databaseAdapter.saveChanges();

      final var room = roomVisit.getStoredParentRoom();
      applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }
}
