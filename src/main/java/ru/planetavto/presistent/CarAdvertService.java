package ru.planetavto.presistent;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.planetavto.advertsment.ParsingPlan;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.repo.CarAdvertRepository;

@Service
public class CarAdvertService {
	private final CarAdvertRepository repository;

	@Autowired
	public CarAdvertService(CarAdvertRepository repository) {
		this.repository = repository;
	}

	public List<CarAdvert> findAll() {
		return repository.findAll();
	}

	public Page<CarAdvert> findPage(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public CarAdvert save(CarAdvert advert) {
		repository.save(advert);
		return advert;
	}

	public CarAdvert findById(long advertId) {

		CarAdvert advert = repository.findById(advertId);
		if (advert == null) {
			throw new EntityNotFoundException("Can't find advert for ID " + advertId);
		}
		return advert;
	}

	public CarAdvert findByRef(String advertRef) {

		CarAdvert advert = repository.findByRef(advertRef);
		if (advert == null) {
			throw new EntityNotFoundException("Can't find advert by ref " + advertRef);
		}
		return advert;
	}
	
	public void deleteById(long id) {
		repository.deleteById(id);
	}
	
	public long[] setInactiveForAllExceptAdvertList(List<CarAdvert> adverts, ParsingPlan plan) {
		
		List<CarAdvert> advertsToInactive = getByActivityAndIdNotIn(adverts, plan);
		
		long[] inactiveId = new long[advertsToInactive.size()];
		for(CarAdvert advert: advertsToInactive) {
			advert.setActivity(false);
			advert.setChangeActivityDate(LocalDate.now());
			repository.save(advert);
			//AOP
			
			inactiveId[advertsToInactive.indexOf(advert)] = advert.getId();
		}
		return inactiveId;		
	}
	
	private List<CarAdvert> getByActivityAndIdNotIn(List<CarAdvert> adverts, ParsingPlan plan){
		long [] idArray = new long[Math.max(adverts.size(), 1)];
		for(CarAdvert advert: adverts) {
			idArray[adverts.indexOf(advert)] = advert.getId();
		}
		return repository.findByIdNotInAndActivityAndPlan(idArray, true, plan);
	}
}
