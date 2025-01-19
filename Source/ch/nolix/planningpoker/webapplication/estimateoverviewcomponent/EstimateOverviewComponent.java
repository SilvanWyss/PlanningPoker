package ch.nolix.planningpoker.webapplication.estimateoverviewcomponent;

import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoomVisit;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.application.component.ComponentWithDataSupplier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.button.Button;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class EstimateOverviewComponent
extends ComponentWithDataSupplier<EstimateOverviewController, IPlanningPokerService, IDataAdapter> {

  private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();

  public EstimateOverviewComponent(
    final String roomVisitId,
    final String roomId,
    final WebClientSession<IPlanningPokerService> session,
    IDataAdapter initialDataAdapter) {
    super(new EstimateOverviewController(roomVisitId, roomId), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(
    final EstimateOverviewController controller,
    final IDataAdapter dataAdapter) {

    final var roomVisitId = controller.getRoomVisitId();
    final var roomVisit = dataAdapter.getStoredRoomVisitById(roomVisitId);

    return createControl(roomVisit, controller);
  }

  private IControl<?, ?> createControl(final IRoomVisit roomVisit, final EstimateOverviewController controller) {
    final var estimatesGridContainer = new Grid();

    final var room = roomVisit.getStoredParentRoom();
    final var roomCreator = room.getStoredParentCreator();
    final var roomVisitIsFromRoomCreator = roomVisit.getStoredVisitor().hasId(roomCreator.getId());
    var rowIndex = 1;
    for (final var rv : room.getStoredRoomVisits()) {

      final var visitor = rv.getStoredVisitor();

      if (!visitor.hasId(roomCreator.getId()) || ROOM_VISIT_EVALUATOR.hasEstimate(rv)) {

        final var visitorName = visitor.getName();
        estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 1, visitorName);

        final var estimateText = controller.getEstimateText(rv);
        estimatesGridContainer.insertTextAtRowAndColumn(rowIndex, 2, estimateText);

        if (roomVisitIsFromRoomCreator && !visitor.hasId(roomCreator.getId())) {

          final var kickButton = new Button()
            .setText("&#128098;")
            .setLeftMouseButtonPressAction(() -> controller.openKickUserDialog(visitor.getId()));

          estimatesGridContainer.insertControlAtRowAndColumn(rowIndex, 3, kickButton);
        }

        rowIndex++;
      }
    }

    return estimatesGridContainer;
  }
}
