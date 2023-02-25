package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.containerapi.baseapi.IContainer;

public interface IEstimationRound {
	
	IContainer<? extends IEstimation> getRefEstimations();
}
