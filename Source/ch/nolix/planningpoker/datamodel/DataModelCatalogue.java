package ch.nolix.planningpoker.datamodel;

import ch.nolix.system.objectdatabase.database.DataImplementation;
import ch.nolix.system.objectdatabase.schema.Schema;
import ch.nolix.systemapi.objectdatabaseapi.schemaapi.ISchema;

public final class DataModelCatalogue {
	
	public static final ISchema<DataImplementation> SCHEMA =
	Schema.withEntityType(Estimation.class, EstimationRound.class, Room.class, RoomVisit.class, User.class);
	
	private DataModelCatalogue() {}
}
