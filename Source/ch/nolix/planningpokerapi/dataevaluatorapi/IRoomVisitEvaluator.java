package ch.nolix.planningpokerapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;

public interface IRoomVisitEvaluator {
	
	boolean hasEstimate(IRoomVisit roomVisit);
}
