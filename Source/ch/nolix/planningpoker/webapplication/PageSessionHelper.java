package ch.nolix.planningpoker.webapplication;

import ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.element.style.DeepStyle;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.dialog.EnterValueDialogFactory;
import ch.nolix.systemapi.elementapi.styleapi.IStyle;
import ch.nolix.template.webgui.style.DarkModeStyleCreator;

public final class PageSessionHelper {
	
	private static final EnterValueDialogFactory ENTER_VALUE_DIALOG_FACTORY = new EnterValueDialogFactory();
	
	private static final DarkModeStyleCreator DARK_MODE_STYLE_CREATOR = new DarkModeStyleCreator();
	
	public IStyle createStyle() {
		return
		DARK_MODE_STYLE_CREATOR
		.createDarkModeStyle()
		.addConfiguration(
			new DeepStyle()
			.addSelectorToken("card")
			.addAttachingAttribute(
				"BaseWidth(63)",
				"BaseHeight(88)",
				"BaseBackground(Color(Beige))",
				"HoverBackground(Color(Black))",
				"FocusBackground(Color(Black))",
				"BaseTextSize(30)",
				"BaseTextColor(Black)",
				"HoverTextColor(White)",
				"FocusTextColor(White)"
			)
		)
		.addConfiguration(
			new DeepStyle()
			.addSelectorToken("activated_card")
			.addAttachingAttribute(
				"BaseBackground(Color(Black))",
				"HoverBackground(Color(Black))",
				"FocusBackground(Color(Black))",
				"BaseTextColor(White)"
			)
		);
	}
	
	public String getUserLabelText(
		final ISingleContainer<String> userIdContainer,
		final IDataController dataController
	) {
		
		if (userIdContainer.containsAny()) {
			
			final var userId = userIdContainer.getOriElement();
			final var user = dataController.getOriUserById(userId);
			
			return getUserLabelText(user);
		}
		
		return "Welcome!";
	}
	
	public void openEditUserNameDialog(
		final String userId,
		final WebClientSession<IApplicationContext> webClientSession,
		final Label userLabel
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
		
			final var user = dataController.getOriUserById(userId);
			final var originUserName = user.getName();
			
			webClientSession
			.getOriGUI()
			.pushLayer(
				ENTER_VALUE_DIALOG_FACTORY.createEnterValueDialogWithTextAndOriginalValueAndValueTaker(
					"Edit your user name",
					originUserName,
					newUserName -> setUserNameAndRefresh(userId, newUserName, webClientSession, userLabel)
				)
			);
		}
	}
	
	private String getUserLabelText(final IUser user) {
		
		final var userName = user.getName();
		
		return getUserLabelText(userName);
	}
	
	private String getUserLabelText(final String userName) {
		return ("you: " + userName);
	}
	
	private void setUserNameAndRefresh(
		final String userId,
		final String newUserName,
		final WebClientSession<IApplicationContext> webClientSession,
		final Label userLabel
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			user.setName(newUserName);
			dataController.saveChanges();
			
			userLabel.setText(getUserLabelText(user));
		}
	}
}
