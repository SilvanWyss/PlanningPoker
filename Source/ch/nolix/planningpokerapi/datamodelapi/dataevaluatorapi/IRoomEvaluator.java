package ch.nolix.planningpokerapi.datamodelapi.dataevaluatorapi;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;

public interface IRoomEvaluator {

  boolean containsEstimate(IRoom room);
}
