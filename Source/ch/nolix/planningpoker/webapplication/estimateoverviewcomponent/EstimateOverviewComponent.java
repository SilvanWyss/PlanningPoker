package ch.nolix.planningpoker.webapplication.estimateoverviewcomponent;

import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataAdapter;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.container.Grid;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class EstimateOverviewComponent
extends ComponentWithDataAdapter<EstimateOverviewController, IPlanningPokerContext, IDataAdapter>
implements ITriggerableSubscriber {

  private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();

  public EstimateOverviewComponent(
    final String roomVisitId,
    final String roomId,
    final WebClientSession<IPlanningPokerContext> session,
    IDataAdapter initialDataAdapter) {
    super(new EstimateOverviewController(roomVisitId, roomId, session), initialDataAdapter);
  }

  @Override
  public void trigger() {
    refresh();
  }

  @Override
  protected IControl<?, ?> createControl(
    final EstimateOverviewController controller,
    final IDataAdapter dataAdapter) {

    final var roomVisitId = controller.getRoomVisitId();
    final var roomVisit = dataAdapter.getStoredRoomVisitById(roomVisitId);

    return createControl(roomVisit, controller);
  }

  @Override
  protected void doRegistrations(final EstimateOverviewController controller) {
    //Does nothing.
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
