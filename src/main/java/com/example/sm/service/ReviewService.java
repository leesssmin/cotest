package com.example.sm.service;



import com.example.sm.domain.Post;
import com.example.sm.domain.Review;
import com.example.sm.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PostService postService;

    public List<Review> findByPostId(Long postId){
        return reviewRepository.findByPostId(postId);
    }

    @Transactional
    public Review save(Long postId, String content, String author){
        Post post = postService.findById(postId);

        Review review = Review.builder()
                .post(post)
                .content(content)
                .author(author)
                .build();

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteById(Long id){
        reviewRepository.deleteById(id);
    }

}
