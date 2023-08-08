package ch.nolix.planningpoker.webapplication.roommanagercomponent;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataAdapter;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomManagerComponent
extends ComponentWithDataAdapter<RoomManagerController, IPlanningPokerContext, IDataAdapter> {
	
	public RoomManagerComponent(
		final String userId,
		final WebClientSession<IPlanningPokerContext> session,
		final IDataAdapter initialDataAdapter
	) {
		super(new RoomManagerController(userId, session), initialDataAdapter);
	}
	
	@Override
	protected IControl<?, ?> createControl(final RoomManagerController controller, final IDataAdapter dataAdapter) {
		
		final var userId = controller.getUserId();
		final var user = dataAdapter.getStoredUserById(userId);
		final var roomVisit = user.getStoredCurrentRoomVisit();
		
		return createControl(roomVisit, controller);
	}
	
	private IControl<?, ?> createControl(final IRoomVisit roomVisit, final RoomManagerController controller) {
		return
		new HorizontalStack()
		.addControl(
			new Label()
			.setText(controller.getCaptainInfoText(roomVisit)),
			new Button()
			.setVisibility(controller.isAllowedToConfigureRoom(roomVisit))
			.setText("Show/hide estimates")
			.setLeftMouseButtonPressAction(
				() -> controller.toggleEstimateVisibilityAndTrigger(roomVisit.getStoredParentRoom().getId())
			),
			new Button()
			.setVisibility(controller.isAllowedToConfigureRoom(roomVisit))
			.setText("Delete estimates")
			.setLeftMouseButtonPressAction(
				() -> controller.openDeleteEstimatesDialog(roomVisit.getStoredParentRoom().getId())
			)
		);
	}
	
	@Override
	protected void doRegistrations(final RoomManagerController controller) {
		//Does nothing.
	}
}
