package ch.nolix.planningpoker.analysis;

import ch.nolix.core.container.immutablelist.ImmutableList;
import ch.nolix.core.math.main.GlobalCalculator;
import ch.nolix.core.programatom.function.FunctionCatalogue;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.analysisapi.IEstimateRoundAnalysis;

public final class EstimateRoundAnalysis implements IEstimateRoundAnalysis {
	
	public static EstimateRoundAnalysis forEstimatesInStoryPoints(
		final IContainer<Double> estimateInStoryPoints
	) {
		return new EstimateRoundAnalysis(estimateInStoryPoints);
	}
	
	private final IContainer<Double> estimatesInStoryPoints;
	
	private EstimateRoundAnalysis(final IContainer<Double> estimatesInStoryPoints) {
		this.estimatesInStoryPoints = ImmutableList.forIterable(estimatesInStoryPoints);
	}
	
	@Override
	public double getAverageDeviationFromAverageEstimateInStoryPoints() {
		
		final var averageEstimateInStoryPoints = getAverageEstimateInStoryPoints();
		
		return
		estimatesInStoryPoints.getAverage(
			e -> GlobalCalculator.getAbsoluteDifference(averageEstimateInStoryPoints, e)
		);
	}
	
	@Override
	public double getAverageEstimateInStoryPoints() {
		return estimatesInStoryPoints.getAverage(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMedianEstimateInStoryPoints() {
		return estimatesInStoryPoints.getMedian(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMinEstimateInStoryPoints() {
		return estimatesInStoryPoints.getMin(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMaxEstimateInStoryPoints() {
		return estimatesInStoryPoints.getMax(FunctionCatalogue::getSelf);
	}
}
