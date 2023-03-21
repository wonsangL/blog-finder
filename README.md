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
해당 프로젝트는 아래와 같은 설정으로 `Redis`를 사용하고 있습니다.

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

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

## 주요 구현 내용
- 블로그 검색 소스가 추가되는 경우를 고려
  - 블로그 검색 소스를 [추상화](src/main/java/com/example/blogfinder/domain/blog/BlogClient.java)하여, 검색 소스가 추가되더라도 이를 사용하는 [클라이언트](src/main/java/com/example/blogfinder/domain/blog/BlogFinder.java)에는 수정이 필요하지 않도록 설계
  - `@DependsOn`를 사용하여 검색 소스의 순서를 지정, 특정 검색 소스에 문제가 있으면 다음 검색 소스를 사용한다고 가정하였다.
- 트래픽이 많은 상황을 고려
  - 캐싱을 통해 성능 최적화
    - 블로그의 데이터 특성상 정확도순으로 조회할 경우, 데이터의 실시간성은 비교적 중요하지 않다고 생각하여 데이터를 캐싱 
- 동시성이 이슈가 발생하는 상황 고려

## 아쉬웠던 점 및 추가 고민해볼 부분
- 캐싱된 블로그 데이터에 중복이 발생할 수 있다.
  - `page=1&size=10` 결과와 `page=1&size=20`의 파라미터로 블로그를 검색할 경우 `page=1&size=10` 데이터는 중복되어 존재할 수 있다.