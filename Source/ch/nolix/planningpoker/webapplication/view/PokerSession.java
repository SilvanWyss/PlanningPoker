package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.webapplication.pokercomponent.PokerComponent;
import ch.nolix.planningpoker.webapplication.userlinecomponent.UserLineComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession {

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
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
    return new PokerComponent(userId, dataAdapter, this, SelectRoomSession::withUserId).getStoredControl();
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter) {
    return new UserLineComponent(userId, this, dataAdapter).getStoredControl();
  }

  @Override
  protected void noteSelfChange() {
    getStoredApplicationContext().getStoredRoomChangeNotifier().noteRoomChange(roomId);
  }
}
