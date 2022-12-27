package ch.nolix.planningpoker.datamodel;

import ch.nolix.core.errorcontrol.invalidargumentexception.InvalidArgumentException;
import ch.nolix.coreapi.containerapi.mainapi.IContainer;
import ch.nolix.planningpoker.analysis.EstimationRoundAnalysis;
import ch.nolix.planningpokerapi.analysisapi.IEstimationRoundAnalysis;
import ch.nolix.planningpokerapi.datamodelapi.IEstimation;
import ch.nolix.planningpokerapi.datamodelapi.IEstimationRound;
import ch.nolix.system.objectdatabase.database.Entity;
import ch.nolix.system.objectdatabase.database.MultiReference;

public final class EstimationRound extends Entity implements IEstimationRound {
	
	public static EstimationRound withEstimations(final IContainer<Estimation> estimations) {
		
		final var estimationRound = new EstimationRound();
		estimationRound.setEstimations(estimations);
		
		return estimationRound;
	}
	
	private final MultiReference<Estimation> estimations = MultiReference.forEntity(Estimation.class);
	
	private EstimationRound() {
		initialize();
	}
	
	@Override
	public IEstimationRoundAnalysis getAnalysis() {
		return EstimationRoundAnalysis.forEstimationsInStoryPoints(getEstimationsInStoryPoints());
	}
	
	@Override
	public IContainer<? extends IEstimation> getRefEstimations() {
		return estimations.getReferencedEntities();
	}
	
	private void assertDoesNotContainEstimations() {
		if (containsEstimations()) {
			throw InvalidArgumentException.forArgumentAndErrorPredicate(this, "contains already estimations");
		}
	}
	
	private boolean containsEstimations() {
		return estimations.containsAny();
	}
	
	private IContainer<Double> getEstimationsInStoryPoints() {
		return getRefEstimations().to(IEstimation::getStoryPoints);
	}
	
	private void setEstimations(final IContainer<Estimation> estimations) {
		
		assertDoesNotContainEstimations();
		
		estimations.forEach(this.estimations::addEntity);
	}
}
