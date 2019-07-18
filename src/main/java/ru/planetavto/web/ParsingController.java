package ru.planetavto.web;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.planetavto.parsing.ParsingOkami;

@Controller
@RequestMapping("/parse")
public class ParsingController {
	
	@Autowired
	private ParsingOkami parser;

	@GetMapping
	public String registerForm() {
		return "parsing";
	}

	@PostMapping
	public String parse() throws IOException, SQLException {
		parser.parseAdvertsByAllPlans();		
		return "parsing";
		
	}
}
