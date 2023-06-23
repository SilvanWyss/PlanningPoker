package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpoker.webapplication.controller.InitializeController;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitialSession extends WebClientSession<IApplicationContext> {
	
	private static final InitializeController INITIALIZE_CONTROLLER = new InitializeController();
	
	@Override
	protected void initialize() {
		
		final var firstPageSession =
		INITIALIZE_CONTROLLER.createFirstPageSession(
			this,
			CreateUserSession::new,
			SelectRoomSession::withUserId,
			PokerSession::withUserIdAndRoomId
		);
		
		setNext(firstPageSession);
	}
}
