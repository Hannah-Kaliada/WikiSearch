<h1 align = center><b><i>Wikipedia Search Service</i></b></h1>
<hr>
<p>This service takes a term/word as input and retrieves relevant text from Wikipedia using the Wikipedia API.</p>
 <h2>API Documentation</h2>
<p>The service uses the Wikipedia API for retrieving information. You can find more about the API in the official documentation:</p>
 <p><a href="https://en.wikipedia.org/w/api.php" target="_blank">Wikipedia API Documentation</a></p>
     <h2>Example Request</h2>
    <p>Make a GET request to the following endpoint:</p>
    <pre>
        <code>http://localhost:8080/api/v1/search/getSearchResult?word=your_search_term</code>
    </pre>
    <h2>Response</h2>
    <p>The service will respond with the relevant text from Wikipedia for the provided term.</p>
    <h2>Setup</h2>
<p>Follow these steps to set up and run the service:</p>
<ol>
    <li>Clone the repository: <code>git clone https://github.com/Hannah-Kaliada/WikiSearch.git</code></li>
    <li>Build the project:</li>
        <ul>
            <li>For macOS:</li>
                <code>./mvnw clean install</code>
            <li>For Windows:</li>
                <code>mvnw clean install</code>
        </ul>
    <li>Run the service:</li>
        <ul>
            <li>For macOS:</li>
                <code>./mvnw exec:java -Dexec.mainClass="com.search.wiki.WikiApplication"</code>
            <li>For Windows:</li>
                <code>mvnw exec:java -Dexec.mainClass="com.search.wiki.WikiApplication"</code>
        </ul>
</ol>
    <h2>Code Quality</h2>
   <p><a href="https://sonarcloud.io/summary/overall?id=Hannah-Kaliada_WikiSearch" target="_blank">View the overall code quality status on SonarCloud.</a></p>
