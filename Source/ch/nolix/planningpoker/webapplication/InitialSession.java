package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.BackendWebClientSession;

public final class InitialSession extends BackendWebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		
		final var userId = getRefParentClient().getCookieValueByCookieNameOrNull("userId");
		
		if (userId == null) {
			setNext(new CreateUserSession());
		} else {
			//TODO: Create SelectRoomSession.
		}
	}
}
