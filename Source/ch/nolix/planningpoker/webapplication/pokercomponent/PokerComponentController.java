package ch.nolix.planningpoker.webapplication.pokercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.Controller;

public final class PokerComponentController extends Controller<IPlanningPokerContext> {

  private final String userId;

  public PokerComponentController(final String userId) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }
}
