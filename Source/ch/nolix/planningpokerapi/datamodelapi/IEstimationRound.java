package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.containerapi.mainapi.IContainer;

public interface IEstimationRound {
	
	IContainer<? extends IEstimation> getRefEstimations();
}
