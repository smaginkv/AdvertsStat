package ru.planetavto.advertsment.car;

import java.io.Serializable;
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
import ru.planetavto.parsing.ParsingPlan;

@Entity
@Data
public class CarAdvert implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition="Decimal(2,1)")
	private float engineCapacity;
	
	private int productionYear;
	
	@Lob
	@Column(name="image")
	private byte[] image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "advert_prices", joinColumns = @JoinColumn(name = "advert_id", nullable = false))
	private List<Price> prices = new ArrayList<>();

	@ManyToOne
	private Model model;
	
	@ManyToOne
	private ParsingPlan plan;
	
	private String cityOfSale;
	private boolean activity;	
	private int horsepower;
	private String description;
	private byte ownersNumber;
	private boolean airConditioning;
	private boolean esp;
	private int mileage;
	private String ref;
	
	public boolean lastPriceEquals(int price) {
		if(prices.isEmpty()) {
			return false;
		}else if (prices.get(prices.size() - 1).getPrice() == price) {
			return true;
		}
		return false;	
	}

	public CarAdvert() {
		super();
		activity = true;
	}
	
	public int getLastPrice() {
		if (prices.isEmpty()) {
			return 0;
		}else {
			return prices.get(prices.size() - 1).getPrice();			
		}		
	}
}
