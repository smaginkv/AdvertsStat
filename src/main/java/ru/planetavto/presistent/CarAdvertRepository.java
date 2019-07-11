package ru.planetavto.presistent;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import ru.planetavto.advertsment.car.CarAdvert;

public interface CarAdvertRepository extends PagingAndSortingRepository<CarAdvert, Long> {
	public CarAdvert findById(long id);
	
	public List<CarAdvert> findAll();
	
	public CarAdvert findByRef(String ref);
}
