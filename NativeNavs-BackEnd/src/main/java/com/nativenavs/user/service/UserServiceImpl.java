package com.nativenavs.user.service;

import com.nativenavs.user.entity.UserEntity;
import com.nativenavs.user.dto.UserDTO;
import com.nativenavs.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Set<String> authenticatedUsers = ConcurrentHashMap.newKeySet();   // 인증 회원을 임시 저장



    @Override
    public boolean checkDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public boolean checkDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();

    }

    @Override
    public void signUp(UserDTO userDTO) {
        if(!authenticatedUsers.contains(userDTO.getEmail())){  // 이메일 미인증시
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다.");
        }

        UserEntity userEntity = UserEntity.toSaveEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);

        authenticatedUsers.remove(userDTO.getEmail()); // 메모리 저장소에서 인증 회원 제거 (가입 완료했으니)
    }

    @Override
    public List<UserDTO> searchAllUser() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(UserDTO::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO searchByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return UserDTO.toUserDTO(userEntity);
    }

    @Override
    public UserDTO searchById(int id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return UserDTO.toUserDTO(userEntity);
    }

    @Override
    public UserDTO searchByNickname(String nickname) {
        UserEntity userEntity = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("User with nickname " + nickname + " not found"));
        return UserDTO.toUserDTO(userEntity);
    }

    @Override
    public UserDTO searchByName(String name) {
        UserEntity userEntity = userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User with name " + name + " not found"));
        return UserDTO.toUserDTO(userEntity);
    }

    @Override
    public int changeEmailToId(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return userEntity.getId();
    }

    @Transactional
    @Override
    public void updateUser(int existingId, UserDTO updateUserDTO){

        Optional<UserEntity> findUserEntity = userRepository.findById(existingId);
        if (findUserEntity.isPresent()) {
            UserEntity updateUserEntity = findUserEntity.get();
            updateUserDTOFields(updateUserEntity, updateUserDTO);
            userRepository.save(updateUserEntity);

        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public void updateUserDTOFields(UserEntity updateUserEntity, UserDTO updateUserDTO) {
        updateUserEntity.setImage(updateUserDTO.getImage());
        updateUserEntity.setNickname(updateUserDTO.getNickname());
        updateUserEntity.setUserLanguage(updateUserDTO.getUserLanguage());
        updateUserEntity.setPhone(updateUserDTO.getPhone());
        updateUserEntity.setPassword(updateUserDTO.getPassword());
    }

    @Override
    public void deleteUser(int id) {
        Optional<UserEntity> findUserEntity = userRepository.findById(id);
        userRepository.delete(findUserEntity.get());
    }


    @Override
    public void addAuthenticatedUser(String email){
        authenticatedUsers.add(email);
    }

}
