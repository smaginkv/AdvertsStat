package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.planetavto.advertsment.Model;
import ru.planetavto.presistent.ModelRepository;

@Controller
@RequestMapping(value = "/model")
public class ModelController {
	
	private ModelRepository modelRepo;
	
	@Autowired	
	public ModelController(ModelRepository modelRepo) {
		this.modelRepo = modelRepo;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String goToAdvertListForm(Map<String,Object> model) {
		List<Model> models = modelRepo.findAll();
		model.put("models", models);
		return "model/modelListForm";
	}

	@RequestMapping(value ="/new",  method=RequestMethod.GET)
	public String newAdvert(Map<String,Object> model) {	
		model.put("model", new Model());
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value ="/new",  method=RequestMethod.POST)
	public String saveNewAdvert(Model model) {
		modelRepo.save(model, false);
		return "redirect:/model";
	}
	
	@RequestMapping(value = "/{modelId}", method=RequestMethod.GET)
	public String modelListForm(@PathVariable String modelId, Map<String,Object> model) {
		Model carModel = modelRepo.findById(Long.parseLong(modelId));
		model.put("model", carModel);
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value = "/{modelId}", method=RequestMethod.POST)
	public String updateTargetModel(@PathVariable String modelId, Model model) {
		model.setId(Long.parseLong(modelId));
		modelRepo.save(model, true);
		return "redirect:/model";
	}	
}
