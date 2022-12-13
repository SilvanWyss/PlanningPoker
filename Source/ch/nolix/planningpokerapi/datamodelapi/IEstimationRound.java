package ch.nolix.planningpokerapi.datamodelapi;

import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpokerapi.analysisapi.IEstimationRoundAnalysis;

public interface IEstimationRound {
	
	IEstimationRoundAnalysis getAnalysis();
	
	IContainer<IEstimation> getRefEstimations();
}
