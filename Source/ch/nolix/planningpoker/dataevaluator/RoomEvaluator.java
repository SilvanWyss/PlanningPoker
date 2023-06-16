package ch.nolix.planningpoker.dataevaluator;

import ch.nolix.planningpokerapi.dataevaluatorapi.IRoomEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;

public final class RoomEvaluator implements IRoomEvaluator {
	
	private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();
	
	@Override
	public boolean containsEstimate(final IRoom room) {
		return
		room != null
		&& containsEstimateWhenIsNotNull(room);
	}
	
	private boolean containsEstimateWhenIsNotNull(final IRoom room) {
		
		final var roomVisits = room.getOriRoomVisits();
		
		return roomVisits.containsAny(ROOM_VISIT_EVALUATOR::hasAnyEstimation);
	}
}
