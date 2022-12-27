package ch.nolix.planningpoker.datamodel;

public final class RoomGlobalIdentificationGenerator {
	
	private static int counter = 1;
	
	public static String generateNextIdentification() {
		
		final var identification = String.valueOf(counter);
		
		counter++;
		
		return identification;
	}
	
	private RoomGlobalIdentificationGenerator() {}
}
