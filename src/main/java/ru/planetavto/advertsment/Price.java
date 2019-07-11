package ru.planetavto.advertsment;

import java.time.LocalDate;

import javax.persistence.Embeddable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import lombok.Data;

@Embeddable
@Data
public class Price {
	
	public Price(int price) {
		this.date = LocalDate.now();
		this.price = price;
	}
	public Price() {
		this.date = LocalDate.now();
	}

	@NumberFormat(pattern = "###,000")
	private int price;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;  
		
	@Override
    public String toString() {
        return "Price [date=" + this.date + ", price=" + this.price + "]";
    }
}
