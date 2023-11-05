package ch.nolix.planningpoker.webapplication.createusercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IUser;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class CreateUserController extends Controller<IPlanningPokerContext> {

  private final ISelectRoomSessionFactory selectRoomSessionFactory;

  private final IPokerSessionFactory pokerSessionFactory;

  public CreateUserController(
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    GlobalValidator.assertThat(selectRoomSessionFactory).thatIsNamed(ISelectRoomSessionFactory.class).isNotNull();
    GlobalValidator.assertThat(pokerSessionFactory).thatIsNamed(IPokerSessionFactory.class).isNotNull();

    this.selectRoomSessionFactory = selectRoomSessionFactory;
    this.pokerSessionFactory = pokerSessionFactory;
  }

  public void createUserAndSetCookieAndRedirect(final String userName) {

    final var applicationContext = getStoredApplicationContext();
    final var session = getStoredWebClientSession();

    final var roomNumber = session.getStoredParentClient().getUrlParameterValueByUrlParameterNameOrNull("room");

    try (final var databaseAdapter = applicationContext.createDataSupplier()) {

      final var user = databaseAdapter.createUserWithName(userName);

      final var room = databaseAdapter.getStoredRoomByNumberOrNull(roomNumber);
      if (room != null) {

        databaseAdapter.enterRoom(user, room);
        databaseAdapter.saveChanges();

        applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
      } else {
        databaseAdapter.saveChanges();
      }

      session.getStoredParentClient().setOrAddCookieWithNameAndValue("user_id", user.getId());

      session.setNext(createNextSession(user, selectRoomSessionFactory, pokerSessionFactory));
    }
  }

  private WebClientSession<IPlanningPokerContext> createNextSession(
    final IUser user,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    if (user.isInARoom()) {

      final var room = user.getStoredCurrentRoomVisit().getStoredParentRoom();

      return pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(user.getId(), room.getId());
    }

    return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
  }
}
