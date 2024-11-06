package com.MeetingWeb.Service;

import com.MeetingWeb.Dto.MemberProfileDto;
import com.MeetingWeb.Entity.GroupCategory;
import com.MeetingWeb.Entity.User;
import com.MeetingWeb.Entity.UserSelectCategory;
import com.MeetingWeb.Repository.GroupCategoryRepository;
import com.MeetingWeb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final UserRepository userRepository;
    private final GroupCategoryRepository groupCategoryRepository;

    public MemberProfileDto getMemberProfile(String userName) {
        // 사용자 이름으로 사용자 정보를 조회하고 DTO로 변환
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return MemberProfileDto.fromEntity(user);
    }

    // 사용자 정보를 업데이트하는 메서드
    @Transactional
    public void updateMemberProfile(MemberProfileDto memberProfileDto) {
        // 사용자를 userName으로 검색
        User user = userRepository.findByUserName(memberProfileDto.getUserName());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 사용자 정보 업데이트
        user.setName(memberProfileDto.getName());
        user.setEmail(memberProfileDto.getEmail());
        user.setBirthdate(memberProfileDto.getBirthDate());
        user.setActivityArea(memberProfileDto.getActivityArea());
        user.setGender(memberProfileDto.getGender());

        // 사용자 선택 카테고리 업데이트
        user.getSelectedCategories().clear();  // 기존 카테고리 삭제
        if (memberProfileDto.getSelectedCategories() != null) {
            for (String categoryName : memberProfileDto.getSelectedCategories()) {
                // 카테고리 이름으로 GroupCategory 조회
                GroupCategory category = groupCategoryRepository.findByCategory(categoryName);
                if (category != null) {
                    UserSelectCategory userSelectCategory = new UserSelectCategory();
                    userSelectCategory.setUser(user);
                    userSelectCategory.setGroupCategory(category);
                    user.getSelectedCategories().add(userSelectCategory);
                }
            }
        }

        // 프로필 이미지 파일 저장 처리
        MultipartFile profilePicture = memberProfileDto.getProfilePicture();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                // 파일 저장 경로 설정
                String fileName = UUID.randomUUID().toString() + "_" + profilePicture.getOriginalFilename();
                Path filePath = Paths.get("path/to/save/directory", fileName);

                // 파일 저장
                Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 저장된 파일의 경로를 user 엔터티에 설정
                user.setProfileImgUrl("/path/to/save/directory/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.");
            }
        }

        // 변경된 사용자 정보 저장 (트랜잭션 안에서는 이 호출로 DB에 반영됩니다)
        userRepository.save(user);

    }
}
