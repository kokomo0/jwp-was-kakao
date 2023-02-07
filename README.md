# 웹 애플리케이션 서버

---

## 1. GET /index.html 응답하기
- [x] http://localhost:8080/index.html에 접근할 수 있도록 구현한다.
- [x] RequestHandlerTest 테스트가 모두 통과하도록 구현한다.

```
* HTTP Request Header 예

GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

#### 구현 목록
- [x] Request Header를 파싱한다.
- [x] 요청에 맞는 URL로 연결한다.
- [x] 파일 읽어 응답을 만든다.

### 2. CSS 지원하기
- [x] Stylesheet 파일을 지원하도록 구현하도록 한다.

```
* HTTP Request Header 예

GET ./css/style.css HTTP/1.1
Host: localhost:8080
Accept: text/css,*/*;q=0.1
Connection: keep-alive
```

#### 구현 목록
- [x] resources/static 하위 Stylesheet 파일을 지원하도록 한다.

### 3. Query String 파싱
- [x] “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다.
- [x] 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
- [x] HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
- [x] 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.
```
* HTTP Request Header 예

GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
#### 구현 목록
- [x] 쿼리를 파싱한다.
- [x] User 객체를 만든다.
- [x] User 객체를 데이터베이스에 저장한다.

### 4. POST 방식으로 회원가입
- [x] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.

```
* HTTP Request Header 예

POST /user/create HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*

userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com
```

#### 구현 목록
- [x] Request Body를 파싱한다.
- [x] 3과 같이 User 객체를 만들어 데이터베이스에 저장한다.

### 5. Redirect
- [x] 현재는 “회원가입”을 완료 후, URL이 /user/create 로 유지되는 상태로 읽어서 전달할 파일이 없다. redirect 방식처럼 회원가입을 완료한 후 index.html로 이동해야 한다.

#### 구현 목록
- [x] 302 응답 헤더를 이용한다.
