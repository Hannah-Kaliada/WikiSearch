let currentPage = 1;
const itemsPerPage = 5;

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[[]]/g, "$&");
    let regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function loadArticles(page) {
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const articlesContainer = document.querySelector('.articles');
    articlesContainer.innerHTML = "";

    fetch("http://localhost:8080/api/v1/articles/search?keyword=" + keyword)
        .then(response => response.json())
        .then(data => {
            data.slice(startIndex, endIndex).forEach(article => {
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

            const prevBtn = document.getElementById('prevBtn');
            const nextBtn = document.getElementById('nextBtn');
            prevBtn.disabled = currentPage === 1;
            nextBtn.disabled = currentPage === Math.ceil(data.length / itemsPerPage);
        })
        .catch(error => console.error('Ошибка при получении данных:', error));
}

function goToPrevPage() {
    currentPage--;
    loadArticles(currentPage);
}

function goToNextPage() {
    currentPage++;
    loadArticles(currentPage);
}

let keyword = getParameterByName('keyword');

loadArticles(currentPage);

document.getElementById('prevBtn').addEventListener('click', goToPrevPage);
document.getElementById('nextBtn').addEventListener('click', goToNextPage);
