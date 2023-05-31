package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;

public final class CreateUserSessionHelper {
	
	public void createUserAndSetCookieAndRedirect(final String userName, final CreateUserSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			dataController.saveChanges();
			
			session.getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
			
			session.setNext(createNextSession(session, dataController));
		}
	}
	
	private PlanningPokerSession createNextSession(
		final CreateUserSession session,
		final IDataController dataController
	) {
		
		final var roomNumber = session.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("roomNumber");
		
		if (roomNumber != null) {
			
			final var room = dataController.getOriRoomByNumber(roomNumber);
			
			return RoomSession.withRoomId(room.getId());
		}
		
		return new CreateRoomSession();
	}
}
