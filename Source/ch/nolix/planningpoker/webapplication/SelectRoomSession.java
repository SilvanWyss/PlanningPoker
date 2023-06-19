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

public final class SelectRoomSession extends PageSession {
	
	public static SelectRoomSession withUserId(final String userId) {
		return new SelectRoomSession(userId);
	}
	
	private static final SelectRoomSessionHelper CREATE_ROOM_SESSION_HELPER = new SelectRoomSessionHelper();
	
	private SelectRoomSession(final String userId) {
		super(new SingleContainer<>(userId));
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var roomNumberTextbox = new Textbox();
		
		return
		new VerticalStack()
		.addControl(
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				roomNumberTextbox,
				new Button()
				.setText("Enter room")
				.setLeftMouseButtonPressAction(
					() ->
					CREATE_ROOM_SESSION_HELPER.enterRoomAndRedirect(getUserId(), roomNumberTextbox.getText(), this)
				)
			),
			new VerticalStack()
			.addControl(
				new Button()
				.setText("Create new room")
				.setLeftMouseButtonPressAction(
					() -> CREATE_ROOM_SESSION_HELPER.createAndEnterRoomAndRedirect(getUserId(), this)
				)
			)
		);
	}
	
	@Override
	protected void doRegistrations(final IRoomChangeNotifier roomChangeNotifier) {
		//Does nothing.
	}
	
	@Override
	protected void noteSelfChange() {
		refreshIfDoesNotHaveOpenDialog();
	}
}
