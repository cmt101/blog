package com.calebtl.blog.services;

import com.calebtl.blog.dtos.CreateUserRequest;
import com.calebtl.blog.dtos.ProfileDto;
import com.calebtl.blog.dtos.UserDto;
import com.calebtl.blog.entities.Profile;
import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.UserExistsException;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.mappers.CreateUserMapper;
import com.calebtl.blog.mappers.ProfileMapper;
import com.calebtl.blog.mappers.UserMapper;
import com.calebtl.blog.repositories.ProfileRepository;
import com.calebtl.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private ProfileMapper profileMapper;
    private CreateUserMapper createUserMapper;
    private final ProfileRepository profileRepository;

    public List<UserDto> getAllUsersSorted() {
        return userRepository.findAllUsersSorted()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        User u = userRepository.findUserById(id).orElse(null);
        if (u == null) {
            return null;
        }

        return userMapper.toDto(u);
    }

    @Transactional
    public UserDto createUser(CreateUserRequest data) {
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new UserExistsException();
        }

        User u = createUserMapper.toEntity(data);
        u.getProfile().setUser(u);
        userRepository.save(u);

        return userMapper.toDto(u);
    }


    // There's an argument to be made that this should go in its own ProfileService, but this also works fine.
    @Transactional
    public ProfileDto updateUserProfile(Long id, ProfileDto updates) {
        Profile p = profileRepository.findByUserId(id).orElseThrow(UserNotFoundException::new);
        profileMapper.update(updates, p);
        profileRepository.save(p);
        return profileMapper.toDto(p);
    }

    @Transactional
    public void deleteUser(Long id) {
        User u = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(u);
    }
}
