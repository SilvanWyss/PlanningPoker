package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;

public final class PageSessionHelper {
	
	public String getUserLabelText(final IDataController dataController, final PageSession session) {
		
		final var userId = session.getOriParentClient().getCookieValueByCookieNameOrNull("userId");
		final var user = dataController.getOriUserByIdOrNull(userId);
		
		if (user != null) {
			return ("you: " + user.getName());
		}
		
		return "Welcome!";
	}
}
