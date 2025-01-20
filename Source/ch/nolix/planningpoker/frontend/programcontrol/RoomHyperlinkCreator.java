package ch.nolix.planningpoker.frontend.programcontrol;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.frontendapi.programcontrolapi.IRoomTarget;
import ch.nolix.systemapi.applicationapi.mainapi.IApplication;

public final class RoomHyperlinkCreator {

  public IRoomTarget createRoomTarget(final IRoom room, final IApplication<?> application) {

    final var applicationTarget = application.asTarget();

    return RoomTarget.forIpOrAddressNameAndPortAndApplicationInstanceNameAndRoomNumberAndSecurityLevelForConnections(
      applicationTarget.getIpOrDomain(),
      applicationTarget.getPort(),
      applicationTarget.getApplicationInstanceName(),
      applicationTarget.getApplicationUrlInstanceName(),
      room.getNumber(),
      applicationTarget.getSecurityModeForConnection());
  }

  public String createHyperlinkToRoom(final IRoom room, final IApplication<?> application) {

    final var roomTarget = createRoomTarget(room, application);

    return roomTarget.toUrl();
  }
}
