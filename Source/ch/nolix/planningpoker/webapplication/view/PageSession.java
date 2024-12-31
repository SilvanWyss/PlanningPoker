package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.planningpoker.webapplication.footercomponent.FooterComponent;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDataAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.webgui.atomiccontrol.label.Label;
import ch.nolix.system.webgui.container.SingleContainer;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.labelapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends WebClientSession<IPlanningPokerContext> {

  protected abstract IControl<?, ?> createMainControl(IDataAdapter dataAdapter);

  protected abstract IControl<?, ?> createUserProfileControl(IDataAdapter dataAdapter);

  @Override
  protected final void initialize() {
    getStoredGui()
      .pushLayerWithRootControl(createRootControl())
      .setStyle(PlanningPokerStyleCatalogue.DARK_MODE_STYLE);
  }

  private IControl<?, ?> createRootControl() {
    try (final var databaseAdapter = getStoredApplicationContext().createDataSupplier()) {
      return new VerticalStack()
        .setRole(ContainerRole.OVERALL_CONTAINER)
        .addControl(
          new VerticalStack()
            .addControl(
              new HorizontalStack()
                .setRole(ContainerRole.HEADER_CONTAINER)
                .addControl(
                  new Label()
                    .setRole(LabelRole.TITLE)
                    .setText(getApplicationName()),
                  createUserProfileControl(databaseAdapter))),
          new SingleContainer()
            .setRole(ContainerRole.MAIN_CONTENT_CONTAINER)
            .setControl(createMainControl(databaseAdapter)),
          new FooterComponent(this));
    }
  }
}
