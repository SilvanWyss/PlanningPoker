package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.control.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateRoomSession extends PageSession {
	
	public static CreateRoomSession withUserId(final String userId) {
		return new CreateRoomSession(userId);
	}
	
	private static final CreateRoomSessionHelper CREATE_ROOM_SESSION_HELPER = new CreateRoomSessionHelper();
	
	private CreateRoomSession(final String userId) {
		super(new SingleContainer<>(userId));
	}
	
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
	
	@Override
	protected void doRegistrations(final IRoomChangeNotifier roomChangeNotifier) {
		//Does nothing.
	}
	
	private void createRoomAndEnterRoomAndRedirect() {
		CREATE_ROOM_SESSION_HELPER.createRoomAndEnterRoomAndRedirect(this);
	}
	
	private void enterRoomAndRedirect() {
		
		final var roomNumber = roomNumberTextbox.getText();
		
		CREATE_ROOM_SESSION_HELPER.enterRoomAndRedirect(roomNumber, this);
	}
}
