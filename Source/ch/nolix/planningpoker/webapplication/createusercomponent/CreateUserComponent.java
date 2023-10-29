package ch.nolix.planningpoker.webapplication.createusercomponent;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.webapplicationapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.Component;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.atomiccontrol.Textbox;
import ch.nolix.system.webgui.atomiccontrol.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserComponent extends Component<CreateUserController, IPlanningPokerContext> {

  public CreateUserComponent(
    final WebClientSession<IPlanningPokerContext> session,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {
    super(new CreateUserController(session, selectRoomSessionFactory, pokerSessionFactory));
  }

  @Override
  protected IControl<?, ?> createControl(final CreateUserController controller) {

    final var userNameTextbox = new Textbox();

    return new VerticalStack()
      .addControl(
        new ValidationLabel(),
        new HorizontalStack()
          .addControl(
            new Label()
              .setText("Enter user name:"),
            userNameTextbox,
            new Button()
              .setRole(ButtonRole.CONFIRM_BUTTON)
              .setText("Ok")
              .setLeftMouseButtonPressAction(
                () -> controller.createUserAndSetCookieAndRedirect(userNameTextbox.getText()))));
  }

  @Override
  protected void doRegistrations(final CreateUserController controller) {
    //Does nothing.
  }
}
