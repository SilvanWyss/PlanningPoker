package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpoker.webapplication.controller.CreateUserController;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.atomiccontrol.Textbox;
import ch.nolix.system.webgui.atomiccontrol.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PageSession {
	
	private static final CreateUserController CREATE_USER_SESSION_HELPER = new CreateUserController();
	
	public CreateUserSession() {
		super(new SingleContainer<>());
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDatabaseAdapter databaseAdapter) {
		
		final var userNameTextbox = new Textbox();
		
		return
		new VerticalStack()
		.addControl(
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				new Label()
				.setText("Enter user name:"),
				userNameTextbox,
				new Button()
				.setRole(ButtonRole.CONFIRM_BUTTON)
				.setText("Ok")
				.setLeftMouseButtonPressAction(
					() ->
					CREATE_USER_SESSION_HELPER.createUserAndSetCookieAndRedirect(
						userNameTextbox.getText(),
						this,
						SelectRoomSession::withUserId,
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
