package ch.nolix.planningpokerapi.analysisapi;

public interface IEstimateRoundAnalysis {
	
	double getAverageDeviationFromAverageEstimateInStoryPoints();
	
	double getAverageEstimateInStoryPoints();
	
	double getMedianEstimateInStoryPoints();
	
	double getMinEstimateInStoryPoints();
	
	double getMaxEstimateInStoryPoints();
}
