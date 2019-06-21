package ru.planetavto.presistent;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import ru.planetavto.advertsment.car.CarAdvert;

@Repository
@Transactional
public class CarAdvertRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<CarAdvert> findAll() {
		return (List<CarAdvert>) entityManager.createQuery("select s from CarAdvert s").getResultList();
	}

	public CarAdvert save(CarAdvert advert) {
		entityManager.persist(advert);
		return advert;
	}
	
	public CarAdvert findById(Long advertId) {
		
		CarAdvert advert = entityManager.find(CarAdvert.class, advertId);
        if (advert == null) {
            throw new EntityNotFoundException("Can't find advert for ID " + advertId);
        }
        return advert;
	}
}
