package com.myPg.service.impl.admin;

import com.myPg.dto.admin.CreateRoomRequest;
import com.myPg.entity.Hostel;
import com.myPg.entity.Room;
import com.myPg.exception.CustomException;
import com.myPg.repository.HostelRepository;
import com.myPg.repository.RoomRepository;
import com.myPg.service.admin.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HostelRepository hostelRepository;

    @Override
    public Room createRoom(CreateRoomRequest request) {

        UUID hostelId;
        try {
            hostelId = UUID.fromString(request.getHostelId());
        } catch (IllegalArgumentException ex) {
            throw new CustomException("INVALID_HOSTEL_ID", "Hostel ID is invalid");
        }

        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new CustomException("HOSTEL_NOT_FOUND", "Hostel not found"));

        Room room = Room.builder()
                .roomNumber(request.getRoomNumber().trim())
                .capacity(request.getCapacity())
                .occupied(request.getOccupied())
                .hostel(hostel)
                .build();

        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(UUID roomId, CreateRoomRequest request) {
        Room room = getRoomById(roomId);

        if (request.getRoomNumber() != null && !request.getRoomNumber().isBlank()) {
            room.setRoomNumber(request.getRoomNumber().trim());
        }

        if (request.getCapacity() != null && request.getCapacity() > 0) {
            room.setCapacity(request.getCapacity());
        }

        if (request.getOccupied() != null && request.getOccupied() >= 0) {
            room.setOccupied(request.getOccupied());
        }

        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(UUID roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room); // Or soft delete if required
    }

    @Override
    public Room getRoomById(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("ROOM_NOT_FOUND", "Room not found"));
    }

    @Override
    public List<Room> getRoomsByHostel(UUID hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> new CustomException("HOSTEL_NOT_FOUND", "Hostel not found"));
        return roomRepository.findByHostel(hostel);
    }
}
