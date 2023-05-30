package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.BackendWebClientSession;

public final class InitialSession extends BackendWebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		setNext(createNextSession());
	}
	
	private BackendWebClientSession<IApplicationContext> createNextSession() {
		try (final var dataController = getOriApplicationContext().createDataController()) {
			
			final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			
			if (dataController.containsUserWithId(userId)) {
				
				final var user = dataController.getOriUserById(userId);
				
				if (user.isInARoom()) {
					return RoomSession.withRoomId(user.getOriCurrentRoomVisit().getOriParentRoom().getId());
				}
				
				final var roomNumber = getOriParentClient().getCookieValueByCookieNameOrNull("roomNumber");
				
				if (roomNumber != null) {
					
					final var room = dataController.getOriRoomByNumber(roomNumber);
					
					return RoomSession.withRoomId(room.getId());
				}
				
				return new CreateRoomSession();
			}
			
			return new CreateUserSession();
		}
	}
}
