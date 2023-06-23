package ch.nolix.planningpokerapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;

public interface IRoomEvaluator {
	
	boolean containsEstimate(IRoom room);
}
