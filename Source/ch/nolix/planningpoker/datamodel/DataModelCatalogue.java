package ch.nolix.planningpoker.datamodel;

import ch.nolix.system.objectdatabase.schema.Schema;
import ch.nolix.systemapi.objectdatabaseapi.schemaapi.ISchema;

public final class DataModelCatalogue {
	
	public static final ISchema SCHEMA =
	Schema.withEntityType(Estimate.class, EstimateRound.class, Room.class, RoomVisit.class, User.class);
	
	private DataModelCatalogue() {}
}
