package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
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
				.setLeftMouseButtonPressAction(this::createUserAndRedirect)
			)
		);
	}
	
	private void createUserAndRedirect() {
		
		final var userName = userNameTextbox.getText();
		
		createUserAndRedirectWithUserName(userName);
	}
	
	private void createUserAndRedirectWithUserName(final String userName) {
		
		GlobalValidator.assertThat(userName).thatIsNamed("user name").isNotBlank();
		
		final var applicationController = getOriApplicationContext().createApplicationController();
		
		try (final var dataController = applicationController.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			dataController.saveChanges();
			getOriParentClient().setSessionVariableWithKeyAndValue("userId", user.getId());
			getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
		
			setNext(new CreateRoomSession());
		}
	}
}
