package com.teamsapi.respository;

import com.teamsapi.entity.teamsapi.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamsChannelRepository extends MongoRepository<Channel, String> {

}