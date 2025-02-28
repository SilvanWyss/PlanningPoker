package ch.nolix.planningpoker.frontend.component.selectroomcomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.system.application.component.Controller;

final class SelectRoomController extends Controller<IPlanningPokerService> {

  private final String userId;

  public SelectRoomController(final String userId) {

    GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public void createAndEnterRoomAndRedirect(final IPokerSessionFactory pokerSessionFactory) {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var user = databaseAdapter.getStoredUserById(userId);
      databaseAdapter.createNewRoomAndEnterRoom(user);
      databaseAdapter.saveChanges();

      final var pokerSession = pokerSessionFactory.createPokerSessionWihtUserId(userId);
      getStoredWebClientSession().setNext(pokerSession);
    }
  }

  public void enterRoomAndRedirect(final String roomNumber, final IPokerSessionFactory pokerSessionFactory) {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var user = databaseAdapter.getStoredUserById(userId);
      final var room = databaseAdapter.getStoredRoomByNumber(roomNumber);
      databaseAdapter.enterRoom(user, room);
      databaseAdapter.saveChanges();

      applicationService.getStoredRoomChangeNotifier().noteRoomChange(room.getId());

      final var pokerSession = pokerSessionFactory.createPokerSessionWihtUserId(userId);
      getStoredWebClientSession().setNext(pokerSession);
    }
  }
}
