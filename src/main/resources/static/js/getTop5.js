fetch('/api/v1/articles/top5ByUserCount')
    .then(response => response.json())
    .then(articles => {
        const articlesContainer = document.querySelector('.articles');
        articles.forEach(article => {
            const articleElement = document.createElement('div');
            articleElement.classList.add('article');

            const imageElement = document.createElement('img');
            imageElement.classList.add('article-image');
            imageElement.src = article.imagePath;
            articleElement.appendChild(imageElement);

            const contentElement = document.createElement('div');
            contentElement.classList.add('article-content');

            const titleElement = document.createElement('div');
            titleElement.classList.add('article-title');

            const linkElement = document.createElement('a');
            linkElement.href = article.url;
            linkElement.target = "_blank";
            linkElement.textContent = article.title;

            titleElement.appendChild(linkElement);
            contentElement.appendChild(titleElement);

            articleElement.appendChild(contentElement);

            articlesContainer.appendChild(articleElement);
        });
    })
    .catch(error => console.error('Ошибка получения данных: ', error));