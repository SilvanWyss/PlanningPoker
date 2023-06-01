package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.commontype.commontypeconstant.StringCatalogue;
import ch.nolix.planningpokerapi.applicationcontextapi.IApplicationContext;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;

public final class PokerSessionHelper {
	
	public String getCaptainInfoText(final IRoomVisit roomVisit) {
		
		final var roomCreator = roomVisit.getOriParentRoom().getOriParentCreator();
		final var user = roomVisit.getOriVisitor();
		
		if (roomCreator.hasId(user.getId())) {
			return "You are our captain.";
		}
		
		return (roomCreator.getName() + " is our captain.");
	}
	
	public String getEstimationText(final IRoomVisit roomVisit) {
		
		if (roomVisit.hasEstimationInStorypoints()) {
			return String.valueOf(roomVisit.getEstimationInStoryPoints());
		}
		
		if (roomVisit.hasInfiniteEstimation()) {
			return StringCatalogue.INFINITY;
		}
		
		return StringCatalogue.MINUS;
	}
	
	public void setEstimationInStoryPoints(
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
}
