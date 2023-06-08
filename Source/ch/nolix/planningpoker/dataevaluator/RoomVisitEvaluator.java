package ch.nolix.planningpoker.dataevaluator;

import ch.nolix.planningpokerapi.dataevaluatorapi.IRoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;

public final class RoomVisitEvaluator implements IRoomVisitEvaluator {
	
	@Override
	public boolean hasAnyEstimation(final IRoomVisit roomVisit) {
		return
		roomVisit != null
		&& (roomVisit.hasEstimateInStorypoints() || roomVisit.hasInfiniteEstimate());
	}
}
