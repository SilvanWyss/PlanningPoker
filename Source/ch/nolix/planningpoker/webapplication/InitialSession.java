package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.BackendWebClientSession;

public final class InitialSession extends BackendWebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		setNext(createNextSession());
	}
	
	private BackendWebClientSession<IApplicationContext> createNextSession() {
				
		final var applicationController = getOriApplicationContext().createApplicationController();
		
		try (final var dataController = applicationController.createDataController()) {
			
			final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			
			if (dataController.containsUserWithId(userId)) {
				
				final var user = dataController.getOriUserById(userId);
				
				if (user.isInARoom()) {
					return RoomSession.withRoomId(user.getOriCurrentRoomVisit().getOriParentRoom().getId());
				}
				
				return new CreateRoomSession();
			}
			
			return new CreateUserSession();
		}
	}
}
