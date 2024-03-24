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
    private final Cache userCache;
    private final Cache articleCache;

    public void addArticleToUserFavorites(Long userId, Long articleId) {
        User user = getUserFromCache(userId);
        Article article = getArticleFromCache(articleId);

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userCache.put(getUserCacheKey(userId), user);
            }
        }

        if (article == null) {
            article = articleRepository.findById(articleId).orElse(null);
            if (article != null) {
                articleCache.put(getArticleCacheKey(articleId), article);
            }
        }

        if (user != null && article != null) {
            user.getFavoriteArticles().add(article);
            userRepository.save(user);
            userCache.put(getUserCacheKey(userId), user);
        }
    }

    public void removeArticleFromUserFavorites(long userId, long articleId) {
        String userCacheKey = getUserCacheKey(userId);
        User user = (User) userCache.get(userCacheKey);

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userCache.put(userCacheKey, user);
            }
        }
        if (user != null) {
            user.getFavoriteArticles().removeIf(article -> article.getId() == articleId);
            userRepository.save(user);
            userCache.remove(userCacheKey);
        }
    }

    public void editUserFavoriteArticle(Long userId, Long prevArticleId, Long newArticleId) {
        String userCacheKey = getUserCacheKey(userId);
        User user = (User) userCache.get(userCacheKey);

        if (user != null) {
            Article prevArticle = getArticleFromCache(prevArticleId);

            if (prevArticle == null) {
                prevArticle = articleRepository.findById(prevArticleId).orElse(null);
                if (prevArticle != null) {
                    articleCache.put(getArticleCacheKey(prevArticleId), prevArticle);
                }
            }

            if (prevArticle != null) {
                user.getFavoriteArticles().remove(prevArticle);
                prevArticle.getUsers().remove(user);
                articleRepository.save(prevArticle);
            }

            Article newArticle = getArticleFromCache(newArticleId);
            if (newArticle == null) {
                newArticle = articleRepository.findById(newArticleId).orElse(null);
                if (newArticle != null) {
                    articleCache.put(getArticleCacheKey(newArticleId), newArticle);
                }
            }

            if (newArticle != null) {
                user.getFavoriteArticles().add(newArticle);
                newArticle.getUsers().add(user);
                articleRepository.save(newArticle);
                User updatedUser = userRepository.save(user);
                if (updatedUser != null) {
                    userCache.put(userCacheKey, updatedUser);
                }
            }
        }
    }

    public FavouriteArticlesDTO getUserFavoriteArticles(Long userId) {
        User user = getUserFromCache(userId);
        FavouriteArticlesDTO favouriteArticlesDTO = new FavouriteArticlesDTO();

        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                userCache.put(getUserCacheKey(userId), user);
            }
        }

        if (user != null) {
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
        Article article = getArticleFromCache(articleId);
        Set<User> users = new HashSet<>();

        if (article == null) {
            article = articleRepository.findById(articleId).orElse(null);
            if (article != null) {
                articleCache.put(getArticleCacheKey(articleId), article);
            }
        }

        if (article != null) {
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
        return (User) userCache.get(getUserCacheKey(userId));
    }

    private Article getArticleFromCache(Long articleId) {
        return (Article) articleCache.get(getArticleCacheKey(articleId));
    }

    private String getUserCacheKey(Long userId) {
        return "User_" + userId;
    }

    private String getArticleCacheKey(Long articleId) {
        return "Article_" + articleId;
    }
}
