package ch.nolix.planningpoker.webapplication.roomheadercomponent;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.ComponentWithDataAdapter;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomHeaderComponent
extends ComponentWithDataAdapter<RoomHeaderController, IPlanningPokerContext, IDataAdapter> {
	
	public RoomHeaderComponent(
		final String userId,
		final WebClientSession<IPlanningPokerContext> session,
		final IDataAdapter initialDataAdapter,
		final ISelectRoomSessionFactory selectRoomSessionFactory
	) {
		super(new RoomHeaderController(userId, session, selectRoomSessionFactory), initialDataAdapter);
	}
	
	@Override
	protected IControl<?, ?> createControl(final RoomHeaderController controller, final IDataAdapter dataAdapter) {
		
		final var userId = controller.getUserId();
		final var user = dataAdapter.getStoredUserById(userId);
		final var roomVisit = user.getStoredCurrentRoomVisit();
		
		return createControl(roomVisit, controller);
	}
	
	private IControl<?, ?> createControl(final IRoomVisit roomVisit, final RoomHeaderController controller) {
		return
		new HorizontalStack()
		.setRole(ContainerRole.HEADER_CONTAINER)
		.addControl(
			new Label()
			.setRole(LabelRole.LEVEL1_HEADER)
			.setText("Room " + roomVisit.getStoredParentRoom().getNumber()),
			new Button()
			.setText("Show link to room")
			.setLeftMouseButtonPressAction(
				() -> controller.openShareRoomDialog(roomVisit.getStoredParentRoom())
			),
			new Button()
			.setText("Go to another room")
			.setLeftMouseButtonPressAction(controller::openGoToOtherRoomDialog)
		);
	}
	
	@Override
	protected void doRegistrations(final RoomHeaderController controller) {
		//Do nothing.
	}
}
