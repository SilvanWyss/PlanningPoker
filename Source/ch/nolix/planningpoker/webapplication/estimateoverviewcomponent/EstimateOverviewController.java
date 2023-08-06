package ch.nolix.planningpoker.webapplication.estimateoverviewcomponent;

import java.util.Locale;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;
import ch.nolix.system.application.webapplication.WebClientSession;

final class EstimateOverviewController extends Controller<IPlanningPokerContext> {
	
	private static final IRoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
	
	private final String roomVisitId;
	
	private final String roomId;
	
	public EstimateOverviewController(
		final String roomVisitId,
		final String roomId,
		final WebClientSession<IPlanningPokerContext> session
	) {
		
		super(session);
		
		GlobalValidator.assertThat(roomId).thatIsNamed("room visit id").isNotBlank();
		GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
		
		this.roomVisitId = roomVisitId;
		this.roomId = roomId;
	}
	
	public String getEstimateText(final IRoomVisit roomVisit) {
		
		if (roomVisit.getStoredParentRoom().hasSetEstimatesVisible()) {
			return getEstimateTextWhenEstimateIsVisible(roomVisit);
		}
		
		return getEstimateTextWhenEstimateIsInvisible(roomVisit);
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	public String getRoomVisitId() {
		return roomVisitId;
	}
	
	public IRoomChangeNotifier getStoredRoomChangeNotifier() {
		return getStoredApplicationContext().getStoredRoomChangeNotifier();
	}
	
	private String getEstimateTextWhenEstimateIsInvisible(final IRoomVisit roomVisit) {
		
		if (ROOM_VISIT_EVALUATOR.hasEstimate(roomVisit)) {
			return StringCatalogue.QUESTION_MARK;
		}
		
		return StringCatalogue.THIN_CROSS;
	}
	
	private String getEstimateTextWhenEstimateIsVisible(final IRoomVisit roomVisit) {
		
		if (roomVisit.hasEstimateInStorypoints()) {
			return getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(roomVisit);
		}
		
		if (roomVisit.hasInfiniteEstimate()) {
			return StringCatalogue.INFINITY;
		}
		
		return StringCatalogue.THIN_CROSS;
	}
	
	private String getEstimateTextWhenEstimateIsVisibleAndHasEstimateInStoryPoints(
		final IRoomVisit roomVisit
	) {
		final var estimateInStoryPoints = roomVisit.getEstimateInStoryPoints();
		
		if (estimateInStoryPoints == 0.5) {
			return "0.5";
		}
		
		return String.format(Locale.ENGLISH, "%.0f", estimateInStoryPoints);
	}
}
