package ch.nolix.planningpoker.frontend.component.createusercomponent;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IUser;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class CreateUserController extends Controller<IPlanningPokerService> {

  private final ISelectRoomSessionFactory selectRoomSessionFactory;

  private final IPokerSessionFactory pokerSessionFactory;

  public CreateUserController(
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    Validator.assertThat(selectRoomSessionFactory).thatIsNamed(ISelectRoomSessionFactory.class).isNotNull();
    Validator.assertThat(pokerSessionFactory).thatIsNamed(IPokerSessionFactory.class).isNotNull();

    this.selectRoomSessionFactory = selectRoomSessionFactory;
    this.pokerSessionFactory = pokerSessionFactory;
  }

  public void createUserAndSetCookieAndRedirect(final String userName) {

    final var applicationService = getStoredApplicationService();
    final var session = getStoredWebClientSession();

    final var roomNumber = session.getStoredParentClient().getOptionalUrlParameterValueByUrlParameterName("room");

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var user = databaseAdapter.createUserWithName(userName);

      if (roomNumber.isPresent()) {

        final var room = databaseAdapter.getStoredRoomByNumberOrNull(roomNumber.get());

        if (room != null) {
          databaseAdapter.enterRoom(user, room);
          databaseAdapter.saveChanges();
          applicationService.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
        }
      } else {
        databaseAdapter.saveChanges();
      }

      session.getStoredParentClient().setOrAddCookieWithNameAndValue("user_id", user.getId());

      session.setNext(createNextSession(user, selectRoomSessionFactory, pokerSessionFactory));
    }
  }

  private WebClientSession<IPlanningPokerService> createNextSession(
    final IUser user,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {

    if (user.isInARoom()) {
      return pokerSessionFactory.createPokerSessionWihtUserId(user.getId());
    }

    return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(user.getId());
  }
}
