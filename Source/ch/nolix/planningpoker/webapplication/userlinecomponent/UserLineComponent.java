package ch.nolix.planningpoker.webapplication.userlinecomponent;

import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.ComponentWithDataSupplier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.button.Button;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class UserLineComponent
extends ComponentWithDataSupplier<UserLineController, IPlanningPokerContext, IDataAdapter> {

  public UserLineComponent(
    final String userId,
    final WebClientSession<IPlanningPokerContext> session,
    final IDataAdapter initialDataAdapter) {
    super(new UserLineController(userId), initialDataAdapter, session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(
    final UserLineController userLineController,
    final IDataAdapter dataAdapter) {
    return new HorizontalStack()
      .addControl(
        new Label()
          .setText("you: "),
        new Button()
          .setText(userLineController.getLoggedInUserName(dataAdapter))
          .setLeftMouseButtonPressAction(userLineController::openEditUserNameDialog));
  }
}
