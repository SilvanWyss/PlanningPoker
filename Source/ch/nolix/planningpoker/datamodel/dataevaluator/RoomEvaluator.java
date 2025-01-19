package ch.nolix.planningpoker.datamodel.dataevaluator;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.backendapi.examinerapi.IRoomExaminer;

public final class RoomEvaluator implements IRoomExaminer {

  private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();

  @Override
  public boolean containsEstimate(final IRoom room) {
    return room != null
    && containsEstimateWhenIsNotNull(room);
  }

  private boolean containsEstimateWhenIsNotNull(final IRoom room) {

    final var roomVisits = room.getStoredRoomVisits();

    return roomVisits.containsAny(ROOM_VISIT_EVALUATOR::hasEstimate);
  }
}
