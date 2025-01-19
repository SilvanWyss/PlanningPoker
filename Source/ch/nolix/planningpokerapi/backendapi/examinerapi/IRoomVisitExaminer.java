package ch.nolix.planningpokerapi.backendapi.examinerapi;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;

public interface IRoomVisitExaminer {

  boolean hasEstimate(IRoomVisit roomVisit);
}
