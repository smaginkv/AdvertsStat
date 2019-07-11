package ru.planetavto.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.parsing.ParsingOkami;
import ru.planetavto.presistent.CarAdvertService;

@Controller
@RequestMapping("/parse")
public class ParsingController {

	@Autowired
	private CarAdvertService advertRepo;
	
	@Autowired
	private ParsingOkami parser;

	@GetMapping
	public String registerForm() {
		return "parsing";
	}

	@PostMapping
	public String parse() throws IOException, SQLException {
		
		List<CarAdvert> advertList = parser.getAdvertList();
		for (CarAdvert advert : advertList) {
			advertRepo.save(advert);
		}		
		return "parsing";
	}
}
