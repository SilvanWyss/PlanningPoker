package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.control.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateRoomSession extends PlanningPokerSession {
	
	private final Textbox roomIdentificationTextbox = new Textbox();
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		return
		new VerticalStack().addControl(
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				roomIdentificationTextbox,
				new Button()
				.setText("Enter room")
				.setLeftMouseButtonPressAction(this::enterRoom)
			),
			new VerticalStack()
			.addControl(
				new Button()
				.setText("Create new room")
				.setLeftMouseButtonPressAction(this::createAndEnterNewRoom)
			)
		);
	}
	
	private void createAndEnterNewRoom() {
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		
		try (final var dataController = applicationController.createDataController()) {
			
			final var user = dataController.getRefUserById(getUserId());
			dataController.createAndEnterNewRoom(user);
			dataController.saveChanges();
			
			//TODO: Redirect to RoomSession.
		}
	}
	
	private void enterRoom() {
		
		final var roomIdentification = roomIdentificationTextbox.getText();
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		try (final var dataController = applicationController.createDataController()) {
			
			final var room = dataController.getRefRoomByIdentification(roomIdentification);
			final var user = dataController.getRefUserById(getUserId());
			room.addVisitor(user);
			dataController.saveChanges();
			
			//TODO: Redirect to RoomSession.
		}
	}
}