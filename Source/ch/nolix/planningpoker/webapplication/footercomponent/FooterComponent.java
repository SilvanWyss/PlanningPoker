package ch.nolix.planningpoker.webapplication.footercomponent;

import ch.nolix.coreapi.webapi.webproperty.LinkTarget;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.system.application.component.Component;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.graphic.image.Image;
import ch.nolix.system.webgui.atomiccontrol.ImageControl;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.atomiccontrol.Link;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.systemapi.applicationapi.componentapi.RefreshBehavior;
import ch.nolix.systemapi.graphicapi.imageapi.IImage;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public final class FooterComponent extends Component<FooterController, IPlanningPokerContext> {

  private static final String GITHUB_LOGO_RESOURCE_PATH = "ch/nolix/planningpoker/resource/github_logo.jpg";

  private static final IImage GITHUB_LOGO = Image.fromResource(GITHUB_LOGO_RESOURCE_PATH)
    .withWidthAndHeight(100, 100);

  private static final String PLANNING_POKER_SOURCE_CODE_ON_GITHUB_URL = "https://github.com/Nimeon/PlanningPoker";

  private static final String NOLIX_WEB_APPLICATIONS_LINK = "https://nolix.ch/nolix_web_applications.html";

  public FooterComponent(final WebClientSession<IPlanningPokerContext> session) {
    super(new FooterController(), session);
  }

  @Override
  public RefreshBehavior getRefreshBehavior() {
    return RefreshBehavior.REFRESH_SELF;
  }

  @Override
  protected IControl<?, ?> createControl(final FooterController controller) {
    return new HorizontalStack()
      .setRole(ContainerRole.FOOTER_CONTAINER)
      .addControl(
        new Label()
          .setText("Copyright © 2023 Silvan Wyss"),
        new HorizontalStack()
          .addControl(
            new ImageControl()
              .setImage(GITHUB_LOGO),
            new Link()
              .setDisplayText("source code on GitHub")
              .setTarget(LinkTarget.NEW_TAB)
              .setUrl(PLANNING_POKER_SOURCE_CODE_ON_GITHUB_URL)),
        new HorizontalStack()
          .addControl(
            new Label()
              .setText("developped with"),
            new Link()
              .setDisplayText("Nolix Web-GUIs")
              .setTarget(LinkTarget.NEW_TAB)
              .setUrl(NOLIX_WEB_APPLICATIONS_LINK)));
  }
}
