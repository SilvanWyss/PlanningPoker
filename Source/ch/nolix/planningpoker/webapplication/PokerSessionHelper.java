package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;

public final class PokerSessionHelper {
	
	public void deleteEstimationAndUpdate(final String roomVisitId, final IApplicationContext applicationContext) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.deleteEstimation();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public String getCaptainInfoText(final IRoomVisit roomVisit) {
		
		final var roomCreator = roomVisit.getOriParentRoom().getOriParentCreator();
		final var user = roomVisit.getOriVisitor();
		
		if (roomCreator.hasId(user.getId())) {
			return "You are our captain.";
		}
		
		return (roomCreator.getName() + " is our captain.");
	}
	
	public String getEstimationText(final IRoomVisit roomVisit) {
		
		if (roomVisit.getOriParentRoom().hasSetEstimationsVisible()) {
			return getEstimationTextWhenEstimationIsVisible(roomVisit);
		}
		
		return StringCatalogue.QUESTION_MARK;
	}

	public void setEstimationInStoryPointsAndUpdate(
		final String roomVisitId,
		final int estimationInStoryPoints,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setEstimationInStoryPoints(estimationInStoryPoints);
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	public void setInfiniteEstimationAndUpdate(
		final String roomVisitId,
		final IApplicationContext applicationContext
	) {
		try (final var dataController = applicationContext.createDataController()) {
			
			final var roomVisit = dataController.getOriRoomVisitById(roomVisitId);
			roomVisit.setInfiniteEstimation();
			dataController.saveChanges();
			
			final var room = roomVisit.getOriParentRoom();	
			applicationContext.getOriRoomChangeNotifier().noteRoomChange(room.getId());
		}
	}
	
	private String getEstimationTextWhenEstimationIsVisible(final IRoomVisit roomVisit) {
		
		if (roomVisit.hasEstimationInStorypoints()) {
			return String.valueOf(roomVisit.getEstimationInStoryPoints());
		}
		
		if (roomVisit.hasInfiniteEstimation()) {
			return StringCatalogue.INFINITY;
		}
		
		return StringCatalogue.THIN_CROSS;
	}
}
