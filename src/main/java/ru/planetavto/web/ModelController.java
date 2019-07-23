package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.planetavto.advertsment.Model;
import ru.planetavto.presistent.repo.ModelRepository;

@Controller
@RequestMapping(value = "/model")
public class ModelController {
	
	private ModelRepository modelRepo;
	
	@Autowired	
	public ModelController(ModelRepository modelRepo) {
		this.modelRepo = modelRepo;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getModelListForm(Map<String,Object> model) {
		List<Model> models = modelRepo.findAll();
		model.put("models", models);
		return "model/modelListForm";
	}

	@RequestMapping(value ="/new",  method=RequestMethod.GET)
	public String newModel(Map<String,Object> model) {	
		model.put("model", new Model());
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value ="/new",  method=RequestMethod.POST)
	public String saveNewAdvert(Model model) {
		modelRepo.save(model, false);
		return "redirect:/model";
	}
	
	@RequestMapping(value = "/{modelId}", method=RequestMethod.GET)
	public String getModelUnitForm(@PathVariable String modelId, Map<String,Object> model) {
		Model carModel = modelRepo.findById(Long.parseLong(modelId));
		model.put("model", carModel);
		return "model/modelUnitForm";
	}
	
	@RequestMapping(value = "/{modelId}", method=RequestMethod.POST)
	public String updateTargetModel(@PathVariable String modelId, Model model) {
		modelRepo.save(model, true);
		return "redirect:/model";
	}	
}
