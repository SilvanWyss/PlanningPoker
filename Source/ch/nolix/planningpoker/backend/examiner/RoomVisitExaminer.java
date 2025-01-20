package ch.nolix.planningpoker.backend.examiner;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.backendapi.examinerapi.IRoomVisitExaminer;

public final class RoomVisitExaminer implements IRoomVisitExaminer {

  @Override
  public boolean hasEstimate(final IRoomVisit roomVisit) {
    return roomVisit != null
    && hasEstimateWhenIsNotNull(roomVisit);
  }

  private boolean hasEstimateWhenIsNotNull(final IRoomVisit roomVisit) {
    return roomVisit.hasEstimateInStorypoints() || roomVisit.hasInfiniteEstimate();
  }
}
