package com.example.sm.service;

import com.example.sm.domain.Post;
import com.example.sm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    // 페이징 처리된 전체 조회
    public Page<Post> findAllWithPaging(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return postRepository.findAll(pageable);
    }

    public Page<Post> searchPosts(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (keyword == null || keyword.trim().isEmpty()){
            return postRepository.findAll(pageable);
        }
        return postRepository.searchByTitleOrContent(keyword, pageable);
    }

    @Transactional
    public Post save(Post post){
        return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
}
