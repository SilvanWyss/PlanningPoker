package ch.nolix.planningpoker.webapplication.roomheadercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.Controller;
import ch.nolix.template.webgui.dialog.ShowValueDialogBuilder;
import ch.nolix.template.webgui.dialog.YesNoDialogBuilder;

final class RoomHeaderController extends Controller<IPlanningPokerContext> {

  private static final RoomHyperlinkCreator ROOM_HYPERLINK_CREATOR = new RoomHyperlinkCreator();

  private final String userId;

  private final ISelectRoomSessionFactory selectRoomSessionFactory;

  public RoomHeaderController(final String userId, final ISelectRoomSessionFactory selectRoomSessionFactory) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();
    GlobalValidator.assertThat(selectRoomSessionFactory).thatIsNamed(ISelectRoomSessionFactory.class).isNotNull();

    this.userId = userId;
    this.selectRoomSessionFactory = selectRoomSessionFactory;
  }

  public String getUserId() {
    return userId;
  }

  public void openGoToOtherRoomDialog() {

    final var goToOtherRoomDialog = new YesNoDialogBuilder()
      .setYesNoQuestion("Do you want to leave the current room?")
      .setConfirmAction(this::goToOtherRoomAndTrigger)
      .build();

    getStoredWebClientSession().getStoredGui().pushLayer(goToOtherRoomDialog);
  }

  public void openShareRoomDialog(final IRoom room) {

    final var application = getStoredWebClientSession().getStoredParentClient().getStoredParentApplication();

    final var roomHyperlink = ROOM_HYPERLINK_CREATOR.createHyperlinkToRoom(room, application);

    final var shareRoomDialog = new ShowValueDialogBuilder()
      .setValueName("Link to room")
      .setValue(roomHyperlink)
      .setValueCopier(v -> getStoredWebClientSession().getStoredGui().onFrontEnd().writeTextToClipboard(v))
      .build();

    getStoredWebClientSession().getStoredGui().pushLayer(shareRoomDialog);
  }

  private void goToOtherRoomAndTrigger() {

    final var applicationContext = getStoredApplicationContext();

    try (final var databaseAdapter = applicationContext.createDataAdapter()) {

      final var user = databaseAdapter.getStoredUserById(userId);
      final var room = user.getStoredCurrentRoomVisit().getStoredParentRoom();
      databaseAdapter.leaveRoom(user);

      databaseAdapter.saveChanges();

      getStoredWebClientSession().setNext(selectRoomSessionFactory.createSelectRoomSessionWihtUserId(userId));

      applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
    }
  }
}
