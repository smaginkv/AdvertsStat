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
}
