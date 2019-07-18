package ru.planetavto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.planetavto.parsing.ParsingOkami;

@Component
public class Scheduler {
	
	@Autowired
	private ParsingOkami parser;
	
    @Scheduled(cron = "0 0 9,21 * *  ?")
    public void clearTempFolder() {
    	parser.parseAdvertsByAllPlans();
    	
    }
}