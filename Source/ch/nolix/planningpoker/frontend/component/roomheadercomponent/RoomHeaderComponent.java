package ch.nolix.planningpoker.frontend.component.roomheadercomponent;

import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.ComponentWithAdapterFactory;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.button.Button;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.labelapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomHeaderComponent
extends ComponentWithAdapterFactory<RoomHeaderController, IPlanningPokerService, IDataAdapter> {

  public RoomHeaderComponent(
    final String userId,
    final WebClientSession<IPlanningPokerService> session,
    final IDataAdapter initialDataAdapter,
    final ISelectRoomSessionFactory selectRoomSessionFactory) {
    super(new RoomHeaderController(userId, selectRoomSessionFactory), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(final RoomHeaderController controller, final IDataAdapter dataAdapter) {

    final var userId = controller.getUserId();
    final var user = dataAdapter.getStoredUserById(userId);
    final var roomVisit = user.getStoredCurrentRoomVisit();

    return createControl(roomVisit, controller);
  }

  private IControl<?, ?> createControl(final IRoomVisit roomVisit, final RoomHeaderController controller) {
    return new HorizontalStack()
      .setRole(ContainerRole.HEADER_CONTAINER)
      .addControl(
        new Label()
          .setRole(LabelRole.LEVEL1_HEADER)
          .setText("Room " + roomVisit.getStoredParentRoom().getNumber()),
        new Button()
          .setText("Show link to room")
          .setLeftMouseButtonPressAction(
            () -> controller.openShareRoomDialog(roomVisit.getStoredParentRoom())),
        new Button()
          .setText("Go to another room")
          .setLeftMouseButtonPressAction(controller::openGoToOtherRoomDialog));
  }
}
