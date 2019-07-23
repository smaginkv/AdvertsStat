package ru.planetavto.web;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ru.planetavto.advertsment.Price;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.parsing.ParsingOkami;
import ru.planetavto.presistent.CarAdvertService;
import ru.planetavto.presistent.repo.ModelRepository;

@Controller
public class AdvertController {

	private CarAdvertService advertRepo;
	private ModelRepository modelRepo;
	private ParsingOkami parser;
	
	@Autowired
	public AdvertController(CarAdvertService advertRepo, ModelRepository modelRepo, ParsingOkami parser) {
		this.advertRepo = advertRepo;
		this.modelRepo  = modelRepo;
		this.parser = parser;
	}
	
	@RequestMapping(value = "/advert", method=RequestMethod.GET)
	public String getAdvertListForm(Model model, Pageable pageable) {
		Pageable newPageable = PageRequest.of(pageable.getPageNumber(), 8);
		
		Page<CarAdvert> page = advertRepo.findPage(newPageable);
		model.addAttribute("page", page);
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
	
	@RequestMapping(value = "/db_image/{advertId}", method=RequestMethod.GET) 
	public ResponseEntity<byte[]> getImage(@PathVariable("advertId") long advertId) {
        
		CarAdvert advert = advertRepo.findById(advertId);
		HttpStatus status = advert == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
		
		return new ResponseEntity<byte[]>(advert.getImage(), status);	
	} 
	
	@RequestMapping(value = "/advert_images/{advertId}/{imageId}", method=RequestMethod.GET) 
	public ResponseEntity<byte[]> getImages(@PathVariable("advertId") long advertId,
			@PathVariable("imageId") int imageId) {

		CarAdvert advert = advertRepo.findById(advertId);
		HttpStatus status = advert == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
		
		return new ResponseEntity<byte[]>(advert.getImages().get(imageId).getImage(), status);
	}
	
	@RequestMapping(value = "/advert/{advertId}", params={"saveAdvert"}, method=RequestMethod.POST)
	public String updateTargetAdvert(@PathVariable long advertId, CarAdvert advert) {
		advert.setId(advertId);
		advertRepo.save(advert);
		return "redirect:/advert";
	}
	
	@RequestMapping(value = "/advert/{advertId}", params={"deleteAdvert"}, method=RequestMethod.POST)
	public String deleteTargetAdvert(@PathVariable long advertId) {
		advertRepo.deleteById(advertId);
		return "redirect:/advert";
	}
	
	@RequestMapping(value = "/advert/{advertId}", params={"updateImages"}, method=RequestMethod.POST)
	public String updateAdvertUmages(@PathVariable long advertId) {
		parser.saveImagesByAdvert(advertId);		
		return "redirect:/advert";
	}
	
	@ModelAttribute("allModels")
	public List<ru.planetavto.advertsment.Model> populateModels() {
	    return modelRepo.findAll();
	}
	
	@RequestMapping(value="/advert/{advertId}", params={"addPrice"}, method=RequestMethod.POST)
	public String addPrice(CarAdvert advert, Map<String,Object> model) {
		advert.getPrices().add(new Price(0));
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
