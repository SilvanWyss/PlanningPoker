package ch.nolix.planningpoker.webapplication.estimateoverviewcomponent;

import java.util.Locale;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programatomapi.stringcatalogapi.StringCatalog;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.backendapi.examinerapi.IRoomVisitExaminer;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;
import ch.nolix.template.webgui.dialog.YesNoDialogBuilder;

final class EstimateOverviewController extends Controller<IPlanningPokerContext> {

  private static final IRoomVisitExaminer ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();

  private final String roomVisitId;

  private final String roomId;

  public EstimateOverviewController(final String roomVisitId, final String roomId) {

    GlobalValidator.assertThat(roomId).thatIsNamed("room visit id").isNotBlank();
    GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

    this.roomVisitId = roomVisitId;
    this.roomId = roomId;
  }

  public String getEstimateText(final IRoomVisit roomVisit) {

    if (roomVisit.getStoredParentRoom().hasSetEstimatesVisible()) {
      return getEstimateTextWhenEstimateIsVisible(roomVisit);
    }

    return getEstimateTextWhenEstimateIsInvisible(roomVisit);
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

  public void openKickUserDialog(final String userId) {

    final var applicationContext = getStoredApplicationContext();

    try (final var dataAdapter = applicationContext.createDataSupplier()) {

      final var user = dataAdapter.getStoredUserById(userId);

      final var kickUserDialog = new YesNoDialogBuilder()
        .setYesNoQuestion("Do you want to kick out " + user.getName() + " of the room?")
        .setConfirmAction(() -> kickUserAndTrigger(userId))
        .build();

      getStoredWebClientSession().getStoredGui().pushLayer(kickUserDialog);
    }
  }

  private String getEstimateTextWhenEstimateIsInvisible(final IRoomVisit roomVisit) {

    if (ROOM_VISIT_EVALUATOR.hasEstimate(roomVisit)) {
      return StringCatalog.QUESTION_MARK;
    }

    return StringCatalog.EMPTY_STRING;
  }

  private String getEstimateTextWhenEstimateIsVisible(final IRoomVisit roomVisit) {

    if (roomVisit.hasEstimateInStorypoints()) {
      return getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(roomVisit);
    }

    if (roomVisit.hasInfiniteEstimate()) {
      return StringCatalog.INFINITY;
    }

    return StringCatalog.EMPTY_STRING;
  }

  private String getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(
    final IRoomVisit roomVisit) {
    final var estimateInStoryPoints = roomVisit.getEstimateInStoryPoints();

    if (estimateInStoryPoints == 0.5) {
      return "0.5";
    }

    return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
  }

  private void kickUserAndTrigger(final String userId) {

    final var applicationContext = getStoredApplicationContext();

    try (final var dataAdapter = applicationContext.createDataSupplier()) {

      final var user = dataAdapter.getStoredUserById(userId);
      dataAdapter.leaveRoom(user);
      dataAdapter.saveChanges();

      getStoredApplicationContext().getStoredRoomChangeNotifier().noteRoomChange(roomId);
    }
  }
}
