package ru.planetavto.advertsment;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Embeddable
@Data
public class AdvertImage {	
	private String title;
	
	@Lob
	private byte[] image;

}
