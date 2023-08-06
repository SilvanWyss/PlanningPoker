package ch.nolix.planningpoker.webapplication.selectroomcomponent;

import ch.nolix.planningpoker.webapplication.view.PokerSession;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.Component;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Textbox;
import ch.nolix.system.webgui.atomiccontrol.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class SelectRoomComponent extends Component<SelectRoomController, IPlanningPokerContext> {
	
	public SelectRoomComponent(final String userId, final WebClientSession<IPlanningPokerContext> session) {
		super(new SelectRoomController(userId, session));
	}
	
	@Override
	protected IControl<?, ?> createControl(final SelectRoomController controller) {
		
		final var roomNumberTextbox = new Textbox();
		
		return
		new VerticalStack()
		.addControl(
			new VerticalStack()
			.addControl(
				new Button()
				.setText("Create new room")
				.setLeftMouseButtonPressAction(
					() ->
					controller.createAndEnterRoomAndRedirect(
						PokerSession::withUserIdAndRoomId
					)
				)
			),
			new ValidationLabel(),
			new HorizontalStack()
			.addControl(
				roomNumberTextbox,
				new Button()
				.setText("Enter room")
				.setLeftMouseButtonPressAction(
					() ->
					controller.enterRoomAndRedirect(
						roomNumberTextbox.getText(),
						PokerSession::withUserIdAndRoomId
					)
				)
			)
		);
	}
}
