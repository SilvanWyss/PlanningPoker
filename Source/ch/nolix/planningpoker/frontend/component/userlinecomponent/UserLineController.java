package ch.nolix.planningpoker.frontend.component.userlinecomponent;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.system.application.component.Controller;
import ch.nolix.template.webgui.dialog.EnterValueDialogBuilder;

final class UserLineController extends Controller<IPlanningPokerService> {

  private final String userId;

  public UserLineController(final String userId) {

    Validator.assertThat(userId).thatIsNamed("user id").isNotBlank();

    this.userId = userId;
  }

  public String getLoggedInUserName(final IDataAdapter dataAdapter) {

    final var user = dataAdapter.getStoredUserById(userId);

    return user.getName();
  }

  public void openEditUserNameDialog() {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var user = databaseAdapter.getStoredUserById(userId);
      final var originUserName = user.getName();

      getStoredWebClientSession()
        .getStoredGui()
        .pushLayer(
          new EnterValueDialogBuilder()
            .setInfoText("Edit your user name.")
            .setOriginalValue(originUserName)
            .setValueTaker(this::setUserName)
            .build());
    }
  }

  private void setUserName(final String newUserName) {

    final var applicationService = getStoredApplicationService();

    try (final var databaseAdapter = applicationService.createAdapter()) {

      final var user = databaseAdapter.getStoredUserById(userId);
      user.setName(newUserName);
      databaseAdapter.saveChanges();

      if (user.isInARoom()) {
        applicationService
          .getStoredRoomChangeNotifier()
          .noteRoomChange(user.getStoredCurrentRoomVisit().getStoredParentRoom().getId());
      }
    }
  }
}
