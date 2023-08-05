package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class CreateUserController {
	
	public void createUserAndSetCookieAndRedirect(
		final String userName,
		final WebClientSession<IPlanningPokerContext> webClientSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		final var roomNumber =
		webClientSession.getStoredParentClient().getUrlParameterValueByUrlParameterNameOrNull("room");
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
			
			final var user = databaseAdapter.createUserWithName(userName);
			
			final var room = databaseAdapter.getStoredRoomByNumberOrNull(roomNumber);
			if (room != null) {
				
				databaseAdapter.enterRoom(user, room);
				databaseAdapter.saveChanges();
				
				applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
			} else {
				databaseAdapter.saveChanges();
			}
			
			webClientSession.getStoredParentClient().setOrAddCookieWithNameAndValue("user_id", user.getId());
			
			webClientSession.setNext(createNextSession(user, selectRoomSessionFactory, pokerSessionFactory));
		}
	}
	
	private WebClientSession<IPlanningPokerContext> createNextSession(
		final IUser user,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		if (user.isInARoom()) {
			
			final var room = user.getStoredCurrentRoomVisit().getStoredParentRoom();
			
			return pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(user.getId(), room.getId());
		}
		
		return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
	}
}
