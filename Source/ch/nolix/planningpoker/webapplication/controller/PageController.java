package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.dialog.EnterValueDialogFactory;

public final class PageController {
	
	private static final EnterValueDialogFactory ENTER_VALUE_DIALOG_FACTORY = new EnterValueDialogFactory();
	
	public String getUserName(final ISingleContainer<String> userIdContainer, final IDatabaseAdapter databaseAdapter) {
		
		if (userIdContainer.containsAny()) {
			
			final var userId = userIdContainer.getOriElement();
			final var user = databaseAdapter.getOriUserById(userId);
			
			return user.getName();
		}
		
		return StringCatalogue.MINUS;
	}
	
	public void openEditUserNameDialog(
		final String userId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getOriApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDatabaseAdapter()) {
		
			final var user = databaseAdapter.getOriUserById(userId);
			final var originUserName = user.getName();
			
			webClientSession
			.getOriGui()
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
		final IPlanningPokerContext planningPokerContext
	) {
		
		try (final var databaseAdapter = planningPokerContext.createDatabaseAdapter()) {
			final var user = databaseAdapter.getOriUserById(userId);
			user.setName(newUserName);
			databaseAdapter.saveChanges();
		}
	}
}
