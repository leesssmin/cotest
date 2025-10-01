package com.example.sm.controller;

import com.example.sm.domain.Post;
import com.example.sm.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public String list(Model model){
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post post = postService.findById(id);
        model.addAttribute("post", post);
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
}
