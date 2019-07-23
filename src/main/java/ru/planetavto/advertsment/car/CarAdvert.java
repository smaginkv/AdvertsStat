package ru.planetavto.advertsment.car;

import java.io.Serializable;
import java.time.LocalDate;
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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import ru.planetavto.advertsment.AdvertImage;
import ru.planetavto.advertsment.Model;
import ru.planetavto.advertsment.ParsingPlan;
import ru.planetavto.advertsment.Price;

@Entity
@Data
public class CarAdvert implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(columnDefinition="Decimal(2,1)")
	private float engineCapacity;
	
	private int productionYear;
	
	@Lob
	@Column(name="image")
	private byte[] image;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "advert_prices", joinColumns = @JoinColumn(name = "advert_id", nullable = false))
	private List<Price> prices = new ArrayList<>();
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(joinColumns = @JoinColumn(name = "advert_id", nullable = false))
	private List<AdvertImage> images = new ArrayList<>();

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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate changeActivityDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	
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
		createDate = LocalDate.now();
	}
	
	public int getLastPrice() {
		if (prices.isEmpty()) {
			return 0;
		}else {
			return prices.get(prices.size() - 1).getPrice();			
		}		
	}
}
