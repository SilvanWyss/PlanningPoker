package ch.nolix.planningpoker.logic.analysis;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.math.main.GlobalCalculator;
import ch.nolix.core.programatom.function.FunctionCatalogue;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.analysisapi.IRoomAnalysis;

public final class RoomAnalysis implements IRoomAnalysis {

  private final IContainer<Double> estimatesInStoryPoints;

  private RoomAnalysis(final IContainer<Double> estimatesInStoryPoints) {

    GlobalValidator.assertThat(estimatesInStoryPoints).thatIsNamed("estimates in story points").isNotNull();

    this.estimatesInStoryPoints = estimatesInStoryPoints;
  }

  public static RoomAnalysis forRoom(final IRoom room) {
  
    final var estimatesInStoryPoints = room
      .getStoredRoomVisits()
      .getStoredSelected(IRoomVisit::hasEstimateInStorypoints)
      .to(IRoomVisit::getEstimateInStoryPoints);
  
    return new RoomAnalysis(estimatesInStoryPoints);
  }

  @Override
  public double getAverageDeviationFromAverageEstimateInStoryPointsOrZero() {

    final var averageEstimateInStoryPoints = getAverageEstimateInStoryPointsOrZero();

    return estimatesInStoryPoints.getAverageOrZero(
      e -> GlobalCalculator.getAbsoluteDifference(averageEstimateInStoryPoints, e));
  }

  @Override
  public double getAverageEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getAverageOrZero(FunctionCatalogue::getSelf);
  }

  @Override
  public double getMaxEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMaxOrZero(FunctionCatalogue::getSelf);
  }

  @Override
  public double getMedianEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMedianOrZero(FunctionCatalogue::getSelf);
  }

  @Override
  public double getMinEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMinOrZero(FunctionCatalogue::getSelf);
  }
}
