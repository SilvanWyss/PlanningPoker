package ch.nolix.planningpoker.frontend.component.roommanagercomponent;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.component.Controller;
import ch.nolix.template.webgui.dialog.YesNoDialogBuilder;

final class RoomManagerController extends Controller<IPlanningPokerService> {

  private final String userId;

  public RoomManagerController(final String userId) {

    Validator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public String getCaptainInfoText(final IRoomVisit roomVisit) {

    final var roomCreator = roomVisit.getStoredParentRoom().getStoredParentCreator();
    final var user = roomVisit.getStoredVisitor();

    if (roomCreator.hasId(user.getId())) {
      return "You are the captain.";
    }

    return (roomCreator.getName() + " is our captain.");
  }

  public String getUserId() {
    return userId;
  }

  public boolean isAllowedToConfigureRoom(final IRoomVisit roomVisit) {

    final var visitor = roomVisit.getStoredVisitor();
    final var room = roomVisit.getStoredParentRoom();
    final var roomCreator = room.getStoredParentCreator();

    return visitor.hasId(roomCreator.getId());
  }

  public void openDeleteEstimatesDialog(final String roomId) {

    final var deleteEstimateDialog = new YesNoDialogBuilder()
      .setYesNoQuestion("Do you want to delete all estimates?")
      .setConfirmAction(() -> deleteEstimatesAndTrigger(roomId))
      .build();

    getStoredWebClientSession().getStoredGui().pushLayer(
      deleteEstimateDialog);
  }

  public void toggleEstimateVisibilityAndTrigger(final String roomId) {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var room = databaseAdapter.getStoredRoomById(roomId);

      if (room.hasSetEstimatesInvisible()) {
        room.setEstimatesVisible();
      } else {
        room.setEstimatesInvisible();
      }

      databaseAdapter.saveChanges();

      applicationService.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }

  private void deleteEstimatesAndTrigger(final String roomId) {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var room = databaseAdapter.getStoredRoomById(roomId);
      room.getStoredRoomVisits().forEach(IRoomVisit::deleteEstimate);
      room.setEstimatesInvisible();
      databaseAdapter.saveChanges();

      applicationService.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }
}
