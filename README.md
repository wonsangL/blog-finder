# 블로그 검색 서비스
## Summary
블로그 검색과 사용된 키워드를 조회할 수 있는 서비스

해당 프로젝트는 아래 기술들을 활용하여 개발하였습니다.
- Java 17
- Spring Boot 3.0.4 
- JPA
- Redis
  - 블로그 검색 결과 캐싱
- RestAssured
  - Acceptance Test를 위하여 사용

## Installation and Getting Started
```text
git clone https://github.com/wonsangL/blog-finder.git

cd blog-finder

java -jar blog-finder-0.0.1-SNAPSHOT.jar
```
## API 명세
### GET /blog
키워드로 검색된 블로그 정보를 조회합니다
#### Request
##### Parameter
| Name  | Type | Description | Required |
|-------|------|-------------|----------|
| query | String | 검색하고자하는 키워드 | O |
| sort | String | 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순)<br/>기본 값 accuracy | X |
| page | Integer | 결과 페이지 번호 | X |
| size | Integer | 한 페이지에 보여줄 결과 수 | X |
#### Response
##### meta
| Name  | Type    | Descripiton                                                          |
|-------|---------|----------------------------------------------------------------------|
| total | Integer | 검색된 블로그의 수                                                           |
| isEnd | Boolean | 해당 페이지가 마지막인지 여부<br/>해당 값이 `false`일 경우, `page`를 증가시켜 다음 페이지를 호출할 수 있다. |
##### data
| Name    | Type   | Descripiton |
|---------|--------|--------|
| title   | String | 블로그 글 제목 |
| content | String | 블로그 글 요약 |
| name    | String | 블로그 이름 |

### GET /keywords
블로그 검색에 사용된 키워드 중 가장 많이 사용된 10개를 조회합니다.

동일한 횟수로 사용된 키워드는 h2 DB를 통해 정렬된 키워드 순으로 조회합니다
#### Response
##### keywords
| Name     | Type    | Descripiton    |
|----------|---------|----------------|
| title    | String  | 검색에 사용한 키워드    |
| useCount | Integer | 해당 키워드가 사용된 횟수 |

