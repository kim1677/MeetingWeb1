package com.MeetingWeb.Repository;

import com.MeetingWeb.Entity.GroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long> {
    List<GroupCategory> findAllByOrderByGroupCategoryIdAsc();

    // 카테고리 이름으로 GroupCategory 조회
    GroupCategory findByCategory(String category);
}
