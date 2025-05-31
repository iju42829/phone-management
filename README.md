# 휴대폰 주문 관리 시스템

## 프로젝트 개요
- 본 프로젝트는 LGU+ 유레카 SW 교육과정과 개인 학습을 바탕으로, 이론을 실무에 적용해보며 실제 개발 경험을 쌓기 위해 진행했습니다.
- 기존에 학습한 내용을 점검하고 부족한 부분을 보완하는 동시에, 프로젝트 과정에서 새로운 기술과 개념을 익히며 성장하는 것을 목표로 삼았습니다.

## 기술 스택
| 분류         | 기술                                     |
|------------|----------------------------------------|
| Language   | Java 17                                |
| Framework  | Spring Boot                            |
| Template   | Thymeleaf                              |
| Database   | MySQL (Main), H2 (Test), Redis (Cache) |
| ORM        | JPA                                    |
| Test       | JUnit 5                                |
| Infra      | EC2, S3, VPC, ECR, Code Deploy         |
| Deployment | Github Actions(CI/CD), Docker          |

## 주요 구현 내용
1. Entity-DTO 간 변환을 위한 Mapper 클래스 구현
2. JUnit과 Mockito를 활용한 단위 테스트 코드 작성
   - 총 154개의 테스트 작성 및 테스트 커버리지 96% 달성
3. MySQL 실행 계획 분석 및 인덱싱 적용으로 조회 성능 개선 (705ms → 171ms)
4. Redis Cache Aside 전략 적용으로 자주 접근하는 페이지 성능 개선 (171ms → 53ms)
5. 파사드 패턴 도입을 통한 주문-장바구니 로직의 트랜잭션 일관성 및 롤백 처리
6. 낙관적 락 적용으로 재고 동시성 문제 해결
7. VPC를 Public/Private Subnet으로 분리하고, Spring Boot 애플리케이션은 Public Subnet에, Redis와 MySQL은 Private Subnet에 배치하여 보안을 강화
8. Github Actions와 AWS CodeDeploy를 활용한 CI/CD 자동 배포 파이프라인 구축

## 아키텍처
![architecture](https://github.com/user-attachments/assets/8532e740-8635-44da-beb0-250c53f6fa7a)

## 네트워크 아키텍처
![network architecture](https://github.com/user-attachments/assets/16ea3623-fd34-4fb6-8f8e-78996b316dd3)

## CI/CD 아키텍처
![ci cd architecture](https://github.com/user-attachments/assets/f08fd731-df8f-4670-8173-f88d7fb73207)

## ERD
![erd](https://github.com/user-attachments/assets/f1bf227a-4b9e-45d4-8c50-28c2298b9566)

## 프로젝트 실행 방법
1. `.env` 파일 생성  
   루트 디렉토리에 `.env` 파일을 생성하고 아래 내용을 입력합니다
    ```env
    SERVER_PORT=
    DB_USERNAME=
    DB_PASSWORD=
    DB_PORT=
    DB_NAME=
    DB_URL=

    REDIS_HOST=
    REDIS_PORT=
    ```
2. Docker Compose로 실행  
   다음 명령어로 컨테이너를 빌드하고 실행합니다

   ```
   docker-compose up --build
   ```
