package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ICreateUserSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitialSession extends WebClientSession<IApplicationContext> {
	
	@Override
	protected void initialize() {
		setNext(createNextSession(CreateUserSession::new, PokerSession::withUserIdAndRoomId));
	}
	
	private WebClientSession<IApplicationContext> createNextSession(
		final ICreateUserSessionFactory createUserSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		try (final var dataController = getOriApplicationContext().createDataController()) {
			
			final var userId = getOriParentClient().getCookieValueByCookieNameOrNull("userId");
			final var user = dataController.getOriUserByIdOrNull(userId);
			
			if (user != null) {
				return createNextSession(user, dataController, pokerSessionFactory);
			}
		}
		
		return createUserSessionFactory.createCreateUserSession();
	}
	
	private WebClientSession<IApplicationContext> createNextSession(
		final IUser user,
		final IDataController dataController,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var roomNumber = getOriParentClient().getURLParameterValueByURLParameterNameOrNull("room");
		final var room = dataController.getOriRoomByNumberOrNull(roomNumber);
		
		if (room != null) {
			dataController.enterRoom(user, room);
			dataController.saveChanges();
		}
		
		if (user.isInARoom()) {
			return
			pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(
				user.getId(),
				user.getOriCurrentRoomVisit().getOriParentRoom().getId()
			);
		}
		
		return SelectRoomSession.withUserId(user.getId());
	}
}
