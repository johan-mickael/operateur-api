package mg.s5.service;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import base.mongo.Offres;

@Repository
@Service
public interface OffreRepository extends MongoRepository<Offres, String> {

}
