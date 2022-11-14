package ch.nolix.planningpoker.analysisapi;

public interface IEstimationRoundAnalysis {
	
	double getAverageDeviationFromAverageEstimationInStoryPoints();
	
	double getAverageEstimationInStoryPoints();
	
	double getMedianEstimationInStoryPoints();
	
	double getMinEstimationInStoryPoints();
	
	double getMaxEstimationInStoryPoints();
}
