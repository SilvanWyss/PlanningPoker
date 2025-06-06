package ch.nolix.planningpoker.frontend.component.createusercomponent;

import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.IPokerSessionFactory;
import ch.nolix.planningpokerapi.frontendapi.sessionfactoryapi.ISelectRoomSessionFactory;
import ch.nolix.system.application.component.Component;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.button.Button;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.system.webgui.atomiccontrol.textbox.Textbox;
import ch.nolix.system.webgui.atomiccontrol.validationlabel.ValidationLabel;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.buttonapi.ButtonRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class CreateUserComponent extends Component<CreateUserController, IPlanningPokerService> {

  public CreateUserComponent(
    final WebClientSession<IPlanningPokerService> session,
    final ISelectRoomSessionFactory selectRoomSessionFactory,
    final IPokerSessionFactory pokerSessionFactory) {
    super(new CreateUserController(selectRoomSessionFactory, pokerSessionFactory), session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
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
}
