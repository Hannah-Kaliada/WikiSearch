package com.search.wiki.service.impl;

import com.search.wiki.controller.dto.ArticleDTO;
import com.search.wiki.controller.dto.FavouriteArticlesDTO;
import com.search.wiki.entity.Article;
import com.search.wiki.entity.User;
import com.search.wiki.repository.ArticleRepository;
import com.search.wiki.repository.UserRepository;
import com.search.wiki.service.FavouriteArticlesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class FavouriteArticlesServiceImpl implements FavouriteArticlesService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public void addArticleToUserFavorites(Long userId, Long articleId) {
        User user = userRepository.findById(userId).orElse(null);
        Article article = articleRepository.findById(articleId).orElse(null);

        if (user != null && article != null) {
            user.getFavoriteArticles().add(article);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void addFavoriteUserToArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (article != null && user != null) {
            article.getUsers().add(user);
            user.getFavoriteArticles().add(article);
            articleRepository.save(article);
            userRepository.save(user);
        }
    }



    @Override
    @Transactional
    public void removeArticleFromUserFavorites(Long userId, Long articleId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.getFavoriteArticles().removeIf(article -> article.getId()==(articleId));
            userRepository.save(user);
        }
    }
    @Override
    @Transactional
    public void removeFavoriteUserFromArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId).orElse(null);

        if (article != null) {
            Iterator<User> iterator = article.getUsers().iterator();
            while (iterator.hasNext()) {
                User user = iterator.next();
                if (user.getId()==(userId)) {
                    iterator.remove();
                    user.getFavoriteArticles().remove(article);
                    userRepository.save(user);
                }
            }

            articleRepository.save(article);
        }
    }


    @Override
    @Transactional
    public void editUserFavoriteArticle(Long userId, Long prevArticleId, Long newArticleId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.getFavoriteArticles().removeIf(article -> article.getId() == prevArticleId);

            Article newArticle = articleRepository.findById(newArticleId).orElse(null);
            if (newArticle != null) {
                user.getFavoriteArticles().add(newArticle);
                userRepository.save(user);
            }
        }
    }

    @Override
    public FavouriteArticlesDTO getUserFavoriteArticles(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        FavouriteArticlesDTO favouriteArticlesDTO = new FavouriteArticlesDTO();

        if (user != null) {
            favouriteArticlesDTO.setUserId(userId);
            Set<ArticleDTO> articleDTOSet = convertToArticleDTOSet(user.getFavoriteArticles());
            favouriteArticlesDTO.setFavouriteArticles(articleDTOSet);
        }

        return favouriteArticlesDTO;
    }


    @Override
    public Set<Long> getArticlesSavedByUser(Long articleId) {
        Article article = articleRepository.findById(articleId).orElse(null);
        Set<Long> userIds = new HashSet<>();

        if (article != null) {
            article.getUsers().forEach(user -> userIds.add(user.getId()));
        }

        return userIds;
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
            System.out.println("Converted article with ID " + article.getId() + " to DTO");
        });
        return articleDTOSet;
    }


}
