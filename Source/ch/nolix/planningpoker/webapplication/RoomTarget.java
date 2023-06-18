package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;
import ch.nolix.core.net.target.ApplicationInstanceTarget;
import ch.nolix.coreapi.programcontrolapi.processproperty.SecurityLevel;
import ch.nolix.planningpokerapi.webapplicationapi.IRoomTarget;

public final class RoomTarget extends ApplicationInstanceTarget implements IRoomTarget {
	
	public static RoomTarget
	forIpOrAddressNameAndPortAndApplicationInstanceNameAndRoomNumberAndSecurityLevelForConnections(
		final String ipOrAddressName,
		final int port,
		final String applicationName,
		final String roomNumber,
		final SecurityLevel securityLevelForConnections
	) {
		return new RoomTarget(ipOrAddressName, port, applicationName, roomNumber, securityLevelForConnections);
	}
	
	private final String roomNumber;
	
	private RoomTarget(
		final String ipOrAddressName,
		final int port,
		final String applicationName,
		final String roomNumber,
		final SecurityLevel securityLevelForConnections
	) {
		
		super(ipOrAddressName, port, applicationName, securityLevelForConnections);
		
		GlobalValidator.assertThat(roomNumber).thatIsNamed("room number").isNotBlank();
		
		this.roomNumber = roomNumber;
	}
	
	@Override
	public String getRoomNumber() {
		return roomNumber;
	}
	
	@Override
	public String toURL() {
		return (super.toURL() + "&room=" + getRoomNumber());
	}
}
