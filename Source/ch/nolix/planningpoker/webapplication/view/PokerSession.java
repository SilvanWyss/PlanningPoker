package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;
import ch.nolix.planningpoker.webapplication.cardsetcomponent.CardSetComponent;
import ch.nolix.planningpoker.webapplication.estimateoverviewcomponent.EstimateOverviewComponent;
import ch.nolix.planningpoker.webapplication.roomanalysiscomponent.RoomAnalysisComponent;
import ch.nolix.planningpoker.webapplication.roomheadercomponent.RoomHeaderComponent;
import ch.nolix.planningpoker.webapplication.roommanagercomponent.RoomManagerComponent;
import ch.nolix.planningpoker.webapplication.userlinecomponent.UserLineComponent;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession implements ITriggerableSubscriber {

  private final String userId;

  private final String roomId;

  private PokerSession(final String userId, final String roomId) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();
    GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

    this.userId = userId;
    this.roomId = roomId;
  }

  public static PokerSession withUserIdAndRoomId(final String userId, final String roomId) {
    return new PokerSession(userId, roomId);
  }

  @Override
  public void trigger() {
    try (final var dataAdapter = getStoredApplicationContext().createDataAdapter()) {

      final var user = dataAdapter.getStoredUserById(userId);

      if (user.isInARoom()) {
        refreshIfDoesNotHaveOpenDialog();
      } else {

        final var selectRoomSession = SelectRoomSession.withUserId(userId);

        setNext(selectRoomSession);
      }
    }
  }

  @Override
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {

    getStoredApplicationContext().getStoredRoomChangeNotifier().registerRoomSubscriberIfNotRegistered(roomId, this);

    final var user = dataAdapter.getStoredUserById(userId);
    final var roomVisit = user.getStoredCurrentRoomVisit();

    return createMainControl(roomVisit, dataAdapter);
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter) {
    return new UserLineComponent(userId, this, dataAdapter).getStoredControl();
  }

  @Override
  protected void noteSelfChange() {
    getStoredApplicationContext().getStoredRoomChangeNotifier().noteRoomChange(roomId);
  }

  private IControl<?, ?> createMainControl(final IRoomVisit roomVisit, final IDataAdapter dataAdapter) {
    return new VerticalStack()
      .addControl(
        new RoomHeaderComponent(userId, this, dataAdapter, SelectRoomSession::withUserId).getStoredControl(),
        new RoomManagerComponent(userId, this, dataAdapter).getStoredControl(),
        new CardSetComponent(roomVisit.getId(), roomId, this, dataAdapter).getStoredControl(),
        new HorizontalStack()
          .addControl(
            new EstimateOverviewComponent(roomVisit.getId(), roomId, this, dataAdapter).getStoredControl(),
            new RoomAnalysisComponent(roomId, this, dataAdapter).getStoredControl()));
  }
}
