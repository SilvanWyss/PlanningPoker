package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.datamodelapi.IUser;

public final class CreateUserSessionHelper {
	
	public void createUserAndSetCookieAndRedirect(final String userName, final CreateUserSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			final var roomNumber = session.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("roomNumber");
			final var room = dataController.getOriRoomByNumberOrNull(roomNumber);
			if (room != null) {
				dataController.enterRoom(user, room);
			}
			dataController.saveChanges();
			
			session.getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
			
			session.setNext(createNextSession(user));
		}
	}
	
	private PageSession createNextSession(final IUser user) {
		
		if (user.isInARoom()) {
			return new PokerSession();
		}
		
		return new CreateRoomSession();
	}
}
