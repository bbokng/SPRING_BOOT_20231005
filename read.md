# Spring Boot 기반 블로그 · 게시판 · 포트폴리오 웹사이트

본 프로젝트는 Spring Boot, JPA, Thymeleaf, Spring Security를 활용하여  
**블로그/게시판/회원 관리 기능**과 **포트폴리오 메인 페이지**를 통합 구현한 웹 애플리케이션입니다.  
기능 구현뿐 아니라, UI/UX 개선과 디자인 테마 적용을 통해 완성도를 높였습니다.

---

## 1. 프로젝트 주요 기능

### 1) 블로그 / 게시판 / 회원 기능 구축
- **엔티티 구성**: `Article`, `Board`, `Member`
- **JPA Repository** 기반 데이터 접근
- **Service / Controller 계층 분리**
- 블로그(Article): CRUD 기능 구현
- 게시판(Board):
  - 페이징 및 키워드 검색
  - 게시글 조회 / 작성 / 수정 / 삭제
- 회원(Member):
  - 회원 가입 / 로그인
  - 이메일 중복 검사
  - BCrypt 기반 비밀번호 암호화

---

### 2) Thymeleaf 기반 UI 구성
- 게시판: `board_list`, `board_view`, `board_write`, `board_edit`
- 블로그: `article_list`, `article_edit`
- 회원: `login`, `join_new`, `join_end`
- 메인/소개 페이지: `index.html`, `about_detailed.html`

---

### 3) 로그인 · 세션 · 보안 설정
- **Spring Security 설정 (SecurityConfig)**
  - `formLogin` + `/api/login_check`
  - remember-me 기능
  - 커스텀 로그인 성공/실패 핸들러
  - 로그아웃: `/api/logout`
  - 세션 만료 페이지: `/session-expired`
  - `X-XSS-Protection` 헤더 추가
- **CSRF 정책**
  - 기본 프로필: CSRF 활성화
  - `dev` 프로필: 개발 편의상 CSRF 비활성화
- **세션 저장 구조**
  - 로그인 성공 시 `userId`, `email`
  - 사용자별 세션 키 분리:
    - `userId:{email}`, `email:{email}`
- **세션 정책**
  - 동시 사용자 허용  
    (`maximumSessions(-1)`, `maxSessionsPreventsLogin(false)`)

---

### 4) 게시판 접근 세션 체크
- `BlogController.boardList`
  - 세션에 `userId` 없을 경우 `/member_login`으로 리다이렉트
  - 기존 `page`, `keyword` 파라미터 유지
- 세션의 `email`을 모델로 전달하여  
  게시판 상단에 **환영 문구** 표시

---

### 5) 메일 업로드 (파일 업로드) 기능
- `POST /upload-email`
  - 메일 내용을 `.txt` 파일로 저장
  - 다중 파일 업로드 지원
  - 파일명 중복 시 **타임스탬프 + UUID**로 새 이름 생성
- 업로드 경로:
  - `spring.servlet.multipart.location=upload`
  - 실행 시 자동 생성
- 업로드 결과 페이지:
  - 성공: `upload_end.html`
  - 실패: `error_page/upload_error.html`
- 메인 페이지 문의 폼을 **메일 업로드 폼**으로 교체
  - email / subject / message + 파일 첨부
  - 버튼 문구: “메일 보내기”

---

### 6) 세션/쿠키 처리 (단일 사용자 제한 해제)
- 로그인 시:
  - 기존 세션 무효화 후 새 세션 생성
  - `JSESSIONID` 쿠키 초기화
- 로그아웃 (`GET /api/logout`)
  - 현재 세션 invalidate
  - 쿠키 삭제 후 새 세션 상태 로그 출력

---

## 2. 디자인 및 UI 개선 (추가 구현)

### 1) 한국어 번역 정비
- 메인/소개 페이지의 영어 슬로건 및 소개 문구를  
  자연스러운 한국어로 번역

### 2) 핑크톤 전역 테마 적용
- `static/css/theme-pink.css` 추가
- 모든 템플릿에서 공통 로드
- 버튼 / 폼 / 테이블 / 카드 / 네비 / 푸터에 핑크 포인트 적용
- 팀원 프로필 이미지용 스타일:
  - `team-avatar` (160x160, 원형, object-fit: cover)

### 3) 팀원 소개 프로필 이미지 교체
- 이미지 경로:
  - `static/img/team/member1.png`
  - `static/img/team/member2.png`
  - `static/img/team/member3.jpg`
- `index.html` 팀원 소개 섹션 이미지 교체
- alt 텍스트 통일 및 `team-avatar` 적용

### 4) 지도(프로필 수정하기) 변경
- `index.html` Map 섹션의 Google Maps iframe을  
  지정된 위치의 퍼가기 코드로 교체
- 폭 `100%`, 높이 `450px`, lazy loading 적용

---

## 3. 실행 방법

```bash
cd demo
./mvnw -q -DskipTests package
./mvnw spring-boot:run
접속: http://localhost:8080
```

---

## 4. 현재 상태
- 빌드 성공
- 주요 화면(메인 / 로그인 / 게시판 / 글쓰기 / 수정 / 업로드) 정상 동작
- 기능 변경 없이 스타일 및 UI 개선 완료

---

## 5. 추가 확인 사항
- 팀원 사진 교체 시 `static/img/team/member*.{png,jpg}` 파일만 덮어쓰기
- 업로드 폴더(`upload`) 쓰기 권한 확인
- 실행 환경(dev/prod)에 따른 CSRF 동작 점검

---

## 교수님께 드리는 말씀
한 학기 동안 본 수업을 통해 Spring Boot 기반 웹 애플리케이션의 전반적인 구조와 흐름을 직접 구현하며 깊이 있게 이해할 수 있었습니다.

단순한 기능 구현을 넘어, 보안·세션 관리·UI 개선까지 경험할 수 있었던 점이 특히 의미 있었습니다. 배운 내용을 바탕으로 앞으로도 더 발전해 나가겠습니다.

그동안 열정적으로 지도해 주셔서 진심으로 감사드립니다.

감사합니다.
