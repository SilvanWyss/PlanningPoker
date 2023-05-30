package ch.nolix.planningpoker.webapplication;

public final class CreateUserSessionHelper {
	
	public void createUserAndAddCookieAndRedirect(final String userName, final CreateUserSession session) {
		
		final var applicationContext = session.getOriApplicationContext();
		
		try (final var dataController = applicationContext.createDataController()) {
			
			final var user = dataController.createUserWithName(userName);
			dataController.saveChanges();
			
			session.getOriParentClient().setOrAddCookieWithNameAndValue("userId", user.getId());
			
			session.setNext(new CreateRoomSession());
		}
	}
}
