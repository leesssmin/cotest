package com.example.sm.service;

import com.example.sm.domain.Post;
import com.example.sm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post save(Post post){
        return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
    }

    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
}
