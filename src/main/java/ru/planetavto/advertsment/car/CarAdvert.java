package ru.planetavto.advertsment.car;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;
import ru.planetavto.advertsment.Model;
import ru.planetavto.advertsment.Price;

@Entity
@Data
public class CarAdvert {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition="Decimal(2,1)")
	private float engineCapacity;
	
	private int productionYear;
	
	@Lob
	@Column(name="image")
	private byte[] image;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "advert_prices", joinColumns = @JoinColumn(name = "advert_id", nullable = false))
	private List<Price> prices = new ArrayList<>();

	@ManyToOne
	private Model model;	
	private String cityOfSale;
	private boolean activity;	
	private int horsepower;
	private String description;
	private byte ownersNumber;
	private boolean airConditioning;
	private boolean esp;
	private int mileage;	
}
