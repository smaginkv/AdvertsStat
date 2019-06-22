package ru.planetavto.advertsment;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ru.planetavto.advertsment.car.CarAdvert;

@Embeddable
public class Price {

	@Column(columnDefinition="Decimal(15,2)")
	private int price;
	
	private LocalDate date;  
		
	public int getPrice() {
		return price;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Override
    public String toString() {
        return "Price [date=" + this.date + ", price=" + this.price + "]";
    }
}
