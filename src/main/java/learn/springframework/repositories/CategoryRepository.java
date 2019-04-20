package learn.springframework.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import learn.springframework.domain.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {

}
