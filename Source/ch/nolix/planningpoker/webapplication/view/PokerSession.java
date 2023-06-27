package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpoker.webapplication.controller.PokerController;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomSubscriber;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession implements IRoomSubscriber {
	
	private static final PokerSessionAssembler POKER_SESSION_ASSEMBLER = new PokerSessionAssembler();
	
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
	public boolean isActive() {
		return belongsToOpenClient();
	}
	
	@Override
	public void trigger() {
		refreshIfDoesNotHaveOpenDialog();
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDatabaseAdapter databaseAdapter) {
		
		final var userId = getUserId();
		final var user = databaseAdapter.getOriUserById(userId);
		final var roomVisit = user.getOriCurrentRoomVisit();
				
		return createMainControl(roomVisit);
	}
	
	@Override
	protected void doRegistrations(final IRoomChangeNotifier roomChangeNotifier) {
		roomChangeNotifier.registerRoomSubscriberIfNotRegistered(roomId, this);
	}
	
	@Override
	protected void noteSelfChange() {
		getOriApplicationContext().getOriRoomChangeNotifier().noteRoomChange(roomId);
	}
	
	private IControl<?, ?> createMainControl(final IRoomVisit roomVisit) {
		return
		new VerticalStack()
		.addControl(
			new HorizontalStack()
			.setRole(ContainerRole.HEADER_CONTAINER)
			.addControl(
				new Label()
				.setRole(LabelRole.LEVEL1_HEADER)
				.setText("Room " + roomVisit.getOriParentRoom().getNumber()),
				new Button()
				.setText("Show link to room")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openShareRoomDialog(roomVisit.getOriParentRoom(), this)
				),
				new Button()
				.setText("Go to another room")
				.setLeftMouseButtonPressAction(
					() ->
					POKER_SESSION_HELPER.openGoToOtherRoomDialog(
						roomVisit.getOriVisitor().getId(),
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
						roomVisit.getOriParentRoom().getId(),
						getOriApplicationContext()
					)
				)
				.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit)),
				new Button()
				.setText("Delete estimates")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openDeleteEstimatesDialog(
						roomVisit.getOriParentRoom().getId(),
						this
					)
				)
				.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit))
			),
			POKER_SESSION_ASSEMBLER.createEstimateCardsControl(roomVisit, getOriApplicationContext()),
			new HorizontalStack()
			.addControl(
				POKER_SESSION_ASSEMBLER.createEstimatesControl(roomVisit.getOriParentRoom()),
				POKER_SESSION_ASSEMBLER.createRoomAnalysisControl(roomVisit.getOriParentRoom())
			)
		);
	}
}
