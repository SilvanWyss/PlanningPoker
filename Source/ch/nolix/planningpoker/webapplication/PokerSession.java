package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.container.singlecontainer.SingleContainer;
import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.planningpokerapi.applicationcontextapi.IRoomSubscriber;
import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoomVisit;
import ch.nolix.system.webgui.control.Button;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.containerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.controlapi.LabelRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class PokerSession extends PageSession implements IRoomSubscriber {
	
	private static final PokerSessionAssembler POKER_SESSION_ASSEMBLER = new PokerSessionAssembler();
	
	private static final PokerSessionHelper POKER_SESSION_HELPER = new PokerSessionHelper();
	
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
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var userId = getUserId();
		final var user = dataController.getOriUserById(userId);
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
			new Button()
			.setText("Go to another room")
			.setLeftMouseButtonPressAction(
				() -> POKER_SESSION_HELPER.openGoToOtherRoomDialog(roomVisit.getOriVisitor().getId(), this)
			),
			new HorizontalStack()
			.setRole(ContainerRole.HEADER_CONTAINER)
			.addControl(
				new Label()
				.setRole(LabelRole.LEVEL1_HEADER)
				.setText("Room " + roomVisit.getOriParentRoom().getNumber()),
				new Button()
				.setText("Link to room")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openShareRoomDialog(roomVisit.getOriParentRoom(), this)
				)
			),
			new Label()
			.setText(POKER_SESSION_HELPER.getCaptainInfoText(roomVisit)),
			new HorizontalStack()
			.setVisibility(POKER_SESSION_HELPER.isAllowedToConfigureRoom(roomVisit))
			.addControl(
				new Button()
				.setText("Show/hide estimates")
				.setLeftMouseButtonPressAction(
					() ->
					POKER_SESSION_HELPER.toggleEstimateVisibilityAndUpdate(
						roomVisit.getOriParentRoom().getId(),
						getOriApplicationContext()
					)
				),
				new Button()
				.setText("Delete estimates")
				.setLeftMouseButtonPressAction(
					() -> POKER_SESSION_HELPER.openDeleteEstimatesDialog(
						roomVisit.getOriParentRoom().getId(),
						this
					)
				)
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
