package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.coreapi.programatomapi.stringcatalogueapi.StringCatalogue;
import ch.nolix.planningpoker.webapplication.createusercomponent.CreateUserComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PageSession {

  @Override
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
    return new CreateUserComponent(
      this,
      SelectRoomSession::withUserId,
      PokerSession::withUserId);
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(final IDataAdapter dataAdapter) {
    return new Label().setText(StringCatalogue.EMPTY_STRING);
  }
}
