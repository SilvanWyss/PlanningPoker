package ch.nolix.planningpokertest.datamodeltest;

import org.junit.jupiter.api.Test;

import ch.nolix.core.document.node.MutableNode;
import ch.nolix.core.testing.validation.GlobalExaminer;
import ch.nolix.planningpoker.datamodel.Estimation;
import ch.nolix.planningpoker.datamodel.EstimationRound;
import ch.nolix.planningpoker.datamodel.Room;
import ch.nolix.planningpoker.datamodel.RoomVisit;
import ch.nolix.planningpoker.datamodel.User;
import ch.nolix.system.objectdatabase.databaseadapter.NodeDatabaseAdapter;
import ch.nolix.system.objectdatabase.schema.Schema;

public final class DataModelTest {
	
	@Test
	public void testCase_createRoomWithVisitors() {
		
		//setup
		final var database = new MutableNode();
		final var schema =
		Schema.withEntityType(Estimation.class, EstimationRound.class, Room.class, RoomVisit.class, User.class);
		final var databaseAdapter =
		NodeDatabaseAdapter.forNodeDatabase(database).withName("TestDatabase").usingSchema(schema);
		final var scrumMaster = User.withName("Scrum Master");
		databaseAdapter.insert(scrumMaster);
		final var developer1 = User.withName("Developer 1");
		databaseAdapter.insert(developer1);
		final var developer2 = User.withName("Developer 2");
		databaseAdapter.insert(developer2);
		final var developer3 = User.withName("Developer 3");
		databaseAdapter.insert(developer3);
		final var room = Room.fromParentCreator(scrumMaster);
		databaseAdapter.insert(room);
		
		//execution & verification
		GlobalExaminer.expect(databaseAdapter::saveChangesAndReset).doesNotThrowException();
	}
}
