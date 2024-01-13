package ch.nolix.planningpoker.webapplication.cardsetcomponent;

import ch.nolix.coreapi.commontypetoolapi.stringtoolapi.StringCatalogue;
import ch.nolix.planningpoker.datamodel.dataevaluator.RoomVisitEvaluator;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataSupplier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CardSetComponent
extends ComponentWithDataSupplier<CardSetController, IPlanningPokerContext, IDataAdapter> {

  private static final RoomVisitEvaluator ROOM_VISIT_EVALUATOR = new RoomVisitEvaluator();

  public CardSetComponent(
    final String roomVisitId,
    final String roomId,
    final WebClientSession<IPlanningPokerContext> session,
    final IDataAdapter initialDataAdapter) {
    super(new CardSetController(roomVisitId, roomId), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(final CardSetController controller, final IDataAdapter dataAdapter) {

    final var roomVisitId = controller.getRoomVisitId();
    final var roomVisit = dataAdapter.getStoredRoomVisitById(roomVisitId);

    return createControl(roomVisit, controller);
  }

  private IControl<?, ?> createControl(final IRoomVisit roomVisit, final CardSetController controller) {
    return new VerticalStack()
      .addControl(
        new HorizontalStack()
          .addControl(
            createDeleteEstimateCardControl(roomVisit, controller),
            createEstimateCardControl(roomVisit, 0, controller),
            createEstimateCardControl(roomVisit, 0.5, controller),
            createEstimateCardControl(roomVisit, 1, controller),
            createEstimateCardControl(roomVisit, 2, controller),
            createEstimateCardControl(roomVisit, 3, controller),
            createEstimateCardControl(roomVisit, 5, controller)),
        new HorizontalStack()
          .addControl(
            createEstimateCardControl(roomVisit, 8, controller),
            createEstimateCardControl(roomVisit, 13, controller),
            createEstimateCardControl(roomVisit, 21, controller),
            createEstimateCardControl(roomVisit, 34, controller),
            createEstimateCardControl(roomVisit, 55, controller),
            createEstimateCardControl(roomVisit, 89, controller),
            createInfiniteEstimateCardControl(roomVisit, controller)));
  }

  private IControl<?, ?> createDeleteEstimateCardControl(
    final IRoomVisit roomVisit,
    final CardSetController controller) {

    final var deleteEstimateCardControl = new Button()
      .addToken("card")
      .setText("\u2715")
      .setLeftMouseButtonPressAction(
        () -> controller.deleteEstimateAndTrigger(roomVisit.getId()));

    if (!ROOM_VISIT_EVALUATOR.hasEstimate(roomVisit)) {
      deleteEstimateCardControl.addToken("activated_card");
    }

    return deleteEstimateCardControl;
  }

  private IControl<?, ?> createEstimateCardControl(
    final IRoomVisit roomVisit,
    final double estimateInStoryPoints,
    final CardSetController controller) {

    final var estimateCardButton = new Button()
      .addToken("card")
      .setText(controller.getEstimateCardText(estimateInStoryPoints))
      .setLeftMouseButtonPressAction(
        () -> controller.setEstimateInStoryPointsAndTrigger(roomVisit.getId(), estimateInStoryPoints));

    if (roomVisit.hasEstimateInStorypoints() && roomVisit.getEstimateInStoryPoints() == estimateInStoryPoints) {
      estimateCardButton.addToken("activated_card");
    }

    return estimateCardButton;
  }

  private IControl<?, ?> createInfiniteEstimateCardControl(
    final IRoomVisit roomVisit,
    final CardSetController controller) {

    final var infiniteEstimateCardButton = new Button()
      .addToken("card")
      .setText(StringCatalogue.INFINITY)
      .setLeftMouseButtonPressAction(() -> controller.setInfiniteEstimateAndTrigger(roomVisit.getId()));

    if (roomVisit.hasInfiniteEstimate()) {
      infiniteEstimateCardButton.addToken("activated_card");
    }

    return infiniteEstimateCardButton;
  }
}
