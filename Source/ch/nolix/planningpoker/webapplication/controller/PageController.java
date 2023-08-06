package ch.nolix.planningpoker.webapplication.controller;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.template.webgui.dialog.EnterValueDialogBuilder;

public final class PageController {
	
	public String getUserName(final ISingleContainer<String> userIdContainer, final IDatabaseAdapter databaseAdapter) {
		
		if (userIdContainer.containsAny()) {
			
			final var userId = userIdContainer.getStoredElement();
			final var user = databaseAdapter.getStoredUserById(userId);
			
			return user.getName();
		}
		
		return StringCatalogue.MINUS;
	}
	
	public void openEditUserNameDialog(
		final String userId,
		final WebClientSession<IPlanningPokerContext> webClientSession
	) {
		
		final var applicationContext = webClientSession.getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
		
			final var user = databaseAdapter.getStoredUserById(userId);
			final var originUserName = user.getName();
			
			webClientSession
			.getStoredGui()
			.pushLayer(
				new EnterValueDialogBuilder()
				.setInfoText("Edit user name:")
				.setOriginalValue(originUserName)
				.setValueTaker(newUserName -> setUserName(userId, newUserName, webClientSession.getStoredApplicationContext()))
				.build()
			);
		}
	}
	
	private void setUserName(
		final String userId,
		final String newUserName,
		final IPlanningPokerContext planningPokerContext
	) {
		
		try (final var databaseAdapter = planningPokerContext.createDataAdapter()) {
			final var user = databaseAdapter.getStoredUserById(userId);
			user.setName(newUserName);
			databaseAdapter.saveChanges();
		}
	}
}
