package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class CreateUserSessionHelper {
	
	public void createUserAndSetCookieAndRedirect(
		final String userName,
		final WebClientSession<IApplicationContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		final var roomNumber =
		webClientSession.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("roomNumber");
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			
			final var room = dataController.getOriRoomByNumberOrNull(roomNumber);
			if (room != null) {
				dataController.enterRoom(user, room);
			}
			dataController.saveChanges();
			
			webClientSession.getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
			
			webClientSession.setNext(createNextSession(user));
		}
	}
	
	private PageSession createNextSession(final IUser user) {
		
		if (user.isInARoom()) {
			return
			PokerSession.withConfiguration(
				new PokerSessionConfiguration(
					user.getId(),
					user.getOriCurrentRoomVisit().getOriParentRoom().getId()
				)
			);
		}
		
		return SelectRoomSession.withUserId(user.getId());
	}
}
