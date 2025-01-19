package ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;

public interface IRoomVisitEvaluator {

  boolean hasEstimate(IRoomVisit roomVisit);
}
