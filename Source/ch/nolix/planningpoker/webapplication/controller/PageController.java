package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.dialog.EnterValueDialogFactory;

public final class PageController {
	
	private static final EnterValueDialogFactory ENTER_VALUE_DIALOG_FACTORY = new EnterValueDialogFactory();
	
	public String getUserName(final ISingleContainer<String> userIdContainer, final IDataController dataController) {
		
		if (userIdContainer.containsAny()) {
			
			final var userId = userIdContainer.getOriElement();
			final var user = dataController.getOriUserById(userId);
			
			return user.getName();
		}
		
		return StringCatalogue.MINUS;
	}
	
	public void openEditUserNameDialog(
		final String userId,
		final WebClientSession<IApplicationContext> webClientSession
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
					newUserName -> setUserName(userId, newUserName, webClientSession.getOriApplicationContext())
				)
			);
		}
	}
	
	private void setUserName(
		final String userId,
		final String newUserName,
		final IApplicationContext applicationContext
	) {
		
		try (final var dataController = applicationContext.createDataController()) {
			final var user = dataController.getOriUserById(userId);
			user.setName(newUserName);
			dataController.saveChanges();
		}
	}
}
