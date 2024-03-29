package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.webapplication.selectroomcomponent.SelectRoomComponent;
import ch.nolix.planningpoker.webapplication.userlinecomponent.UserLineComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class SelectRoomSession extends PageSession {

  private final String userId;

  private SelectRoomSession(final String userId) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public static SelectRoomSession withUserId(final String userId) {
    return new SelectRoomSession(userId);
  }

  @Override
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
    return new SelectRoomComponent(userId, this);
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter) {
    return new UserLineComponent(userId, this, dataAdapter);
  }
}
