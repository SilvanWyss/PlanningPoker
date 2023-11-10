package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.planningpoker.webapplication.createusercomponent.CreateUserComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PageSession {

  @Override
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
    return new CreateUserComponent(
      this,
      SelectRoomSession::withUserId,
      PokerSession::withUserId)
      .getStoredControl();
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(final IDataAdapter dataAdapter) {
    return new Label();
  }
}
