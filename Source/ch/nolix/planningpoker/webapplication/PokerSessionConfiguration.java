package ch.nolix.planningpoker.webapplication;

import ch.nolix.core.errorcontrol.validator.GlobalValidator;

public record PokerSessionConfiguration(String userId, String roomId) {
	
	public PokerSessionConfiguration(final String userId, final String roomId) {
		
		GlobalValidator.assertThat(userId).thatIsNamed("user id").isNotBlank();
		GlobalValidator.assertThat(roomId).thatIsNamed("room id").isNotBlank();
		
		this.userId = userId;
		this.roomId = roomId;
	}
	
	public String getRoomId() {
		return roomId;
	}
	
	public String getUserId() {
		return userId;
	}
}
