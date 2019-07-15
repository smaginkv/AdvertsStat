package ru.planetavto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.parsing.ParsingOkami;
import ru.planetavto.presistent.CarAdvertService;

@Component
public class Scheduler {
 
	@Autowired
	private CarAdvertService advertRepo;
	
	@Autowired
	private ParsingOkami parser;
	
    @Scheduled(cron = "0 0 9,21 * *  ?")
    public void clearTempFolder() {

    	List<CarAdvert> advertList = parser.getAdvertList();
		for (CarAdvert advert : advertList) {
			advertRepo.save(advert);
		}
		
    }
}