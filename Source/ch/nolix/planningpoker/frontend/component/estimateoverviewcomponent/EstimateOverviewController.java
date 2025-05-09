package ch.nolix.planningpoker.frontend.component.estimateoverviewcomponent;

import java.util.Locale;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.coreapi.programatomapi.stringcatalogapi.StringCatalog;
import ch.nolix.planningpoker.backend.examiner.RoomVisitExaminer;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.backendapi.examinerapi.IRoomVisitExaminer;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;
import ch.nolix.template.webgui.dialog.YesNoDialogBuilder;

final class EstimateOverviewController extends Controller<IPlanningPokerService> {

  private static final IRoomVisitExaminer ROOM_VISIT_EVALUATOR = new RoomVisitExaminer();

  private final String roomVisitId;

  private final String roomId;

  public EstimateOverviewController(final String roomVisitId, final String roomId) {

    Validator.assertThat(roomId).thatIsNamed("room visit id").isNotBlank();
    Validator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

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
    return getStoredApplicationService().getStoredRoomChangeNotifier();
  }

  public void openKickUserDialog(final String userId) {

    final var applicationService = getStoredApplicationService();

    try (final var dataAdapter = applicationService.createAdapter()) {

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

    final var applicationService = getStoredApplicationService();

    try (final var dataAdapter = applicationService.createAdapter()) {

      final var user = dataAdapter.getStoredUserById(userId);
      dataAdapter.leaveRoom(user);
      dataAdapter.saveChanges();

      getStoredApplicationService().getStoredRoomChangeNotifier().noteRoomChange(roomId);
    }
  }
}
