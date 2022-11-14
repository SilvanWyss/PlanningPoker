package ch.nolix.planningpoker.analysis;

public interface IEstimationRoundAnalysis {
	
	double getAverageDeviationFromAverageEstimationInStoryPoints();
	
	double getAverageEstimationInStoryPoints();
	
	double getMedianEstimationInStoryPoints();
	
	double getMinEstimationInStoryPoints();
	
	double getMaxEstimationInStoryPoints();
}
