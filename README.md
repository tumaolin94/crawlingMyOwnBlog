# crawlingMyOwnBlog

### Based on
1. crawler4j
2. jsoup

### Current Function
1. crawling articles
2. calculating different kinds of statistics
3. output by Logger
4. Saving information in CSV
5. auto download image 
6. Downloading article contents as TXT file in folder contents
### CSV introduction
1. fetch_blog.csv
- URL
- HTTP status code

2. visit_blog.csv
- URL
- file size
- the number of outlinks
- content-type

3. urls_blog.csv
- URL
- a. resides in the website(OK), b. outside of website(N_OK)

4. blog_articles.csv
- URL
- article header