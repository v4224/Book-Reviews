package com.harinem.post_service.service;

import com.harinem.post_service.dto.PageResponse;
import com.harinem.post_service.dto.request.PostRequest;
import com.harinem.post_service.dto.response.PostResponse;
import com.harinem.post_service.dto.response.UserProfileResponse;
import com.harinem.post_service.entity.Post;
import com.harinem.post_service.mapper.PostMapper;
import com.harinem.post_service.repository.PostRepository;
import com.harinem.post_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PostService {

    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;
    public PostResponse createPost(PostRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId=authentication.getName();
        Post post= Post.builder()
                .userId(userId)
                .content(request.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        post=postRepository.save(post);
        return postMapper.toPostResponse(post);

    }

    public PageResponse<PostResponse> getMyPosts(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId=authentication.getName();

        UserProfileResponse userProfileResponse=null;
        try{
            userProfileResponse=profileClient.getProfileByUserId(userId).getResult();
        }
        catch (Exception e){
            log.error("Error while getting user profile", e);
        }


        Sort sort=Sort.by("createdDate").descending();

        Pageable pageable= PageRequest.of(page-1,size,sort);
        var pageData=postRepository.findAllByUserId(userId,pageable);

        String username=userProfileResponse!=null?userProfileResponse.getUsername():null;
        var postList=pageData.getContent().stream().map(post -> {
            var postResponse=postMapper.toPostResponse(post);
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            postResponse.setUsername(username);
            return postResponse;
        }).toList();
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();

    }


}
