package ch.nolix.planningpoker.webapplication.estimatetablecomponent;

import java.util.Locale;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class EstimateTableController extends Controller<IPlanningPokerContext> {
	
	private final String roomVisitId;
	
	public EstimateTableController(final String roomVisitId, final WebClientSession<IPlanningPokerContext> session) {
		
		super(session);
		
		GlobalValidator.assertThat(roomVisitId).thatIsNamed("room visit id").isNotBlank();
		
		this.roomVisitId = roomVisitId;
	}
	
	public void deleteEstimateAndTrigger(final String roomVisitId) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.deleteEstimate();
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public String getEstimateCardText(final double estimateInStoryPoints) {
		
		if (estimateInStoryPoints == 0.5) {
			return "0.5";
		}
		
		return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
	}
	
	public String getRoomVisitId() {
		return roomVisitId;
	}
	
	public IRoomChangeNotifier getStoredRoomChangeNotifier() {
		return getStoredApplicationContext().getStoredRoomChangeNotifier();
	}
	
	public void setEstimateInStoryPointsAndTrigger(final String roomVisitId, final double estimateInStoryPoints) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.setEstimateInStoryPoints(estimateInStoryPoints);
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimateAndTrigger(final String roomVisitId) {
		
		final var applicationContext = getStoredApplicationContext();
		
		try (final var databaseAdapter = applicationContext.createDataAdapter()) {
			
			final var roomVisit = databaseAdapter.getStoredRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimate();
			databaseAdapter.saveChanges();
			
			final var room = roomVisit.getStoredParentRoom();	
			applicationContext.getStoredRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
}
