package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class CreateUserSessionHelper {
	
	public void createUserAndSetCookieAndRedirect(
		final String userName,
		final WebClientSession<IApplicationContext> webClientSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		final var roomNumber =
		webClientSession.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("room");
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			
			final var room = dataController.getOriRoomByNumberOrNull(roomNumber);
			if (room != null) {
				
				dataController.enterRoom(user, room);
				dataController.saveChanges();
				
				applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
			} else {
				dataController.saveChanges();
			}
			
			webClientSession.getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
			
			webClientSession.setNext(createNextSession(user, selectRoomSessionFactory));
		}
	}
	
	private WebClientSession<IApplicationContext> createNextSession(
		final IUser user,
		final ISelectRoomSessionFactory selectRoomSessionFactory
	) {
		
		if (user.isInARoom()) {
			return
			PokerSession.withConfiguration(
				new PokerSessionConfiguration(
					user.getId(),
					user.getOriCurrentRoomVisit().getOriParentRoom().getId()
				)
			);
		}
		
		return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
	}
}
