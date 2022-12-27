package ch.nolix.planningpoker.analysis;

import ch.nolix.core.container.immutablelist.ImmutableList;
import ch.nolix.core.math.main.GlobalCalculator;
import ch.nolix.core.programatom.function.FunctionCatalogue;
import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpokerapi.analysisapi.IEstimationRoundAnalysis;

public final class EstimationRoundAnalysis implements IEstimationRoundAnalysis {
	
	public static EstimationRoundAnalysis forEstimationsInStoryPoints(
		final IContainer<Double> estimationsInStoryPoints
	) {
		return new EstimationRoundAnalysis(estimationsInStoryPoints);
	}
	
	private final IContainer<Double> estimationsInStoryPoints;
	
	private EstimationRoundAnalysis(final IContainer<Double> estimationsInStoryPoints) {
		this.estimationsInStoryPoints = ImmutableList.forIterable(estimationsInStoryPoints);
	}
	
	@Override
	public double getAverageDeviationFromAverageEstimationInStoryPoints() {
		
		final var averageEstimationInStoryPoints = getAverageEstimationInStoryPoints();
		
		return
		estimationsInStoryPoints.getAverageByDouble(
			e -> GlobalCalculator.getAbsoluteDifference(averageEstimationInStoryPoints, e)
		);
	}
	
	@Override
	public double getAverageEstimationInStoryPoints() {
		return estimationsInStoryPoints.getAverageByDouble(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMedianEstimationInStoryPoints() {
		return estimationsInStoryPoints.getMedianByDouble(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMinEstimationInStoryPoints() {
		return estimationsInStoryPoints.getMinDouble(FunctionCatalogue::getSelf);
	}
	
	@Override
	public double getMaxEstimationInStoryPoints() {
		return estimationsInStoryPoints.getMaxDouble(FunctionCatalogue::getSelf);
	}
}
