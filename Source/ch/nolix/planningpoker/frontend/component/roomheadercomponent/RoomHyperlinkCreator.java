package ch.nolix.planningpoker.frontend.component.roomheadercomponent;

import ch.nolix.planningpokerapi.backendapi.datamodelapi.IRoom;
import ch.nolix.planningpokerapi.webapplicationapi.controllerapi.IRoomTarget;
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
