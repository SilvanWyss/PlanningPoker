package ch.nolix.planningpoker.webapplication.selectroomcomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class SelectRoomController extends Controller<IPlanningPokerContext> {
	
	private final String userId;
	
	public SelectRoomController(final String userId, final WebClientSession<IPlanningPokerContext> session) {
		
		super(session);
		
		GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();
		
		this.userId = userId;
	}
	
	public void createAndEnterRoomAndRedirect(final IPokerSessionFactory pokerSessionFactory) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var room = databaseAdapter.createNewRoomAndEnterRoom(user);
			databaseAdapter.saveChanges();
			
			final var pokerSession = pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(userId, room.getId());
			getStoredSession().setNext(pokerSession);
		}
	}
	
	public void enterRoomAndRedirect(final String roomNumber, final IPokerSessionFactory pokerSessionFactory) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var user = databaseAdapter.getStoredUserById(userId);
			final var room = databaseAdapter.getStoredRoomByNumber(roomNumber);
			databaseAdapter.enterRoom(user, room);
			databaseAdapter.saveChanges();
			
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
			
			final var pokerSession = pokerSessionFactory.createPokerSessionWihtUserIdAndRoomId(userId, room.getId());
			getStoredSession().setNext(pokerSession);
		}
	}
}
