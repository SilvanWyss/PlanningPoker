package ch.nolix.planningpoker.frontend.session;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpoker.frontend.component.pokercomponent.PokerComponent;
import ch.nolix.planningpoker.frontend.component.userlinecomponent.UserLineComponent;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession {

  private final String userId;

  private PokerSession(final String userId) {

    Validator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public static PokerSession withUserId(final String userId) {
    return new PokerSession(userId);
  }

  @Override
  protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
    return new PokerComponent(userId, dataAdapter, this, SelectRoomSession::withUserId);
  }

  @Override
  protected IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter) {
    return new UserLineComponent(userId, this, dataAdapter);
  }
}
