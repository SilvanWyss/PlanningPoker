package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IEstimateRound {
	
	IContainer<? extends IEstimate> getOriEstimates();
}
