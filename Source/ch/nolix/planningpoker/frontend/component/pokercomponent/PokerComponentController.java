package ch.nolix.planningpoker.frontend.component.pokercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.component.Controller;

public final class PokerComponentController extends Controller<IPlanningPokerService> {

  private final String userId;

  public PokerComponentController(final String userId) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }
}
