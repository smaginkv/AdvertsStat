package ru.planetavto.presistent;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.planetavto.advertsment.car.CarAdvert;

@Service
public class CarAdvertService {
	private final CarAdvertRepository repository;
	
	@Autowired
	public CarAdvertService(CarAdvertRepository repository) {
		this.repository = repository;
	}

	public List<CarAdvert> findAll() {
		return repository.findAll();
		//return (List<CarAdvert>) entityManager.createQuery("select s from CarAdvert s").getResultList();
	}
	
	public Page<CarAdvert> findPage(Pageable pageable) {
		return repository.findAll(pageable);
		//return (List<CarAdvert>) entityManager.createQuery("select s from CarAdvert s").getResultList();
	}

	public CarAdvert save(CarAdvert advert, boolean update) {
		repository.save(advert);
//		if (update) {
//			entityManager.merge(advert);
//		} else {
//			entityManager.persist(advert);
//		}
		return advert;
	}
	
	public CarAdvert findById(long advertId) {
		
		CarAdvert advert = repository.findById(advertId);
        if (advert == null) {
            throw new EntityNotFoundException("Can't find advert for ID " + advertId);
        }
        return advert;
	}
}
