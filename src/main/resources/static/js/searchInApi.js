function searchApi() {
    let searchTerm = encodeURIComponent(document.getElementById("word").value);
    fetch('http://localhost:8080/api/v1/search/getSearchResult?searchTerm=' + searchTerm)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.href = "showResults.html?keyword=" + searchTerm;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}