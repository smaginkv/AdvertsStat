package ru.planetavto.parsing;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import ru.planetavto.advertsment.Model;

@Entity
@Data
public class ParsingPlan {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Model model;
	
	@Column(length = 1000)
	private String url;
	private boolean active;
	
	private String pathToAdvert;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime lastInvoke;
	
	@Override
	public String toString() {
		return ""+model.getBrand() + " " + model.getTitle()+" "+(active?"(active)":"(inactive)");
	}
	
	public String getExtendRef(String ref) {
		return pathToAdvert+ref;		
	}
}
