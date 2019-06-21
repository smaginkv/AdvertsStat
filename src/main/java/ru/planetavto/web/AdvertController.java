package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.planetavto.advertsment.Model;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.CarAdvertRepository;
import ru.planetavto.presistent.ModelRepository;

@Controller
public class AdvertController {

	@Autowired
	private CarAdvertRepository advertRepo;
	@Autowired
	private ModelRepository modelRepo;
	
	@RequestMapping(value = "/advert", method=RequestMethod.GET)
	public String goToAdvertListForm(Map<String,Object> model) {
		List<CarAdvert> adverts = advertRepo.findAll();
		model.put("adverts", adverts);
		return "advert/advertListForm";
	}

	@RequestMapping(value ="/advert/new",  method=RequestMethod.GET)
	public String newAdvert(Map<String,Object> model) {	
		model.put("advert", new CarAdvert());
		return "advert/advertUnitForm";
	}
	
	@RequestMapping(value ="/advert/new",  method=RequestMethod.POST)
	public String saveNewAdvert(CarAdvert advert) {
		advertRepo.save(advert);		
		return "redirect:/advert";
	}
	
	@RequestMapping(value = "/advert/{advertId}", method=RequestMethod.GET)
	public String advertListForm(@PathVariable String advertId, Map<String,Object> model) {
		CarAdvert advert = advertRepo.findById(Long.parseLong(advertId));
		model.put("advert", advert);
		return "advert/advertUnitForm";
	}
	
	@ModelAttribute("allModels")
	public List<Model> populateModels() {
	    return modelRepo.findAll();
	}
	
}
