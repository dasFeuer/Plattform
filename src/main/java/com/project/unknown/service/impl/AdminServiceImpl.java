package com.project.unknown.service;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.postDto.PostSummaryDto;
import com.project.unknown.domain.dtos.userDto.*;


public interface AdminService {
    AdminStatsDto getStats();

    PagedResponse<UserManagementDto> getAllUsers(int page, int size);
    UserManagementDto getUserById(Long userId);
    UserManagementDto promoteToAdmin(Long userId);
    UserManagementDto demoteToUser(Long userId);
    UserManagementDto toggleUserBan(Long userId);
    void deleteUser(Long userId);

    PagedResponse<PostSummaryDto> getAllPosts(int page, int size);
    void deletePost(Long postId);

    void deleteComment(Long commentId);
}
