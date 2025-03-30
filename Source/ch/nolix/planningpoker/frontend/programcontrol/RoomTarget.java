package ch.nolix.planningpoker.frontend.programcontrol;

import ch.nolix.core.errorcontrol.validator.Validator;
import ch.nolix.core.net.target.ApplicationInstanceTarget;
import ch.nolix.coreapi.netapi.securityproperty.SecurityMode;
import ch.nolix.planningpokerapi.frontendapi.programcontrolapi.IRoomTarget;

public final class RoomTarget extends ApplicationInstanceTarget implements IRoomTarget {

  private final String roomNumber;

  private RoomTarget(
    final String ipOrAddressName,
    final int port,
    final String applicationName,
    final String applicationUrlInstanceName,
    final String roomNumber,
    final SecurityMode securityModeForConnections) {

    super(ipOrAddressName, port, applicationName, applicationUrlInstanceName, securityModeForConnections);

    Validator.assertThat(roomNumber).thatIsNamed("room number").isNotBlank();

    this.roomNumber = roomNumber;
  }

  public static RoomTarget //
  forIpOrAddressNameAndPortAndApplicationInstanceNameAndRoomNumberAndSecurityLevelForConnections(
    final String ipOrAddressName,
    final int port,
    final String applicationName,
    final String applicationUrlInstanceName,
    final String roomNumber,
    final SecurityMode securityModeForConnections) {
    return new RoomTarget(
      ipOrAddressName,
      port,
      applicationName,
      applicationUrlInstanceName,
      roomNumber,
      securityModeForConnections);
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
