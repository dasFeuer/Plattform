package com.project.unknown.controller;

import com.project.unknown.config.GeneralEndPointAccess;
import com.project.unknown.domain.PatchUserDataRequest;
import com.project.unknown.domain.UpdateUserDataRequest;
import com.project.unknown.domain.dtos.userDto.PatchUserDataRequestDto;
import com.project.unknown.domain.dtos.userDto.UpdateUserDataRequestDto;
import com.project.unknown.domain.dtos.userDto.UserDto;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.mapper.UserMapper;
import com.project.unknown.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final GeneralEndPointAccess endPointAccess;

    @GetMapping("/{id}/id")
    public ResponseEntity<UserDto> getById(@PathVariable Long id){
        Optional<User> userById = userService.getUserById(id);
        if ((userById.isPresent())){
            UserDto dto = userMapper.toDto(userById.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAl(){
        List<User> allUser = userService.getAllUser();
        List<UserDto> list = allUser.stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/patchData")
    public ResponseEntity<UserDto> patchUserData(@PathVariable Long id,
                                                 @RequestBody PatchUserDataRequestDto patchUserDataRequestDto)
            throws IOException {

        endPointAccess.validateUserAccess(id);

        try {
            PatchUserDataRequest patchUserDataRequest = userMapper.toPatchUserDataRequest(patchUserDataRequestDto);
            User updatedUser = userService.patchUserInfo(id, patchUserDataRequest);
            UserDto userDto = userMapper.toDto(updatedUser);
            return ResponseEntity.ok().body(userDto);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/updateData")
    public ResponseEntity<UserDto> updateUserData(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateUserDataRequestDto updateUserDataRequestDto)
            throws IOException {

        endPointAccess.validateUserAccess(id);

        try{
            UpdateUserDataRequest updateUserDataRequest = userMapper.toUpdateUserDataRequest(updateUserDataRequestDto);
            User updatedUser = userService.updateUserInfo(id, updateUserDataRequest);
            UserDto updatedUserDto = userMapper.toDto(updatedUser);
            return ResponseEntity.ok().body(updatedUserDto);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
