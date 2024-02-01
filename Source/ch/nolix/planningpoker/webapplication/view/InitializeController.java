package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ICreateUserSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeController {

  public WebClientSession<IPlanningPokerContext> createFirstPageSession(
    final WebClientSession<IPlanningPokerContext> initialSession,
    final ICreateUserSessionFactory createUserSessionFactory,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    final var applicationContext = initialSession.getStoredApplicationContext();

    try (final var databaseAdapter = applicationContext.createDataSupplier()) {

      final var userId = initialSession.getStoredParentClient().getOptionalCookieValueByCookieName("user_id");

      if (userId.isPresent()) {

        final var user = databaseAdapter.getStoredUserByIdOrNull(userId.get());

        if (user != null) {
          return createNextSession(user, databaseAdapter, initialSession, selectRoomSessionFactory,
            pokerSessionFactory);
        }
      }
    }

    return createUserSessionFactory.createCreateUserSession();
  }

  private WebClientSession<IPlanningPokerContext> createNextSession(
    final IUser user,
    final IDataAdapter dataAdapter,
    final WebClientSession<IPlanningPokerContext> initialSession,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    final var roomNumber = //
    initialSession.getStoredParentClient().getOptionalUrlParameterValueByUrlParameterName("room");

    if (roomNumber.isPresent()) {

      final var room = dataAdapter.getStoredRoomByNumberOrNull(roomNumber.get());

      if (room != null) {
        dataAdapter.enterRoom(user, room);
        dataAdapter.saveChanges();
      }
    }

    if (user.isInARoom()) {
      return pokerSessionFactory.createPokerSessionWihtUserId(user.getId());
    }

    return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
  }
}
