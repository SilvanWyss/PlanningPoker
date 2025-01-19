package ch.nolix.planningpoker.webapplication.roomanalysiscomponent;

import java.util.Locale;

import ch.nolix.coreapi.programatomapi.stringcatalogapi.StringCatalog;
import ch.nolix.planningpoker.logic.analysis.RoomAnalysis;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.logicapi.analysisapi.IRoomAnalysis;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataSupplier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomAnalysisComponent
extends ComponentWithDataSupplier<RoomAnalysisController, IPlanningPokerContext, IDataAdapter> {

  public RoomAnalysisComponent(
    final String roomId,
    final WebClientSession<IPlanningPokerContext> session,
    final IDataAdapter initialDataAdapter) {
    super(new RoomAnalysisController(roomId), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(RoomAnalysisController controller, IDataAdapter dataAdapter) {

    final var roomId = controller.getRoomId();
    final var room = dataAdapter.getStoredRoomById(roomId);

    if (room.hasSetEstimatesVisible()) {
      return createRoomAnalysisControlWhenEstimatesAreVisible(room);
    }

    return new Grid();
  }

  private IControl<?, ?> createRoomAnalysisControlWhenEstimatesAreVisible(final IRoom room) {

    final var roomAnalysis = RoomAnalysis.forRoom(room);

    return createRoomAnalysisControlWhenEstimatesAreVisible(roomAnalysis);
  }

  private IControl<?, ?> createRoomAnalysisControlWhenEstimatesAreVisible(final IRoomAnalysis roomAnalysis) {

    final var minEstimateInStoryPoints = roomAnalysis.getMinEstimateInStoryPointsOrZero();
    final var maxestimateInStoryPoints = roomAnalysis.getMaxEstimateInStoryPointsOrZero();

    final var averageText = String.format(Locale.ENGLISH, "%.1f", roomAnalysis.getAverageEstimateInStoryPointsOrZero());

    final var averageDeviationFromAverageInStoryPointsText = String.format(
      Locale.ENGLISH,
      "%.1f",
      roomAnalysis.getAverageDeviationFromAverageEstimateInStoryPointsOrZero());

    final var rangeText = String.format(Locale.ENGLISH, "%.1f - %.1f", minEstimateInStoryPoints,
      maxestimateInStoryPoints);

    final var differenceText = String.format(Locale.ENGLISH, "%.1f",
      maxestimateInStoryPoints - minEstimateInStoryPoints);

    return new Grid()
      .insertTextAtRowAndColumn(1, 1, StringCatalog.LONG_LEFT_RIGHT_ARROW)
      .insertTextAtRowAndColumn(1, 2, rangeText)
      .insertTextAtRowAndColumn(2, 1, StringCatalog.UPPERCASE_DELTA)
      .insertTextAtRowAndColumn(2, 2, differenceText)
      .insertTextAtRowAndColumn(3, 1, StringCatalog.AVERAGE)
      .insertTextAtRowAndColumn(3, 2, averageText)
      .insertTextAtRowAndColumn(4, 1, StringCatalog.AVERAGE + StringCatalog.UPPERCASE_DELTA)
      .insertTextAtRowAndColumn(4, 2, averageDeviationFromAverageInStoryPointsText);
  }
}
