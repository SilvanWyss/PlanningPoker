package ch.nolix.planningpoker.webapplication;

import ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer;
import ch.nolix.coreapi.programcontrolapi.triggeruniversalapi.Refreshable;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IUser;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.dialog.EnterValueDialogFactory;

public final class PageSessionHelper {
	
	private static final EnterValueDialogFactory ENTER_VALUE_DIALOG_FACTORY = new EnterValueDialogFactory();
	
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
	
	public <WCS extends WebClientSession<IApplicationContext> & Refreshable> void openEditUserNameDialog(
		final String userId,
		final WCS refreshableWebClientSession
	) {
		
		final var applicationContext = refreshableWebClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
		
			final var user = dataController.getOriUserById(userId);
			final var originUserName = user.getName();
			
			refreshableWebClientSession
			.getOriGUI()
			.pushLayer(
				ENTER_VALUE_DIALOG_FACTORY.createEnterValueDialogWithTextAndOriginalValueAndValueTaker(
					"Edit your user name",
					originUserName,
					newUserName -> setUserNameAndRefresh(userId, newUserName, refreshableWebClientSession)
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
	
	private <WCS extends WebClientSession<IApplicationContext> & Refreshable> void setUserNameAndRefresh(
		final String userId,
		final String newUserName,
		final WCS refreshableWebClientSession
	) {
		
		final var applicationContext = refreshableWebClientSession.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.getOriUserById(userId);
			user.setName(newUserName);
			dataController.saveChanges();
			
			refreshableWebClientSession.refresh();
		}
	}
}
