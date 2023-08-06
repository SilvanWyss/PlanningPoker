package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class SelectRoomController {
	
	public void createAndEnterRoomAndRedirect(
		final String userId,
		final WebClientSession<IPlanningPokerContext> webClientSession,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var room = databaseAdapter.createNewRoomAndEnterRoom(user);
			databaseAdapter.saveChanges();
			
			webClientSession.setNext(
				pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(
					user.getId(),
					room.getId()
				)
			);
		}
	}
	
	public void enterRoomAndRedirect(
		final String userId,
		final String roomNumber,
		final WebClientSession<IPlanningPokerContext> webClientSession,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var room = databaseAdapter.getStoredRoomByNumber(roomNumber);
			databaseAdapter.enterRoom(user, room);
			databaseAdapter.saveChanges();
			
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
			
			webClientSession.setNext(pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(userId, room.getId()));
		}
	}
}
