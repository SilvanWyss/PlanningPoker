package ch.nolix.planningpoker.webapplication.roomanalysiscomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IPlanningPokerContext;
import ch.nolix.planningpokerapi.logicapi.applicationcontextapi.IRoomChangeNotifier;
import ch.nolix.system.application.component.Controller;

final class RoomAnalysisController extends Controller<IPlanningPokerContext> {

  private final String roomId;

  protected RoomAnalysisController(final String roomId) {

    GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();

    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }

  public IRoomChangeNotifier getStoredRoomChangeNotifier() {
    return getStoredApplicationContext().getStoredRoomChangeNotifier();
  }
}
