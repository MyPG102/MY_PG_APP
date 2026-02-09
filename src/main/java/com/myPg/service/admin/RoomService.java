package com.myPg.service.admin;



import com.myPg.dto.admin.CreateRoomRequest;
import com.myPg.entity.Room;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    // Create a room in a hostel
    Room createRoom(CreateRoomRequest request);

    // Update room details
    Room updateRoom(UUID roomId, CreateRoomRequest request);

    // Delete room (soft delete or cascade)
    void deleteRoom(UUID roomId);

    // Get room by ID
    Room getRoomById(UUID roomId);

    // Get all rooms for a hostel
    List<Room> getRoomsByHostel(UUID hostelId);
}
