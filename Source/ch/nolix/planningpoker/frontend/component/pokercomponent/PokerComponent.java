package ch.nolix.planningpoker.frontend.component.pokercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;
import ch.nolix.planningpoker.frontend.component.cardsetcomponent.CardSetComponent;
import ch.nolix.planningpoker.frontend.component.estimateoverviewcomponent.EstimateOverviewComponent;
import ch.nolix.planningpoker.frontend.component.roomanalysiscomponent.RoomAnalysisComponent;
import ch.nolix.planningpoker.frontend.component.roomheadercomponent.RoomHeaderComponent;
import ch.nolix.planningpoker.frontend.component.roommanagercomponent.RoomManagerComponent;
import ch.nolix.planningpokerapi.backendapi.dataadapterapi.IDataAdapter;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.ComponentWithAdapterFactory;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerComponent
extends ComponentWithAdapterFactory<PokerComponentController, IPlanningPokerService, IDataAdapter>
implements ITriggerableSubscriber {

  private final ISelectRoomSessionFactory selectRoomSessionFactory;

  public PokerComponent(
    final String userId,
    final IDataAdapter initialDataAdapter,
    final WebClientSession<IPlanningPokerService> webClientSession,
    final ISelectRoomSessionFactory selectRoomSessionFactory) {

    super(new PokerComponentController(userId), initialDataAdapter, webClientSession);

    GlobalValidator.assertThat(selectRoomSessionFactory).thatIsNamed(ISelectRoomSessionFactory.class).isNotNull();

    this.selectRoomSessionFactory = selectRoomSessionFactory;

    doRegistrations(initialDataAdapter);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  public void trigger() {

    final var userId = getStoredController().getUserId();

    try (final var adapter = getStoredApplicationService().createAdapter()) {

      final var user = adapter.getStoredUserById(userId);

      if (user.isInARoom()) {
        refresh();
      } else {

        final var selectRoomSession = selectRoomSessionFactory.createSelectRoomSessionWihtUserId(userId);

        getStoredWebClientSession().setNext(selectRoomSession);
      }
    }
  }

  @Override
  protected IControl<?, ?> createControl(PokerComponentController controller, IDataAdapter dataAdapter) {

    final var userId = getStoredController().getUserId();
    final var user = dataAdapter.getStoredUserById(userId);
    final var roomVisit = user.getStoredCurrentRoomVisit();
    final var room = roomVisit.getStoredParentRoom();
    final var roomId = room.getId();

    return new VerticalStack()
      .addControl(
        new RoomHeaderComponent(userId, getStoredWebClientSession(), dataAdapter,
          this::createSelectRoomSessionWihtUserId),
        new RoomManagerComponent(userId, getStoredWebClientSession(), dataAdapter),
        new CardSetComponent(roomVisit.getId(), roomId, getStoredWebClientSession(), dataAdapter),
        new HorizontalStack()
          .addControl(
            new EstimateOverviewComponent(roomVisit.getId(), roomId, getStoredWebClientSession(), dataAdapter),
            new RoomAnalysisComponent(roomId, getStoredWebClientSession(), dataAdapter)));
  }

  private void doRegistrations(final IDataAdapter dataSupplier) {

    final var userId = getStoredController().getUserId();
    final var user = dataSupplier.getStoredUserById(userId);
    final var roomVisit = user.getStoredCurrentRoomVisit();
    final var room = roomVisit.getStoredParentRoom();
    final var roomId = room.getId();

    getStoredApplicationService().getStoredRoomChangeNotifier().registerRoomSubscriberIfNotRegistered(roomId, this);
  }

  private WebClientSession<IPlanningPokerService> createSelectRoomSessionWihtUserId(String userId) {
    return selectRoomSessionFactory.createSelectRoomSessionWihtUserId(userId);
  }
}
