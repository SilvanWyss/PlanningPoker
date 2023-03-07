package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.applicationcontextapi.IDataController;
import ch.nolix.planningpokerapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.datamodelapi.IRoomVisit;
import ch.nolix.system.webgui.container.GridContainer;
import ch.nolix.system.webgui.control.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class RoomSession extends PlanningPokerSession {
	
	public static RoomSession withRoomId(final String roomId) {
		return new RoomSession(roomId);
	}
	
	private final String roomId;
	
	private RoomSession(final String roomId) {
		
		GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
		
		this.roomId = roomId;
	}
	
	@Override
	protected IControl<?, ?> createMainControl(final IDataController dataController) {
		
		final var room = dataController.getRefRoomById(roomId);
		
		return
		new VerticalStack()
		.addControl(
			new Label()
			.setText("Room " + room.getIdentification()),
			new Label()
			.setText(room.getRefParentCreator().getName() + " is our captain."),
			new HorizontalStack()
			.addControl(
				new VerticalStack()
				.addControl(
					createRoomCockpitControl(),
					createVisitControlForRoom(room)
				),
				createAnalysisControl()
			)
		);
	}
	
	private IControl<?, ?> createRoomCockpitControl() {
		return new HorizontalStack();
	}
	
	private IControl<?, ?> createVisitControlForRoom(final IRoom room) {
		
		final var roomVisitGridContainer = new GridContainer();
		
		var index = 1;
		for (final var v : room.getRefVisits()) {
			
			roomVisitGridContainer.insertControlAtRowAndColumn(
				index,
				1,
				new Label().setText(v.getRefVisitor().getName())
			);
			
			roomVisitGridContainer.insertControlAtRowAndColumn(
				index,
				2,
				createEstimationControlForVisit(v)
			);
			
			index++;
		}
		
		return roomVisitGridContainer;
	}
	
	private IControl<?, ?> createAnalysisControl() {
		return new VerticalStack();
	}
	
	private IControl<?, ?> createEstimationControlForVisit(final IRoomVisit visit) {
		
		final var estimationLabel = new Label();
		
		if (visit.hasEstimationInStorypoints()) {
			estimationLabel.setText(String.valueOf(visit.getEstimationInStoryPoints()));
		}
		
		return estimationLabel;
	}
}
