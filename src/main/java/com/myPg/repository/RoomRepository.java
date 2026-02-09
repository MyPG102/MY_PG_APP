package com.myPg.repository;

import com.myPg.entity.Room;
import com.myPg.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByHostel(Hostel hostel);
}
