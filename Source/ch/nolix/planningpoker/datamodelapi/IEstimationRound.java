package ch.nolix.planningpoker.datamodelapi;

import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpoker.analysisapi.IEstimationRoundAnalysis;

public interface IEstimationRound {
	
	IEstimationRoundAnalysis getAnalysis();
	
	IContainer<IEstimation> getRefEstimations();
}
