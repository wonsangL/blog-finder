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
  - Acceptance Test 수행

## Installation and Getting Started
해당 프로젝트는 아래와 같은 설정으로 `Redis`를 사용하고 있어, `Redis` 설치가 필요합니다.

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
| Name  | Type | Description                                                   | Required |
|-------|------|---------------------------------------------------------------|----------|
| query | String | 검색하고자하는 키워드                                                   | O |
| sort | String | 결과 문서 정렬 방식, ACCURACY(정확도순) 또는 RECENCY(최신순)<br/>기본 값 ACCURACY | X |
| page | Integer | 결과 페이지 번호<br/>1~50 사이의 값, 기본 값 1                              | X |
| size | Integer | 한 페이지에 보여줄 결과 수<br/>1~50 사이의 값, 기본 값 10                       | X |
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

#### Example
##### Request
```curl
curl --location --request GET 'localhost:8080/blog?query=kakaobank'
```
##### Response
```json
{
    "meta": {
        "total": 2198,
        "isEnd": false
    },
    "data": [
        {
            "title": "카카오뱅크 사칭 피싱 사이트-<b>kakaobank</b>(.)cc(2022.10.1)",
            "content": "것을 볼 수가 있습니다. 카카오뱅크 피싱 사이트 웹소스 &lt;/script&gt; &lt;div id=&#34;wrapper&#34;&gt; &lt;div&gt;&lt;a href=&#34;/&#34;&gt;&lt;img src=&#34;./index_files/mm2022-03(.)jpg&#34; alt=&#34;<b>KAKAOBANK</b>채무통합 4대 보험에 가입되어 있나요? 그럼, 신청하세요!&#34;&gt;&lt;/a&gt;&lt;/div&gt; &lt;div&gt;&lt;img src=&#34;./index_files/main01-01)(.)jpg&#34; alt=&#34;<b>KAKAOBANK</b>채무통합 4...",
            "name": "꿈을꾸는 파랑새"
        }
    ]
}
```

### GET /keywords
블로그 검색에 사용된 키워드 중 가장 많이 사용된 10개를 조회합니다.

동일한 횟수로 사용된 키워드는 h2 DB를 통해 정렬된 키워드 순으로 조회합니다
#### Response
##### keywords
| Name     | Type    | Descripiton    |
|----------|---------|----------------|
| title    | String  | 검색에 사용한 키워드    |
| useCount | Integer | 해당 키워드가 사용된 횟수 |

#### Example
##### Request
```curl
curl --location --request GET 'localhost:8080/keywords'
```
##### Response
```json
{
  "keywords": [
    {
      "title": "카카오뱅크",
      "useCount": 4
    }
  ]
}
```

## 주요 구현 내용
- 블로그 검색 소스가 추가되는 경우를 고려
  - 블로그 검색 소스를 [추상화](src/main/java/com/example/blogfinder/domain/blog/BlogClient.java)하여, 검색 소스가 추가되더라도 이를 사용하는 [클라이언트](src/main/java/com/example/blogfinder/domain/blog/BlogFinder.java)에는 수정이 필요하지 않도록 설계
  - `@DependsOn`를 사용하여 검색 소스의 순서를 지정, 특정 검색 소스에 문제가 있으면 다음 검색 소스를 사용한다고 가정하였다.
- 트래픽이 많은 상황을 고려
  - 캐싱을 통해 성능 최적화
    - 블로그
      - 정확도순으로 조회할 경우, 데이터의 실시간성은 비교적 중요하지 않다고 생각하여 데이터를 TTL 1분으로 캐싱
      - 최신순으로 조회할 경우 해당 데이터는 캐싱하지 않는다.
    - 키워드
      - 키워드 데이터의 경우 별도의 TTL을 설정하지 않는다. 순위에 변동이 생길 경우에만 캐시를 지운다. 
- 동시성이 이슈가 발생하는 상황 고려
  - 키워드 사용 횟수를 업데이트할 경우 Optimistic Lock을 활용하여 동시성 이슈를 해소 

## 아쉬웠던 점 및 추가 고민해볼 부분
- 캐싱된 블로그 데이터에 중복이 발생할 수 있다.
  - `page=1&size=10` 결과와 `page=1&size=20`의 파라미터로 블로그를 검색할 경우 `page=1&size=10` 데이터는 중복되어 존재할 수 있다.
- 서로 다른 블로그 검색 소스의 데이터가 캐싱될 수 있다.
  - 카카오에서 검색한 `page=1&size=10` 결과가 캐싱되었다고 가정하고, 이후 `page=2&size=10` 조회를 시도할 때, 카카오에 에러가 발생할 경우 네이버에서 블로그를 검색하게되고 이 경우 `page=1&size=10` 는 카카오 결과가, `page=2&size=10`는 네이버에서 검색한 결과가 캐싱될 수 있다.
- 동시성 이슈에 대한 제어
  - Optimistic Lock을 사용하여 키워드의 사용 횟수에 비정상적인 값이 들어가는 것은 방지하였지만, 해당 요청에서 `HttpStatus 500` 응답이 내려간다.
  - 비록, 키워드 횟수 업데이트는 실패하였지만 블로그 검색 결과는 정상적으로 응답으로 내려줘야할 것이다.