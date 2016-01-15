package ua.com.itproekt.gup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.itproekt.gup.model.news.BlogPost;
import ua.com.itproekt.gup.model.news.BlogPostFilterOptions;
import ua.com.itproekt.gup.model.profiles.Profile;
import ua.com.itproekt.gup.service.news.BlogPostService;
import ua.com.itproekt.gup.service.profile.ProfilesService;
import ua.com.itproekt.gup.util.EntityPage;

/**
 * Created by RAYANT on 13.01.2016.
 */

@Controller
@RequestMapping("/blog-post")
public class BlogPostController {

    @Autowired
    ProfilesService profilesService;

    @Autowired
    BlogPostService blogPostService;

    @RequestMapping("/view/{id}")
    public String blogPostView(Model model, @PathVariable("id") String id) {

        BlogPost blogPost = blogPostService.findBlogPostAndIncViews(id);
        model.addAttribute("blogPost", blogPost);
        return "blog-post-view";
    }

    @RequestMapping("/view-all/{blogId}")
    public String blogPostViewAll(Model model, @PathVariable("blogId") String blogId) {
        BlogPostFilterOptions blogPostFO = new BlogPostFilterOptions();
        blogPostFO.setBlogId(blogId);
        EntityPage<BlogPost> blogPostPages = blogPostService.findBlogPostsWihOptions(blogPostFO);
        model.addAttribute("blogPostPages", blogPostPages);
        return "blog-post-view-all";
    }

    //ToDo Проверять "А ты ли владелец этого блога, чтобы в неём создовать новость?"
    @RequestMapping("/create/{blogId}")
    public String blogPostCreate(Model model, @PathVariable("blogId") String blogId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Profile profile = profilesService.findProfileByEmail(email);
        model.addAttribute("profileId", profile.getId());
        model.addAttribute("blogId", blogId);
        return "blog-post-create";
    }

    @RequestMapping("/edit/{blogPostId}")
    public String blogPostEdit(Model model, @PathVariable("blogPostId") String blogPostId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Profile profile = profilesService.findProfileByEmail(email);
        BlogPost blogPost = blogPostService.findById(blogPostId);
        model.addAttribute("blogPost", blogPost);
        model.addAttribute("profileId", profile.getId());
        return "blog-post-edit";
    }


}
