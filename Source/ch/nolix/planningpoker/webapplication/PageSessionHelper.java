package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;

public final class PageSessionHelper {
	
	public String getUserLabelText(final IDataController dataController, final PageSession session) {
		
		if (session.hasUserId()) {
			
			final var userId = session.getUserId();
			final var user = dataController.getOriUserByIdOrNull(userId);
			
			return ("you: " + user.getName());
		}
		
		return "Welcome!";
	}
}
