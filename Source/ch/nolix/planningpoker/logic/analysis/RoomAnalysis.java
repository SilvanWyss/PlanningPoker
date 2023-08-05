package ch.nolix.planningpoker.logic.analysis;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.math.main.GlobalCalculator;
import ch.nolix.core.programatom.function.FunctionCatalogue;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.analysisapi.IRoomAnalysis;

public final class RoomAnalysis implements IRoomAnalysis {
	
	public static RoomAnalysis forRoom(final IRoom room) {
		
		final var estimatesInStoryPoints =
		room
		.getStoredRoomVisits()
		.getStoredSelected(IRoomVisit::hasEstimateInStorypoints)
		.to(IRoomVisit::getEstimateInStoryPoints);
		
		return new RoomAnalysis(estimatesInStoryPoints);
	}
	
	private final IContainer<Double> estimatesInStoryPoints;
	
	private RoomAnalysis(final IContainer<Double> estimatesInStoryPoints) {
		
		GlobalValidator.assertThat(estimatesInStoryPoints).thatIsNamed("estimates in story points").isNotNull();
		
		this.estimatesInStoryPoints = estimatesInStoryPoints;
	}
	
	@Override
	public double getAverageDeviationFromAverageEstimateInStoryPointsOrZero() {
		
		if (containsEstimatesInStoryPoints()) {
			
			final var averageEstimateInStoryPoints = getAverageEstimateInStoryPointsOrZero();
			
			return
			estimatesInStoryPoints.getAverage(
				e -> GlobalCalculator.getAbsoluteDifference(averageEstimateInStoryPoints, e)
			);
		}
		
		return 0;
	}
	
	@Override
	public double getAverageEstimateInStoryPointsOrZero() {
		
		if (containsEstimatesInStoryPoints()) {
			return estimatesInStoryPoints.getAverage(FunctionCatalogue::getSelf);
		}
		
		return 0;
	}
	
	@Override
	public double getMaxEstimateInStoryPointsOrZero() {
		
		if (containsEstimatesInStoryPoints()) {
			return estimatesInStoryPoints.getMax(FunctionCatalogue::getSelf);
		}
		
		return 0;
	}
	
	@Override
	public double getMedianEstimateInStoryPointsOrZero() {
		
		if (containsEstimatesInStoryPoints()) {
			return estimatesInStoryPoints.getMedian(FunctionCatalogue::getSelf);
		}
		
		return 0;
	}
	
	@Override
	public double getMinEstimateInStoryPointsOrZero() {
		
		if (containsEstimatesInStoryPoints()) {
			return estimatesInStoryPoints.getMin(FunctionCatalogue::getSelf);
		}
		
		return 0;
	}
	
	private boolean containsEstimatesInStoryPoints() {
		return estimatesInStoryPoints.containsAny();
	}
}
