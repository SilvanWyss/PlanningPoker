package ch.nolix.planningpoker.frontend.session;

import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeSession extends WebClientSession<IPlanningPokerService> {

  private static final InitializeController INITIALIZE_CONTROLLER = new InitializeController();

  @Override
  protected void initialize() {

    final var firstPageSession = INITIALIZE_CONTROLLER.createFirstPageSession(
      this,
      CreateUserSession::new,
      SelectRoomSession::withUserId,
      PokerSession::withUserId);

    setNext(firstPageSession);
  }
}
