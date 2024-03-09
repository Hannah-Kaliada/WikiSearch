function searchApi() {
    var searchTerm = document.getElementById("word").value;
    fetch('http://localhost:8080/api/v1/search/getSearchResult?searchTerm=' + searchTerm)
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
