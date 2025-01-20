package ch.nolix.planningpoker.frontend.session;

import ch.nolix.coreapi.programatomapi.stringcatalogapi.StringCatalog;
import ch.nolix.planningpoker.frontend.component.createusercomponent.CreateUserComponent;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
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
    return new Label().setText(StringCatalog.EMPTY_STRING);
  }
}
