package ch.nolix.planningpoker.frontend.component.roomanalysiscomponent;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IPlanningPokerService;
import ch.nolix.planningpokerapi.frontendapi.mainapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;

final class RoomAnalysisController extends Controller<IPlanningPokerService> {

  private final String roomId;

  protected RoomAnalysisController(final String roomId) {

    Validator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }

  public IRoomChangeNotifier getStoredRoomChangeNotifier() {
    return getStoredApplicationService().getStoredRoomChangeNotifier();
  }
}
