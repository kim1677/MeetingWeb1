package com.MeetingWeb.Dto;

import com.MeetingWeb.Constant.Gender;
import com.MeetingWeb.Entity.User;
import com.MeetingWeb.Entity.UserSelectCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDto {

    private String userName; // 사용자 이름 (유저네임)
    private String name; // 사용자 이름
    private String email; // 사용자 이메일 주소
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // 사용자 생년월일
    private String activityArea; // 사용자의 활동 지역
    private MultipartFile profilePicture; // 업로드된 프로필 사진 파일
    private String profileImgUrl; // 저장된 프로필 사진 경로 또는 URL
    private Gender gender; // 사용자 성별
    private List<String> selectedCategories; // 사용자 선택 카테고리


    // Entity 객체를 DTO로 변환하는 메서드
    public static MemberProfileDto fromEntity(User entity) {
        return MemberProfileDto.builder()
                .userName(entity.getUserName())
                .name(entity.getName())
                .email(entity.getEmail())
                .birthDate(entity.getBirthdate())
                .activityArea(entity.getActivityArea())
                .profileImgUrl(entity.getProfileImgUrl())
                .gender(entity.getGender())
                .selectedCategories(
                        entity.getSelectedCategories().stream()
                                .map(uc -> uc.getGroupCategory().getCategory())
                                .collect(Collectors.toList())
                )
                .build();
    }

    // DTO 객체를 Entity로 변환하는 메서드
    public User toEntity() {
        User user = new User();
        user.setUserName(this.userName);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setBirthdate(this.birthDate);
        user.setActivityArea(this.activityArea);
        user.setProfileImgUrl(this.profileImgUrl);
        user.setGender(this.gender);
        
        return user;


    }
}

