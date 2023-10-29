package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeSession extends WebClientSession<IPlanningPokerContext> {

  private static final InitializeController INITIALIZE_CONTROLLER = new InitializeController();

  @Override
  protected void initialize() {

    final var firstPageSession = INITIALIZE_CONTROLLER.createFirstPageSession(
      this,
      CreateUserSession::new,
      SelectRoomSession::withUserId,
      PokerSession::withUserIdAndRoomId);

    setNext(firstPageSession);
  }
}
