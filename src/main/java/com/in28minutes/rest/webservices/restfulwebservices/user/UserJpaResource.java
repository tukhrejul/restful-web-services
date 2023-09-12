package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
public class UserJpaResource {

    private UserRepository repository;
    private PostRepository postRepository;

    public UserJpaResource(UserRepository repository, PostRepository postRepository)
    {
        this.repository = repository;
        this.postRepository = postRepository;
    }
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers()
    {
        return repository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable Integer id)
    {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
        {
            throw new UserNotFoundException("id="+id);
        }
        EntityModel<User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable Integer id)
    {
        repository.deleteById(id);
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user)
    {
        User savedUser = repository.save(user);
        URI loation  = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();
        return ResponseEntity.created(loation).build();
    }

    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostByUser(@PathVariable Integer id)
    {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
        {
            throw new UserNotFoundException("id="+id);
        }
       return user.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Post> createPostForUser(@PathVariable Integer id, @Valid @RequestBody Post post)
    {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
        {
            throw new UserNotFoundException("id="+id);
        }
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);
        URI loation  = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(loation).build();
       // return user.get().getPosts();
    }

    @GetMapping("/jpa/users/{id}/posts/{postId}")
    public Post retrievePostByUser(@PathVariable Integer id, @PathVariable Integer postId)
    {
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
        {
            throw new UserNotFoundException("id="+id);
        }

        Predicate<? super Post> predicate = post -> post.getId().equals(postId);
        return user.get().getPosts().stream().filter(predicate).findFirst().orElse(null);
    }
}
