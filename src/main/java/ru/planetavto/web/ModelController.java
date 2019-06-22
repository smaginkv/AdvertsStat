package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.planetavto.advertsment.Model;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.ModelRepository;

@Controller
public class ModelController {
	@Autowired
	private ModelRepository modelRepo;
	
	@RequestMapping(value = "/model", method=RequestMethod.GET)
	public String goToAdvertListForm(Map<String,Object> model) {
		List<Model> models = modelRepo.findAll();
		model.put("models", models);
		return "model/modelListForm";
	}

	@RequestMapping(value ="/model/new",  method=RequestMethod.GET)
	public String newAdvert(Map<String,Object> model) {	
		model.put("model", new Model());
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value ="/model/new",  method=RequestMethod.POST)
	public String saveNewAdvert(Model model) {
		modelRepo.save(model, false);		
		return "redirect:/model";
	}
	
	@RequestMapping(value = "/model/{modelId}", method=RequestMethod.GET)
	public String modelListForm(@PathVariable String modelId, Map<String,Object> model) {
		Model carModel = modelRepo.findById(Long.parseLong(modelId));
		model.put("model", carModel);
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value = "/model/{modelId}", method=RequestMethod.POST)
	public String updateTargetModel(@PathVariable String modelId, Model model) {
		model.setId(Long.parseLong(modelId));
		modelRepo.save(model, true);
		return "redirect:/model";
	}	
}
