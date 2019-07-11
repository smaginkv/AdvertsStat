package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.planetavto.parsing.ParsingPlan;
import ru.planetavto.presistent.ModelRepository;
import ru.planetavto.presistent.ParsingPlanService;

@Controller
@RequestMapping(value = "/plan")
public class ParsingPlanController {	
	
	private ParsingPlanService planRepo;
	private ModelRepository modelRepo;
	
	@Autowired
	public ParsingPlanController(ParsingPlanService planRepo, ModelRepository modelRepo) {
		this.planRepo = planRepo;
		this.modelRepo = modelRepo;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getPlanListForm(Map<String,Object> model) {
		List<ParsingPlan> plans = planRepo.findAll();
		model.put("plans", plans);
		return "plan/parsingPlanListForm";
	}
	
	@RequestMapping(value ="/new",  method=RequestMethod.GET)
	public String newPlan(Map<String,Object> model) {	
		model.put("plan", new ParsingPlan());
		return "plan/parsingPlanUnitForm";
	}
	
	@RequestMapping(value ="/new",  method=RequestMethod.POST)
	public String saveNewPlan(ParsingPlan parsingPlan) {
		planRepo.save(parsingPlan);
		return "redirect:/plan";
	}
	
	@RequestMapping(value = "/{planId}", method=RequestMethod.GET)
	public String getPlanUnitForm(@PathVariable String planId, Map<String,Object> model) {
		ParsingPlan plan = planRepo.findById(Long.parseLong(planId));
		model.put("plan", plan);
		return "plan/parsingPlanUnitForm";
	}
	
	@RequestMapping(value = "/{planId}", method=RequestMethod.POST)
	public String updateTargetModel(@PathVariable String planId, ParsingPlan plan) {
		plan.setId(Long.parseLong(planId));
		planRepo.save(plan);
		return "redirect:/plan";
	}
	
	@ModelAttribute("allModels")
	public List<ru.planetavto.advertsment.Model> populateModels() {
	    return modelRepo.findAll();
	}

}
