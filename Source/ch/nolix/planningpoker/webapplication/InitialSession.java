package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitialSession extends WebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		setNext(createNextSession());
	}
	
	private WebClientSession<IApplicationContext> createNextSession() {
		try (final var dataController = getOriApplicationContext().createDataController()) {
			
			final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserByIdOrNull(userId);
			
			if (user != null) {
				return createNextSession(dataController, user);
			}
			
			return new CreateUserSession();
		}
	}
	
	private WebClientSession<IApplicationContext> createNextSession(
		final IDataController dataController,
		final IUser user
	) {
		
		final var roomNumber = getOriParentClient().getURLParameterValueByURLParameterNameOrNull("roomNumber");
		final var room = dataController.getOriRoomByNumberOrNull(roomNumber);
		
		if (room != null) {
			dataController.enterRoom(user, room);
			dataController.saveChanges();
		}
		
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
