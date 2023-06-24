package com.teamsapi.respository;

import com.teamsapi.entity.teamsapi.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamsMessageRepository extends MongoRepository<Message, String> {

}