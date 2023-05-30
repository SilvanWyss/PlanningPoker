package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.control.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PlanningPokerSession {
	
	private static final CreateUserSessionHelper CREATE_USER_CONTROLLER = new CreateUserSessionHelper();
	
	private final Textbox userNameTextbox = new Textbox();
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
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
				.setLeftMouseButtonPressAction(this::createUserAndCookieAndRedirect)
			)
		);
	}
	
	private void createUserAndCookieAndRedirect() {
		
		final var userName = userNameTextbox.getText();
		
		CREATE_USER_CONTROLLER.createUserAndAddCookieAndRedirect(userName, this);
	}
}
