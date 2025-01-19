package ch.nolix.planningpokerapi.backendapi.examinerapi;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;

public interface IRoomExaminer {

  boolean containsEstimate(IRoom room);
}
