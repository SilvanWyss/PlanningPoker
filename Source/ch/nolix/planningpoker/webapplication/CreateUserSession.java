package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.planningpoker.webapplication.controller.CreateUserController;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.control.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PageSession {
	
	private static final CreateUserController CREATE_USER_SESSION_HELPER = new CreateUserController();
	
	public CreateUserSession() {
		super(new SingleContainer<>());
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var userNameTextbox = new Textbox();
		
		return
		new VerticalStack()
		.addControl(
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				new Label()
				.setText("Enter your user name: "),
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
