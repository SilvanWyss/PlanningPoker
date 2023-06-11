package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.dialog.EnterValueDialogFactory;

public final class PageSessionHelper {
	
	private static final EnterValueDialogFactory ENTER_VALUE_DIALOG_FACTORY = new EnterValueDialogFactory();
	
	public void editUserName(final IDataController dataController, PageSession session, final Label userLabel) {
		
		final var user = dataController.getOriUserById(session.getUserId());
		final var originUserName = user.getName();
		
		session
		.getOriGUI()
		.pushLayer(
			ENTER_VALUE_DIALOG_FACTORY.createEnterValueDialogWithTextAndOriginalValueAndValueTaker(
				"Edit your user name",
				originUserName,
				un -> setUserName(session, un, userLabel)
			)
		);
	}
	
	public String getUserLabelText(final IDataController dataController, final PageSession session) {
		
		if (session.hasUserId()) {
			
			final var userId = session.getUserId();
			final var user = dataController.getOriUserByIdOrNull(userId);
			
			return ("you: " + user.getName());
		}
		
		return "Welcome!";
	}
	
	private void setUserName(
		final PageSession session,
		final String newUserName,
		final Label userLabel
	) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var userId = session.getUserId();
			final var user = dataController.getOriUserById(userId);
			user.setName(newUserName);
			dataController.saveChanges();
			
			userLabel.setText(getUserLabelText(dataController, session));
		}
	}
}
