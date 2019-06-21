package ru.planetavto.advertsment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Model {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nomination;
	
	private String brand;
	
	public Long getId() {
		return id;
	}
	public String getNomination() {
		return nomination;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setNomination(String nomination) {
		this.nomination = nomination;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	@Override
	public String toString() {
		return brand + " " + nomination;
	}
}
