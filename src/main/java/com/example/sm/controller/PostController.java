package com.example.sm.controller;

import com.example.sm.domain.Post;
import com.example.sm.domain.Review;
import com.example.sm.service.PostService;
import com.example.sm.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final ReviewService reviewService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            Model model,
            HttpSession session) {  // 🔹 세션 사용

        Page<Post> postPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            postPage = postService.searchPosts(keyword, page, size);
            model.addAttribute("keyword", keyword);
        } else {
            postPage = postService.findAllWithPaging(page, size);
        }

        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalItems", postPage.getTotalElements());

        model.addAttribute("hasPrevious", postPage.hasPrevious());
        model.addAttribute("hasNext", postPage.hasNext());
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("previousPage", page - 1);

        // 페이지 번호 리스트 생성 (최대 5개)
        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        int totalPages = postPage.getTotalPages();
        int startPage = Math.max(0, page - 2);
        int endPage = Math.min(totalPages - 1, page + 2);

        for (int i = startPage; i <= endPage; i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        // 🔹 로그인 상태 모델
        String username = (String) session.getAttribute("username");
        model.addAttribute("isLoggedIn", username != null);
        model.addAttribute("username", username != null ? username : "");

        return "list";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post post = postService.findById(id);
        List<Review> reviews = reviewService.findByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("reviews", reviews);
        return "detail";
    }

    @GetMapping("/new")
    public String createForm(){
        return "create";
    }

    @PostMapping("/new")
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) username = "익명";
        Post post = Post.builder()
                .title(title)
                .content(content)
                .author(username)
                .build();
        postService.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model){
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam String content){
        Post post = postService.findById(id);
        post.setTitle(title);
        post.setContent(content);
        postService.save(post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        postService.deleteById(id);
        return "redirect:/posts";
    }

    // 리뷰 작성
    @PostMapping("/{id}/reviews")
    public String createReview(@PathVariable Long id,
                               @RequestParam String content,
                               HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) username = "익명";

        reviewService.save(id, content, username);
        return "redirect:/posts/" + id;
    }

    // 리뷰 삭제
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId,
                               @RequestParam Long postId) {
        reviewService.deleteById(reviewId);
        return "redirect:/posts/" + postId;
    }
}