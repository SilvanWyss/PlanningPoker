package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.invalidargumentexception.InvalidArgumentException;
import ch.nolix.coreapi.containerapi.baseapi.IContainer;
import ch.nolix.planningpokerapi.datamodelapi.IEstimate;
import ch.nolix.planningpokerapi.datamodelapi.IEstimateRound;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.MultiReference;

public final class EstimateRound extends Entity implements IEstimateRound {
	
	public static EstimateRound withEstimates(final IContainer<Estimate> estimates) {
		
		final var estimateRound = new EstimateRound();
		estimateRound.setEstimates(estimates);
		
		return estimateRound;
	}
	
	private final MultiReference<Estimate> estimates = MultiReference.forEntity(Estimate.class);
	
	private EstimateRound() {
		initialize();
	}
	
	@Override
	public IContainer<? extends IEstimate> getOriEstimates() {
		return estimates.getReferencedEntities();
	}
	
	private void assertDoesNotContainEstimates() {
		if (containsEstimates()) {
			throw InvalidArgumentException.forArgumentAndErrorPredicate(this, "contains already estimates");
		}
	}
	
	private boolean containsEstimates() {
		return estimates.containsAny();
	}
	
	private void setEstimates(final IContainer<Estimate> estimates) {
		
		assertDoesNotContainEstimates();
		
		estimates.forEach(this.estimates::addEntity);
	}
}
