package ru.planetavto.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.planetavto.advertsment.Model;
import ru.planetavto.advertsment.Price;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.CarAdvertService;
import ru.planetavto.presistent.ModelRepository;

@Controller
public class AdvertController {

	@Autowired
	private CarAdvertService advertRepo;
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
		advertRepo.save(advert, false);
		return "redirect:/advert";
	}
	
	@RequestMapping(value = "/advert/{advertId}", method=RequestMethod.GET)
	public String goToTargetAdvert(@PathVariable long advertId, Map<String,Object> model, RedirectAttributes redirectAttributes) {
		try {
			CarAdvert advert = advertRepo.findById(advertId);
			model.put("advert", advert);
			return "advert/advertUnitForm";
		} catch (EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute("flash.message", e.getMessage());
			return "redirect:/advert";
		}				
		
	}
	
	@RequestMapping(value = "/img/{advertId}", method=RequestMethod.GET) 
	public ResponseEntity<byte[]> getImage(@PathVariable("advertId") long advertId) {
        
		CarAdvert advert = advertRepo.findById(advertId);
		HttpStatus status = advert != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		
		return new ResponseEntity<byte[]>(advert.getImage(), status);	
	} 
	
	@RequestMapping(value = "/advert/{advertId}", params={"saveAdvert"}, method=RequestMethod.POST)
	public String updateTargetAdvert(@PathVariable long advertId, CarAdvert advert) {
		advert.setId(advertId);
		advertRepo.save(advert, true);
		return "redirect:/advert";
	}	
	
	@ModelAttribute("allModels")
	public List<Model> populateModels() {
	    return modelRepo.findAll();
	}
	
	@RequestMapping(value="/advert/{advertId}", params={"addPrice"}, method=RequestMethod.POST)
	public String addPrice(CarAdvert advert, Map<String,Object> model) {
		advert.getPrices().add(new Price());
		model.put("advert", advert);
	    return "advert/advertUnitForm";
	}

	@RequestMapping(value="/advert/{advertId}", params={"removePrice"}, method=RequestMethod.POST)
	public String removePrice(CarAdvert advert, Map<String,Object> model, HttpServletRequest req) {
	    final Integer priceId = Integer.valueOf(req.getParameter("removePrice"));
	    advert.getPrices().remove(priceId.intValue());
	    model.put("advert", advert);
	    return "advert/advertUnitForm";
	}	
}
