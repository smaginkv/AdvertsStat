package ru.planetavto.presistent.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import ru.planetavto.advertsment.AdvertImage;
import ru.planetavto.advertsment.ParsingPlan;
import ru.planetavto.advertsment.car.CarAdvert;

public interface CarAdvertRepository extends PagingAndSortingRepository<CarAdvert, Long> {
	public CarAdvert findById(long id);
	
	public List<CarAdvert> findAll();
	
	public List<CarAdvert> findByActivity(boolean activity);
	
	public CarAdvert findByRef(String ref);
	
	public List<CarAdvert> findByIdNotInAndActivityAndPlan(long [] Ids, boolean activity, ParsingPlan plan);
}
