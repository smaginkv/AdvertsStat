package ru.planetavto.advertsment;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ru.planetavto.advertsment.car.CarAdvert;

@Entity
public class Price {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private CarAdvert advert;
	
	@Column(columnDefinition="Decimal(15,2)")
	private int price;
	
	private LocalDate date;  
	
	public Long getId() {
		return id;
	}
	public CarAdvert getAdvert() {
		return advert;
	}
	public int getPrice() {
		return price;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setAdvert(CarAdvert advert) {
		this.advert = advert;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
