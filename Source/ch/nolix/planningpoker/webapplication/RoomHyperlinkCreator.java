package ch.nolix.planningpoker.webapplication;

import ch.nolix.planningpokerapi.datamodelapi.schemaapi.IRoom;
import ch.nolix.planningpokerapi.webapplicationapi.IRoomTarget;
import ch.nolix.systemapi.applicationapi.mainapi.IApplication;

public final class RoomHyperlinkCreator {
	
	public IRoomTarget createRoomTarget(final IRoom room, final IApplication<?> application) {
		
		final var applicationTarget = application.asTarget();
		
		return
		RoomTarget.forIpOrAddressNameAndPortAndApplicationInstanceNameAndRoomNumberAndSecurityLevelForConnections(
			applicationTarget.getIpOrAddressName(),
			applicationTarget.getPort(),
			applicationTarget.getApplicationInstanceName(),
			room.getNumber(),
			applicationTarget.getSecurityLevelForConnections()
		);
	}
	
	public String createHyperlinkToRoom(final IRoom room, final IApplication<?> application) {
		
		final var roomTarget = createRoomTarget(room, application);
		
		return roomTarget.toURL();
	}
}
