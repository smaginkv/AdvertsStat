package ru.planetavto.advertsment.car;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ru.planetavto.advertsment.Model;

@Entity
public class CarAdvert {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition="Decimal(2,1)")
	private float engineCapacity;
	
	private int productionYear;	
	
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
	
	public Long getId() {
		return id;
	}
	public int getProductionYear() {
		return productionYear;
	}
	public String getCityOfSale() {
		return cityOfSale;
	}
	public boolean isActivity() {
		return activity;
	}
	public float getEngineCapacity() {
		return engineCapacity;
	}
	public int getHorsepower() {
		return horsepower;
	}
	public String getDescription() {
		return description;
	}
	public byte getOwnersNumber() {
		return ownersNumber;
	}
	public boolean isAirConditioning() {
		return airConditioning;
	}
	public boolean isEsp() {
		return esp;
	}
	public int getMileage() {
		return mileage;
	}	
	public void setId(Long id) {
		this.id = id;
	}
	public void setProductionYear(int productionYear) {
		this.productionYear = productionYear;
	}
	public void setCityOfSale(String cityOfSale) {
		this.cityOfSale = cityOfSale;
	}
	public void setActivity(boolean activity) {
		this.activity = activity;
	}
	public void setEngineCapacity(float engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
	public void setHorsepower(int horsepower) {
		this.horsepower = horsepower;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setOwnersNumber(byte ownersNumber) {
		this.ownersNumber = ownersNumber;
	}
	public void setAirConditioning(boolean airConditioning) {
		this.airConditioning = airConditioning;
	}
	public void setEsp(boolean esp) {
		this.esp = esp;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}	
}
