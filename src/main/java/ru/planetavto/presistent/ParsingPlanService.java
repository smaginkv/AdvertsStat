package ru.planetavto.presistent;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.planetavto.parsing.ParsingPlan;

@Service
public class ParsingPlanService {
	private ParsingPlanRepository repository;

	@Autowired
	public ParsingPlanService(ParsingPlanRepository repository) {
		this.repository = repository;
	}
	
	public List<ParsingPlan> findAll() {
		return repository.findAll();
	}
	
	public List<ParsingPlan> findAllActive() {
		return repository.findAllByActive(true);
	}
	
	public ParsingPlan save(ParsingPlan parsingPlan) {
		repository.save(parsingPlan);
		return parsingPlan;
	}
	
	public ParsingPlan findById(long parsingPlanId) {

		ParsingPlan plan = repository.findById(parsingPlanId);
		if (plan == null) {
			throw new EntityNotFoundException("Can't find parsing plan for ID " + parsingPlanId);
		}
		return plan;
	}

}
