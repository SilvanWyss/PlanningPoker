package ch.nolix.planningpoker.backend.examiner;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.backendapi.examinerapi.IRoomExaminer;

public final class RoomExaminer implements IRoomExaminer {

  private static final RoomVisitExaminer ROOM_VISIT_EVALUATOR = new RoomVisitExaminer();

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
