package ch.nolix.planningpoker.datamodel.dataevaluator;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi.IRoomVisitEvaluator;

public final class RoomVisitEvaluator implements IRoomVisitEvaluator {

  @Override
  public boolean hasEstimate(final IRoomVisit roomVisit) {
    return roomVisit != null
    && hasEstimateWhenIsNotNull(roomVisit);
  }

  private boolean hasEstimateWhenIsNotNull(final IRoomVisit roomVisit) {
    return roomVisit.hasEstimateInStorypoints() || roomVisit.hasInfiniteEstimate();
  }
}
