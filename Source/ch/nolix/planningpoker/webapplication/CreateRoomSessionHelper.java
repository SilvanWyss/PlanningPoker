package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpoker.datamodel.User;

public final class CreateRoomSessionHelper {
	
	public void createRoomAndEnterRoomAndRedirect(final CreateRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserById(userId);
			
			dataController.createNewRoomAndEnterRoom(user);
			dataController.saveChanges();
		}
		
		session.setNext(new PokerSession());
	}
	
	public void enterRoomAndRedirect(String roomNumber, final CreateRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = (User)dataController.getOriUserById(userId);
			final var room = dataController.getOriRoomByNumber(roomNumber);
			
			dataController.enterRoom(user, room);
			dataController.saveChanges();
		}
		
		session.setNext(new PokerSession());
	}
}
