package ch.nolix.planningpoker.webapplication;

public final class SelectRoomSessionHelper {
	
	public void createRoomAndEnterRoomAndRedirect(final SelectRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getUserId();
			final var user = dataController.getOriUserById(userId);
			final var room = dataController.createNewRoomAndEnterRoom(user);
			dataController.saveChanges();
			
			session.setNext(PokerSession.withConfiguration(new PokerSessionConfiguration(user.getId(), room.getId())));
		}
	}
	
	public void enterRoomAndRedirect(String roomNumber, final SelectRoomSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserById(userId);
			final var room = dataController.getOriRoomByNumber(roomNumber);
			dataController.enterRoom(user, room);
			dataController.saveChanges();
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
			
			session.setNext(PokerSession.withConfiguration(new PokerSessionConfiguration(user.getId(), room.getId())));
		}
	}
}
