package ch.nolix.planningpoker.webapplication.roomheadercomponent;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.net.target.ApplicationInstanceTarget;
import ch.nolix.coreapi.netapi.securityapi.SecurityLevel;
import ch.nolix.planningpokerapi.webapplicationapi.controllerapi.IRoomTarget;

public final class RoomTarget extends ApplicationInstanceTarget implements IRoomTarget {

  private final String roomNumber;

  private RoomTarget(
    final String ipOrAddressName,
    final int port,
    final String applicationName,
    final String applicationUrlInstanceName,
    final String roomNumber,
    final SecurityLevel securityLevelForConnections) {

    super(ipOrAddressName, port, applicationName, applicationUrlInstanceName, securityLevelForConnections);

    GlobalValidator.assertThat(roomNumber).thatIsNamed("room number").isNotBlank();

    this.roomNumber = roomNumber;
  }

  public static RoomTarget //
  forIpOrAddressNameAndPortAndApplicationInstanceNameAndRoomNumberAndSecurityLevelForConnections(
    final String ipOrAddressName,
    final int port,
    final String applicationName,
    final String applicationUrlInstanceName,
    final String roomNumber,
    final SecurityLevel securityLevelForConnections) {
    return new RoomTarget(
      ipOrAddressName,
      port,
      applicationName,
      applicationUrlInstanceName,
      roomNumber,
      securityLevelForConnections);
  }

  @Override
  public String getRoomNumber() {
    return roomNumber;
  }

  @Override
  public String toUrl() {
    return (super.toUrl() + "&room=" + getRoomNumber());
  }
}