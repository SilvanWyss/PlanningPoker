package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.BackendWebClientSession;

public final class InitialSession extends BackendWebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		
		final var userId = getRefParentClient().getCookieValueByCookieNameOrNull("userId");
		
		if (!knowsUserWithId(userId)) {
			setNext(new CreateUserSession());			
		} else {
			
			getRefParentClient().setSessionVariableWithKeyAndValue("userId", userId);
			
			setNext(new CreateRoomSession());
		}
	}
	
	private boolean knowsUserWithId(final String id) {
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		
		try (final var dataController = applicationController.createDataController()) {
			return dataController.containsUserWithId(id);
		} 
	}
}
