package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.planningpokerapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ICreateUserSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeController {
	
	public WebClientSession<IPlanningPokerContext> createFirstPageSession(
		final WebClientSession<IPlanningPokerContext> initialSession,
		final ICreateUserSessionFactory createUserSessionFactory,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var applicationContext = initialSession.getOriApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
			
			final var userId = initialSession.getOriParentClient().getCookieValueByCookieNameOrNull("user_id");
			final var user = databaseAdapter.getOriUserByIdOrNull(userId);
			
			if (user != null) {
				return
				createNextSession(user, databaseAdapter, initialSession, selectRoomSessionFactory, pokerSessionFactory);
			}
		}
		
		return createUserSessionFactory.createCreateUserSession();
	}
	
	private WebClientSession<IPlanningPokerContext> createNextSession(
		final IUser user,
		final IDatabaseAdapter databaseAdapter,
		final WebClientSession<IPlanningPokerContext> initialSession,
		final ISelectRoomSessionFactory selectRoomSessionFactory,
		final IPokerSessionFactory pokerSessionFactory
	) {
		
		final var roomNumber = initialSession.getOriParentClient().getURLParameterValueByURLParameterNameOrNull("room");
		final var room = databaseAdapter.getOriRoomByNumberOrNull(roomNumber);
		
		if (room != null) {
			databaseAdapter.enterRoom(user, room);
			databaseAdapter.saveChanges();
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
