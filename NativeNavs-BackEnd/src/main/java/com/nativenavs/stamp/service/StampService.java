package com.nativenavs.stamp.service;

import com.nativenavs.stamp.dto.StampDTO;
import com.nativenavs.stamp.entity.StampEntity;
import com.nativenavs.stamp.entity.UserStampEntity;
import com.nativenavs.stamp.repository.StampRepository;
import com.nativenavs.stamp.repository.UserStampRepository;
import com.nativenavs.user.entity.UserEntity;
import com.nativenavs.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StampService {

    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final StampRepository stampRepository;


    public void addStamp(int stampId, int userId){
        StampEntity stamp = stampRepository.findById(stampId)
                .orElseThrow(() -> new RuntimeException("Stamp not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserStampEntity userStampEntity = new UserStampEntity();
        userStampEntity.setStamp(stamp);
        userStampEntity.setUser(user);
        userStampEntity.setCreatedDate(LocalDate.now());

        userStampRepository.save(userStampEntity);
        System.out.println("스탬프 등록 완료");
    }


    @Transactional
    public List<StampDTO> getAllStampsByUserId(int userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserStampEntity> userStamps = userStampRepository.findAllByUser(user);

        return userStamps.stream()
                .map(StampDTO::toStampDTO)
                .collect(Collectors.toList());
    }
}
