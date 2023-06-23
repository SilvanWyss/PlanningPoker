package ch.nolix.planningpokerapi.logicapi.analysisapi;

public interface IRoomAnalysis {
	
	double getAverageDeviationFromAverageEstimateInStoryPointsOrZero();
	
	double getAverageEstimateInStoryPointsOrZero();
	
	double getMaxEstimateInStoryPointsOrZero();
	
	double getMedianEstimateInStoryPointsOrZero();
	
	double getMinEstimateInStoryPointsOrZero();
}
