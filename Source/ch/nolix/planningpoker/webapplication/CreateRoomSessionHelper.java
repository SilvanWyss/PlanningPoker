package ch.nolix.planningpoker.webapplication;

public final class CreateRoomSessionHelper {
	
	public void createRoomAndEnterRoomAndRedirect(final CreateRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserById(userId);
			dataController.createAndEnterNewRoom(user);
			dataController.saveChanges();
		}
		
		session.setNext(new PokerSession());
	}
	
	public void enterRoomAndRedirect(String roomNumber, final CreateRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserById(userId);
			final var room = dataController.getOriRoomByNumber(roomNumber);
			room.addVisitor(user);
			dataController.saveChanges();
			
			session.setNext(new PokerSession());
		}
	}
}
