package ru.planetavto.advertsment;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Embeddable
public class Price {
	
	public Price() {
		date = LocalDate.now();
	}

	@NumberFormat(pattern = "###,000")
	private int price;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
