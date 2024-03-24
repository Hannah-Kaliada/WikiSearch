package com.search.wiki.service;

import com.search.wiki.cache.Cache;
import com.search.wiki.controller.dto.ArticleDTO;
import com.search.wiki.controller.dto.FavouriteArticlesDTO;
import com.search.wiki.controller.dto.UserDTO;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.User;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.utils.ConvertToDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class FavouriteArticlesService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final Cache cache;
    @PersistenceContext
    private EntityManager entityManager;

    public void addArticleToUserFavorites(Long userId, Long articleId) {
        User user = getUserFromCache(userId);
        Article article = getArticleFromCache(articleId);

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                cache.put(getUserCacheKey(userId), user);
            }
        }

        if (article == null) {
            article = articleRepository.findById(articleId).orElse(null);
            if (article != null) {
                cache.put(getArticleCacheKey(articleId), article);
            }
        }

        if (user != null && article != null) {
            entityManager.detach(user);
            user = entityManager.find(User.class, userId);
            user.getFavoriteArticles().add(article);
            userRepository.save(user);
            cache.put(getUserCacheKey(userId), user);
        }
    }

    public void removeArticleFromUserFavorites(long userId, long articleId) {
        String userCacheKey = getUserCacheKey(userId);
        User user = (User) cache.get(userCacheKey);

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                cache.put(userCacheKey, user);
            }
        }

        if (user != null) {
            entityManager.detach(user);
            user = entityManager.find(User.class, userId);
            user.getFavoriteArticles().removeIf(article -> article.getId() == articleId);
            userRepository.save(user);
            cache.remove(userCacheKey);
        }
    }

    public void editUserFavoriteArticle(Long userId, Long prevArticleId, Long newArticleId) {
        String userCacheKey = getUserCacheKey(userId);

        User user = (User) cache.get(userCacheKey);
        Article prevArticle = (Article) cache.get(getArticleCacheKey(prevArticleId));
        Article newArticle = (Article) cache.get(getArticleCacheKey(newArticleId));

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                cache.put(userCacheKey, user);
            }
        }

        if (prevArticle == null) {
            prevArticle = articleRepository.findById(prevArticleId).orElse(null);
            if (prevArticle != null) {
                cache.put(getArticleCacheKey(prevArticleId), prevArticle);
            }
        }

        if (newArticle == null) {
            newArticle = articleRepository.findById(newArticleId).orElse(null);
            if (newArticle != null) {
                cache.put(getArticleCacheKey(newArticleId), newArticle);
            }
        }

        if (user != null && prevArticle != null && newArticle != null) {
            entityManager.detach(user);
            user = entityManager.find(User.class, userId);

            user.getFavoriteArticles().remove(prevArticle);
            prevArticle.getUsers().remove(user);
            user.getFavoriteArticles().add(newArticle);
            newArticle.getUsers().add(user);

            articleRepository.save(prevArticle);
            articleRepository.save(newArticle);

            userRepository.save(user);

            cache.remove(userCacheKey);
        }
    }


    public FavouriteArticlesDTO getUserFavoriteArticles(Long userId) {
        String userCacheKey = getUserCacheKey(userId);
        User user = (User) cache.get(userCacheKey);
        FavouriteArticlesDTO favouriteArticlesDTO = new FavouriteArticlesDTO();

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                cache.put(userCacheKey, user);
            }
        }

        if (user != null) {
            entityManager.detach(user);
            user = entityManager.find(User.class, userId);

            UserDTO userDTO = ConvertToDTO.convertUserToDTO(user);
            favouriteArticlesDTO.setUserId(userId);
            Set<ArticleDTO> articleDTOSet = convertToArticleDTOSet(user.getFavoriteArticles());
            favouriteArticlesDTO.setEmail(user.getEmail());
            favouriteArticlesDTO.setUsername(user.getUsername());
            favouriteArticlesDTO.setCountry(userDTO.getCountry());
            favouriteArticlesDTO.setPassword(user.getPassword());
            favouriteArticlesDTO.setFavouriteArticles(articleDTOSet);
        }

        return favouriteArticlesDTO;
    }

    public Set<User> getArticlesSavedByUser(Long articleId) {
        Article article = (Article) cache.get(getArticleCacheKey(articleId));
        Set<User> users = new HashSet<>();

        if (article == null) {
            article = articleRepository.findById(articleId).orElse(null);
            if (article != null) {
                cache.put(getArticleCacheKey(articleId), article);
            }
        }

        if (article != null) {
            entityManager.detach(article);
            article = entityManager.find(Article.class, article.getId());
            users.addAll(article.getUsers());
        }

        return users;
    }

    private Set<ArticleDTO> convertToArticleDTOSet(Set<Article> articles) {
        Set<ArticleDTO> articleDTOSet = new HashSet<>();
        articles.forEach(article -> {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(article.getId());
            articleDTO.setTitle(article.getTitle());
            articleDTO.setUrl(article.getUrl());
            articleDTO.setImagePath(article.getImagePath());
            articleDTOSet.add(articleDTO);
        });
        return articleDTOSet;
    }

    private User getUserFromCache(Long userId) {
        return (User) cache.get(getUserCacheKey(userId));
    }

    private Article getArticleFromCache(Long articleId) {
        return (Article) cache.get(getArticleCacheKey(articleId));
    }

    private String getUserCacheKey(Long userId) {
        return "User_" + userId;
    }

    private String getArticleCacheKey(Long articleId) {
        return "Article_" + articleId;
    }
}
