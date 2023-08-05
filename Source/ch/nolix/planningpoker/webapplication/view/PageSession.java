package ch.nolix.planningpoker.webapplication.view;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.coreapi.webapi.webproperty.LinkTarget;
import ch.nolix.planningpoker.webapplication.controller.PageController;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IDatabaseAdapter;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.webapplication.WebClientSession;
import ch.nolix.system.graphic.image.Image;
import ch.nolix.system.webgui.atomiccontrol.Button;
import ch.nolix.system.webgui.atomiccontrol.ImageControl;
import ch.nolix.system.webgui.atomiccontrol.Label;
import ch.nolix.system.webgui.atomiccontrol.Link;
import ch.nolix.system.webgui.container.SingleContainer;
import ch.nolix.system.webgui.linearcontainer.HorizontalStack;
import ch.nolix.system.webgui.linearcontainer.VerticalStack;
import ch.nolix.systemapi.graphicapi.imageapi.IImage;
import ch.nolix.systemapi.webguiapi.atomiccontrolapi.LabelRole;
import ch.nolix.systemapi.webguiapi.basecontainerapi.ContainerRole;
import ch.nolix.systemapi.webguiapi.containerapi.ISingleContainer;
import ch.nolix.systemapi.webguiapi.mainapi.IControl;

public abstract class PageSession extends WebClientSession<IPlanningPokerContext> {
	
	private static final String GITHUB_LOGO_RESOURCE_PATH = "ch/nolix/planningpoker/resource/github_logo.jpg";
	
	private static final IImage GITHUB_LOGO =
	Image.fromResource(GITHUB_LOGO_RESOURCE_PATH)
	.withWidthAndHeight(100, 100);
	
	private static final String PLANNING_POKER_SOURCE_CODE_ON_GITHUB_URL = "https://github.com/Nimeon/PlanningPoker";
	
	private static final String NOLIX_WEB_GUIS_LINK = "https://nolix.ch/nolix_web-guis.html";
	
	private static final PageController PAGE_SESSION_HELPER = new PageController();
	
	private static final PlanningPokerStyleCreator PAGE_SESSION_STYLE_CREATOR = new PlanningPokerStyleCreator();
		
	private final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer;
	
	private final ISingleContainer rootControl = new SingleContainer();
	
	protected PageSession(
		final ch.nolix.coreapi.containerapi.singlecontainerapi.ISingleContainer<String> userIdContainer
	) {
		
		GlobalValidator.assertThat(userIdContainer).thatIsNamed("user id container").isNotNull();
		
		this.userIdContainer = userIdContainer;
	}
	
	protected abstract IControl<?, ?> createMainControl(IDatabaseAdapter databaseAdapter);
	
	protected abstract void doRegistrations(IRoomChangeNotifier roomChangeNotifier);
	
	protected final String getUserId() {
		return userIdContainer.getStoredElement();
	}
	
	protected final boolean hasUserId() {
		return userIdContainer.containsAny();
	}
	
	@Override
	protected final void initialize() {
		
		doRegistrations(getStoredApplicationContext().getStoredRoomChangeNotifier());
		
		getStoredGui()
		.pushLayerWithRootControl(rootControl)
		.setStyle(PAGE_SESSION_STYLE_CREATOR.createPageSessionStyle())
		.setRemoveLayerAction(this::noteSelfChange);
		
		fillUpRootControl();
	}
	
	protected abstract void noteSelfChange();
	
	protected final void refreshIfDoesNotHaveOpenDialog() {
		if (!hasOpenDialog()) {
			refreshActually();
		}
	}
	
	private void fillUpRootControl() {
		try (final var databaseAdapter = getStoredApplicationContext().createDatabaseAdapter()) {
			rootControl
			.setControl(
				new VerticalStack()
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
							new HorizontalStack()
							.setVisibility(hasUserId())
							.addControl(
								new Label()
								.setText("you: "),
								new Button()
								.setText(PAGE_SESSION_HELPER.getUserName(userIdContainer, databaseAdapter))
								.setLeftMouseButtonPressAction(
									() -> PAGE_SESSION_HELPER.openEditUserNameDialog(getUserId(), this)
								)
							)
						)
					),
					new SingleContainer()
					.setRole(ContainerRole.MAIN_CONTENT_CONTAINER)
					.setControl(createMainControl(databaseAdapter)),
					new HorizontalStack()
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
							.setUrl(PLANNING_POKER_SOURCE_CODE_ON_GITHUB_URL)
						),
						new HorizontalStack()
						.addControl(
							new Label()
							.setText("developped with"),
							new Link()
							.setDisplayText("Nolix Web-GUIs")
							.setTarget(LinkTarget.NEW_TAB)
							.setUrl(NOLIX_WEB_GUIS_LINK)
						)
					)
				)
			);
		}
	}
	
	private boolean hasOpenDialog() {
		return (getStoredGui().getStoredLayers().getElementCount() > 1);
	}
	
	private void refreshActually() {
		
		fillUpRootControl();
		
		updateCounterpart();
	}
}
