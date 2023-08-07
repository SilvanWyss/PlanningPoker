package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.programcontrolapi.triggerapi.ITriggerableSubscriber;
import ch.nolix.planningpoker.webapplication.cardsetcomponent.CardSetComponent;
import ch.nolix.planningpoker.webapplication.controller.PokerController;
import ch.nolix.planningpoker.webapplication.estimateoverviewcomponent.EstimateOverviewComponent;
import ch.nolix.planningpoker.webapplication.roomanalysiscomponent.RoomAnalysisComponent;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession implements ITriggerableSubscriber {
	
	private static final PokerController POKER_SESSION_HELPER = new PokerController();
	
	public static PokerSession withUserIdAndRoomId(final String userId, final String roomId) {
		return new PokerSession(userId, roomId);
	}
	
	private final String roomId;
	
	private PokerSession(final String userId, final String roomId) {
		
		super(SingleContainer.withElementOrEmpty(userId));
		
		GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
		
		this.roomId = roomId;
	}
	
	@Override
	public void trigger() {
		refreshIfDoesNotHaveOpenDialog();
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataAdapter dataAdapter) {
		
		getStoredApplicationContext().getStoredRoomChangeNotifier().registerRoomSubscriberIfNotRegistered(roomId, this);
		
		final var userId = getUserId();
		final var user = dataAdapter.getStoredUserById(userId);
		final var roomVisit = user.getStoredCurrentRoomVisit();
				
		return createMainControl(roomVisit, dataAdapter);
	}
	
	@Override
	protected void noteSelfChange() {
		getStoredApplicationContext().getStoredRoomChangeNotifier().noteRoomChange(roomId);
	}
	
	private IControl<?, ?> createMainControl(final IRoomVisit roomVisit, final IDataAdapter dataAdapter) {
		return
		new VerticalStack()
		.addControl(
			new HorizontalStack()
			.setRole(ContainerRole.HEADER_CONTAINER)
			.addControl(
				new Label()
				.setRole(LabelRole.LEVEL1_HEADER)
				.setText("Room " + roomVisit.getStoredParentRoom().getNumber()),
				new Button()
				.setText("Show link to room")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openShareRoomDialog(roomVisit.getStoredParentRoom(), this)
				),
				new Button()
				.setText("Go to another room")
				.setLeftMouseButtonPressAction(
					() ->
					POKER_SESSION_HELPER.openGoToOtherRoomDialog(
						roomVisit.getStoredVisitor().getId(),
						this,
						SelectRoomSession::withUserId
					)
				)
			),
			new HorizontalStack()
			.addControl(
				new Label()
				.setText(POKER_SESSION_HELPER.getCaptainInfoText(roomVisit)),
				new Button()
				.setText("Show/hide estimates")
				.setLeftMouseButtonPressAction(
					() ->
					POKER_SESSION_HELPER.toggleEstimateVisibilityAndUpdate(
						roomVisit.getStoredParentRoom().getId(),
						getStoredApplicationContext()
					)
				)
				.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit)),
				new Button()
				.setText("Delete estimates")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openDeleteEstimatesDialog(
						roomVisit.getStoredParentRoom().getId(),
						this
					)
				)
				.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit))
			),
			new CardSetComponent(roomVisit.getId(), roomId, this, dataAdapter).getStoredControl(),
			new HorizontalStack()
			.addControl(
				new EstimateOverviewComponent(roomVisit.getId(), roomId, this, dataAdapter).getStoredControl(),
				new RoomAnalysisComponent(roomId, this, dataAdapter).getStoredControl()
			)
		);
	}
}
