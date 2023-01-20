package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationController;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Text;
import ch.nolix.system.webgui.control.Textbox;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.controlapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.controlapi.TextRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserSession extends PlanningPokerSession {
	
	private final Textbox userNameTextbox = new Textbox();
	
	private final Text blankUserNameWarningLabel =
	new Text().setRole(TextRole.ERROR_LABEL).setText("The entered user name is blank.").setInvisible();
	
	@Override
	protected IControl<?, ?> createMainControl(
		final IApplicationController applicationController
	) {
		return
		new VerticalStack()
		.addControl(
			blankUserNameWarningLabel,
			new HorizontalStack()
			.addControl(
				new Text()
				.setText("Enter your user name: "),
				userNameTextbox,
				new Button()
				.setRole(ButtonRole.CONFIRM_BUTTON)
				.setText("Ok")
				.setLeftMouseButtonPressAction(this::createUserAndRedirect)
			)
		);
	}
	
	private void createUserAndRedirect() {
		
		final var userName = userNameTextbox.getText();
		
		if (userName.isBlank()) {
			blankUserNameWarningLabel.setVisible();
		} else {
			createUserAndRedirectWithUserName(userName);
		}
	}
	
	private void createUserAndRedirectWithUserName(final String userName) {
		
		final var applicationController = getRefApplicationContext().createApplicationController();
		final var dataController = applicationController.getRefDataController();
		final var user = dataController.createUserWithName(userName);
		dataController.saveChanges();
		
		getRefParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
		
		//TODO: Redirect.
	}
}
