package ch.nolix.planningpokerapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;

public interface IRoomVisitEvaluator {
	
	boolean hasAnyEstimation(IRoomVisit roomVisit);
}
