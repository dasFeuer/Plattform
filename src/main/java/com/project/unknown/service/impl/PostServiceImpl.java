package com.project.unknown.service.impl;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDetailDto;
import com.project.unknown.domain.dtos.postDto.PostSummaryDto;
import com.project.unknown.domain.dtos.postDto.UpdatePostRequestDto;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.exception.ResourceNotFoundException;
import com.project.unknown.mapper.PostMapper;
import com.project.unknown.repository.PostRepository;
import com.project.unknown.service.PostService;
import com.project.unknown.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;


    @Override
    @Transactional
    public PostDetailDto createPost(CreatePostRequestDto requestDto, String authorEmail) {
        log.info("Creating new post with title: '{}' for author: {}", requestDto.getTitle(), authorEmail);

        User author = userService.getUserEntityByEmail(authorEmail);
        log.debug("Author found: {} (ID: {})", author.getUsername(), author.getId());

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(author)
                .comments(new ArrayList<>())
                .reactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with ID: {}", savedPost.getId());


        return postMapper.toDetailDto(savedPost);    }

    @Override
    public PostDetailDto getPostById(Long id) {
        log.debug("Fetching post DTO for ID: {}", id);
        Post post = getPostEntityById(id);
        return postMapper.toDetailDto(post);
    }

    @Override
    public PagedResponse<PostSummaryDto> getAllPosts(int page, int size, String[] sort) {
        log.info("Fetching all posts - page: {}, size: {}", page, size);
        // Pageable erstellen
        Pageable pageable = createPageable(page, size, sort);

        // Posts aus DB holen- paginiert
        Page<Post> postsPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        // Entity- DTO konvertieren
        List<PostSummaryDto> postDtos = postMapper.toSummaryDtoList(postsPage.getContent());

        // PagedResponse erstellen
        PagedResponse<PostSummaryDto> response = PagedResponse.<PostSummaryDto>builder()
                .content(postDtos)
                .pageNumber(postsPage.getNumber())
                .pageSize(postsPage.getSize())
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .last(postsPage.isLast())
                .first(postsPage.isFirst())
                .build();
        log.debug("Fetched {} posts out of {} total", postDtos.size(), postsPage.getTotalElements());
        return response;
    }

    @Override
    public PagedResponse<PostSummaryDto> getPostsByAuthorId(Long authorId, int page, int size) {
        log.info("Fetching posts for author ID: {} - page: {}, size: {}", authorId, page, size);

        // Prüfen ob Author existiert
        userService.getUserEntityById(authorId);

        // Pageable erstellen- immer nach createdAt DESC sortiert
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Posts aus DB holen
        Page<Post> postsPage = postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);

        // Entity- DTO
        List<PostSummaryDto> postDtos = postMapper.toSummaryDtoList(postsPage.getContent());

        // PagedResponse erstellen
        PagedResponse<PostSummaryDto> response = PagedResponse.<PostSummaryDto>builder()
                .content(postDtos)
                .pageNumber(postsPage.getNumber())
                .pageSize(postsPage.getSize())
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .last(postsPage.isLast())
                .first(postsPage.isFirst())
                .build();

        log.debug("Author {} has {} posts total", authorId, postsPage.getTotalElements());

        return response;

    }

    @Override
    public PagedResponse<PostSummaryDto> searchPostsByTitle(String keyword, int page, int size) {
        log.info("Searching posts with keyword: '{}' - page: {}, size: {}", keyword, page, size);

        // Pageable erstellen
        Pageable pageable = PageRequest.of(page, size);

        // Suchen
        Page<Post> postsPage = postRepository.searchByTitle(keyword, pageable);

        // Entity → DTO
        List<PostSummaryDto> postDtos = postMapper.toSummaryDtoList(postsPage.getContent());

        // PagedResponse erstellen
        PagedResponse<PostSummaryDto> response = PagedResponse.<PostSummaryDto>builder()
                .content(postDtos)
                .pageNumber(postsPage.getNumber())
                .pageSize(postsPage.getSize())
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .last(postsPage.isLast())
                .first(postsPage.isFirst())
                .build();

        log.debug("Found {} posts matching keyword '{}'", postsPage.getTotalElements(), keyword);

        return response;    }

    @Override
    public PostDetailDto updatePost(Long id, UpdatePostRequestDto requestDto, String authorEmail) {
        log.info("Updating post with ID: {}", id);

        // Post finden
        Post post = getPostEntityById(id);

        // Author validieren
        User author = userService.getUserEntityByEmail(authorEmail);
        if (!post.getAuthor().getId().equals(author.getId())) {
            log.warn("User {} tried to update post {} owned by {}",
                    author.getId(), id, post.getAuthor().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only update your own posts"
            );
        }

        // Update Felder
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        // Speichern
        Post updatedPost = postRepository.save(post);
        log.info("Post {} updated successfully", id);

        return postMapper.toDetailDto(updatedPost);    }

    @Override
    public void deletePost(Long id, String authorEmail) {

        // Post finden
        Post post = getPostEntityById(id);

        // Author validieren
        User author = userService.getUserEntityByEmail(authorEmail);
        if (!post.getAuthor().getId().equals(author.getId())) {
            log.warn("User {} tried to delete post {} owned by {}",
                    author.getId(), id, post.getAuthor().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only delete your own posts"
            );
        }

        // Löschen
        postRepository.deleteById(id);
        log.info("Post {} deleted successfully", id);
    }

    @Override
    public Optional<Post> findPostById(Long id) {
        log.debug("Finding post by ID: {}", id);
        return postRepository.findById(id);
    }

    @Override
    public Post getPostEntityById(Long id) {
        log.debug("Fetching post entity for ID: {}", id);
        return postRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post with ID {} not found", id);
                    return new ResourceNotFoundException("Post with ID " + id + " not found");
                });
    }


    // Helper Methode für Pageable

    private Pageable createPageable(int page, int size, String[] sort){
        // Sortierung parsen
        List<Sort.Order> orders = new ArrayList<>();

        if(sort != null && sort.length > 0 && sort[0].contains(",")) {
            // Format: "field, direction" z.B. "createdAt,desc"
            for(String sortOrder : sort){
                String[] parts = sortOrder.split(",");
                String field = parts[0];
                Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
                orders.add(new Sort.Order(direction, field));
            }
        } else {
            // Default- Nach createdAt DESC sortieren
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));

        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

}
