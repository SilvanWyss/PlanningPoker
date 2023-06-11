package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class SelectRoomSessionHelper {
	
	public void createAndEnterRoomAndRedirect(
		final String userId,
		final WebClientSession<IApplicationContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			final var room = dataController.createNewRoomAndEnterRoom(user);
			dataController.saveChanges();
			
			webClientSession.setNext(
				PokerSession.withConfiguration(new PokerSessionConfiguration(user.getId(), room.getId()))
			);
		}
	}
	
	public void enterRoomAndRedirect(
		final String userId,
		final String roomNumber,
		final WebClientSession<IApplicationContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			final var room = dataController.getOriRoomByNumber(roomNumber);
			dataController.enterRoom(user, room);
			dataController.saveChanges();
			
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
			
			webClientSession.setNext(
				PokerSession.withConfiguration(new PokerSessionConfiguration(user.getId(), room.getId()))
			);
		}
	}
}
