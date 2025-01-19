package ch.nolix.planningpoker.webapplication.roommanagercomponent;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.application.component.ComponentWithDataSupplier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.button.Button;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomManagerComponent
extends ComponentWithDataSupplier<RoomManagerController, IPlanningPokerService, IDataAdapter> {

  public RoomManagerComponent(
    final String userId,
    final WebClientSession<IPlanningPokerService> session,
    final IDataAdapter initialDataAdapter) {
    super(new RoomManagerController(userId), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(final RoomManagerController controller, final IDataAdapter dataAdapter) {

    final var userId = controller.getUserId();
    final var user = dataAdapter.getStoredUserById(userId);
    final var roomVisit = user.getStoredCurrentRoomVisit();

    return createControl(roomVisit, controller);
  }

  private IControl<?, ?> createControl(final IRoomVisit roomVisit, final RoomManagerController controller) {
    return new HorizontalStack()
      .addControl(
        new Label()
          .setText(controller.getCaptainInfoText(roomVisit)),
        new Button()
          .setVisibility(controller.isAllowedToConfigureRoom(roomVisit))
          .setText("Show/hide estimates")
          .setLeftMouseButtonPressAction(
            () -> controller.toggleEstimateVisibilityAndTrigger(roomVisit.getStoredParentRoom().getId())),
        new Button()
          .setVisibility(controller.isAllowedToConfigureRoom(roomVisit))
          .setText("Delete estimates")
          .setLeftMouseButtonPressAction(
            () -> controller.openDeleteEstimatesDialog(roomVisit.getStoredParentRoom().getId())));
  }
}
