package ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;

public interface IRoomVisitEvaluator {

  boolean hasEstimate(IRoomVisit roomVisit);
}
