package ch.nolix.planningpoker.frontend.session;

import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IUser;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ICreateUserSessionFactory;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.webapplication.WebClientSession;

public final class InitializeController {

  public WebClientSession<IPlanningPokerService> createFirstPageSession(
    final WebClientSession<IPlanningPokerService> initialSession,
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

  private WebClientSession<IPlanningPokerService> createNextSession(
    final IUser user,
    final IDataAdapter dataAdapter,
    final WebClientSession<IPlanningPokerService> initialSession,
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
