package ru.planetavto.presistent;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.planetavto.parsing.ParsingPlan;


public interface ParsingPlanRepository extends CrudRepository<ParsingPlan, Long> {
	public ParsingPlan findById(long id);
	
	public List<ParsingPlan> findAll();
	
	public List<ParsingPlan> findAllByActive(boolean active);
}
