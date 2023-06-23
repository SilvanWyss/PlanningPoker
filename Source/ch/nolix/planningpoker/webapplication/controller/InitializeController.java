package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ICreateUserSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeController {
	
	public WebClientSession<IApplicationContext> createFirstPageSession(
		final WebClientSession<IApplicationContext> initialSession,
		final ICreateUserSessionFactory createUserSessionFactory,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var applicationContext = initialSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = initialSession.getOriParentClient().getCookieValueByCookieNameOrNull("user_id");
			final var user = dataController.getOriUserByIdOrNull(userId);
			
			if (user != null) {
				return
				createNextSession(user, dataController, initialSession, selectRoomSessionFactory, pokerSessionFactory);
			}
		}
		
		return createUserSessionFactory.createCreateUserSession();
	}
	
	private WebClientSession<IApplicationContext> createNextSession(
		final IUser user,
		final IDataController dataController,
		final WebClientSession<IApplicationContext> initialSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var roomNumber = initialSession.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("room");
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
		
		return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
	}
}
