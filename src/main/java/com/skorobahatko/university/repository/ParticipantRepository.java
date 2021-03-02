package com.skorobahatko.university.repository;

import com.skorobahatko.university.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("participantRepository")
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

}
