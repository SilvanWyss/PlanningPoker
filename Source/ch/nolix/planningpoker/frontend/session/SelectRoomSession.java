package ch.nolix.planningpoker.frontend.session;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpoker.frontend.component.selectroomcomponent.SelectRoomComponent;
import ch.nolix.planningpoker.frontend.component.userlinecomponent.UserLineComponent;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class SelectRoomSession extends PageSession {

  private final String userId;

  private SelectRoomSession(final String userId) {

    Validator.assertThat(userId).thatIsNamed("user id").isNotBlank();

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
