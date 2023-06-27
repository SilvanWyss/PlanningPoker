package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpoker.webapplication.controller.SelectRoomController;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
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
	
	private static final SelectRoomController CREATE_ROOM_SESSION_HELPER = new SelectRoomController();
	
	private SelectRoomSession(final String userId) {
		super(new SingleContainer<>(userId));
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDatabaseAdapter databaseAdapter) {
		
		final var roomNumberTextbox = new Textbox();
		
		return
		new VerticalStack()
		.addControl(
			new VerticalStack()
			.addControl(
				new Button()
				.setText("Create new room")
				.setLeftMouseButtonPressAction(
					() ->
					CREATE_ROOM_SESSION_HELPER.createAndEnterRoomAndRedirect(
						getUserId(),
						this,
						PokerSession::withUserIdAndRoomId
					)
				)
			),
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				roomNumberTextbox,
				new Button()
				.setText("Enter room")
				.setLeftMouseButtonPressAction(
					() ->
					CREATE_ROOM_SESSION_HELPER.enterRoomAndRedirect(
						getUserId(),
						roomNumberTextbox.getText(),
						this,
						PokerSession::withUserIdAndRoomId
					)
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
