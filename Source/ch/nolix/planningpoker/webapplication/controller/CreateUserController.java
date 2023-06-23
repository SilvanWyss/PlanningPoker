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
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		final var roomNumber =
		webClientSession.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("room");
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
			
			final var user = databaseAdapter.createUserWithName(userName);
			
			final var room = databaseAdapter.getOriRoomByNumberOrNull(roomNumber);
			if (room != null) {
				
				databaseAdapter.enterRoom(user, room);
				databaseAdapter.saveChanges();
				
				applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
			} else {
				databaseAdapter.saveChanges();
			}
			
			webClientSession.getOriParentClient().setOrAddCookieWithNameAndValue("user_id", user.getId());
			
			webClientSession.setNext(createNextSession(user, selectRoomSessionFactory, pokerSessionFactory));
		}
	}
	
	private WebClientSession<IPlanningPokerContext> createNextSession(
		final IUser user,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		if (user.isInARoom()) {
			
			final var room = user.getOriCurrentRoomVisit().getOriParentRoom();
			
			return pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(user.getId(), room.getId());
		}
		
		return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
	}
}
