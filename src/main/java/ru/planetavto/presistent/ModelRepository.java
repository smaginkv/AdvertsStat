package ru.planetavto.presistent;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import ru.planetavto.advertsment.Model;

@Repository
@Transactional
public class ModelRepository {
	@PersistenceContext
	private EntityManager manager;
	
	@SuppressWarnings("unchecked")
	public List<Model> findAll() {
		return (List<Model>) manager.createQuery("select s from Model s").getResultList();
	}

	public Model save(Model model) {
		manager.persist(model);
		return model;
	}
	
	public Model findById(Long modelId) {
		
		Model model = manager.find(Model.class, modelId);
        if (model == null) {
            throw new EntityNotFoundException("Can't find model for ID " + modelId);
        }
        return model;
	}

}
