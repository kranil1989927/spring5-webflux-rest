package learn.springframework.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import learn.springframework.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String>{

}
