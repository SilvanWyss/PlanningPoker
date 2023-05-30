package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.control.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateRoomSession extends PlanningPokerSession {
	
	private static final CreateRoomSessionHelper CREATE_ROOM_SESSION_HELPER = new CreateRoomSessionHelper();
	
	private final Textbox roomNumberTextbox = new Textbox();
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		return
		new VerticalStack()
		.addControl(
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				roomNumberTextbox,
				new Button()
				.setText("Enter room")
				.setLeftMouseButtonPressAction(this::enterRoomAndRedirect)
			),
			new VerticalStack()
			.addControl(
				new Button()
				.setText("Create new room")
				.setLeftMouseButtonPressAction(this::createRoomAndEnterRoomAndRedirect)
			)
		);
	}
	
	private void createRoomAndEnterRoomAndRedirect() {
		CREATE_ROOM_SESSION_HELPER.createRoomAndEnterRoomAndRedirect(this);
	}
	
	private void enterRoomAndRedirect() {
		
		final var roomNumber = roomNumberTextbox.getText();
		
		CREATE_ROOM_SESSION_HELPER.enterRoomAndRedirect(roomNumber, this);
	}
}
