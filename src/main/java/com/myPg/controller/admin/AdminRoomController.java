package com.myPg.controller.admin;

import com.myPg.dto.ApiDataResponse;
import com.myPg.dto.ApiResponse;
import com.myPg.dto.admin.CreateRoomRequest;
import com.myPg.entity.Room;
import com.myPg.service.admin.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class AdminRoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiDataResponse<Room>> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Room room = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiDataResponse<>(true, "Room created successfully", room));
    }

    @GetMapping("/hostel/{hostelId}")
    public ResponseEntity<ApiDataResponse<List<Room>>> getRoomsByHostel(@PathVariable(name="hostelId") UUID hostelId) {
        List<Room> rooms = roomService.getRoomsByHostel(hostelId);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Rooms fetched successfully", rooms));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Room>> updateRoom(@PathVariable(name="id") UUID id,
                                                            @Valid @RequestBody CreateRoomRequest request) {
        Room updated = roomService.updateRoom(id, request);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Room updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRoom(@PathVariable(name="id") UUID id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(new ApiResponse(true, "Room deleted successfully"));
    }
}
