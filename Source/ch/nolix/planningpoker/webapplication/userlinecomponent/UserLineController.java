package ch.nolix.planningpoker.webapplication.userlinecomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.template.webgui.dialog.EnterValueDialogBuilder;

final class UserLineController extends Controller<IPlanningPokerContext> {
	
	private final String userId;
	
	public UserLineController(final String userId, final WebClientSession<IPlanningPokerContext> session) {
		
		super(session);
		
		GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();
		
		this.userId = userId;
	}
	
	public String getLoggedInUserName(final IDataAdapter dataAdapter) {
		
		final var user = dataAdapter.getStoredUserById(userId);
			
		return user.getName();
	}
	
	public void openEditUserNameDialog() {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var originUserName = user.getName();
			
			getStoredSession()
			.getStoredGui()
			.pushLayer(
				new EnterValueDialogBuilder()
				.setInfoText("Edit your user name.")
				.setOriginalValue(originUserName)
				.setValueTaker(this::setUserName)
				.build()
			);
		}
	}
	
	private void setUserName(final String newUserName) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			final var user = databaseAdapter.getStoredUserById(userId);
			user.setName(newUserName);
			databaseAdapter.saveChanges();
		}
	}
}
