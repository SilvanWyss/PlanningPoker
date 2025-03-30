package ch.nolix.planningpoker.backend.analysis;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.core.math.main.Calculator;
import ch.nolix.core.programatom.function.FunctionService;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.backendapi.analysisapi.IRoomAnalysis;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;

public final class RoomAnalysis implements IRoomAnalysis {

  private final IContainer<Double> estimatesInStoryPoints;

  private RoomAnalysis(final IContainer<Double> estimatesInStoryPoints) {

    Validator.assertThat(estimatesInStoryPoints).thatIsNamed("estimates in story points").isNotNull();

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
      e -> Calculator.getAbsoluteDifference(averageEstimateInStoryPoints, e));
  }

  @Override
  public double getAverageEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getAverageOrZero(FunctionService::getSelf);
  }

  @Override
  public double getMaxEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMaxOrZero(FunctionService::getSelf);
  }

  @Override
  public double getMedianEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMedianOrZero(FunctionService::getSelf);
  }

  @Override
  public double getMinEstimateInStoryPointsOrZero() {
    return estimatesInStoryPoints.getMinOrZero(FunctionService::getSelf);
  }
}
