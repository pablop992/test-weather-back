package com.test.testweatherback.repository;

import com.test.testweatherback.entity.ForecastWrapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastRepository extends MongoRepository<ForecastWrapper, String> {

}
