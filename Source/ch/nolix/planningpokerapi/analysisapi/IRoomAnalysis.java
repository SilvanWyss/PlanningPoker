package ch.nolix.planningpokerapi.analysisapi;

public interface IRoomAnalysis {
	
	double getAverageDeviationFromAverageEstimateInStoryPointsOrZero();
	
	double getAverageEstimateInStoryPointsOrZero();
	
	double getMaxEstimateInStoryPointsOrZero();
	
	double getMedianEstimateInStoryPointsOrZero();
	
	double getMinEstimateInStoryPointsOrZero();
}
