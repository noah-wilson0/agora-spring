# Redis 설명

## Docker Download
https://www.docker.com/

Download Docker Desktop

Download for windows - AMD64 버전 설치

기본옵션으로 설치 후 재시작

    docker --version
으로 설치 확인

## Redis 실행
    docker run --name redis -p 6379:6379 -d redis
입력 하면 redis image를 확인 후 없으면 설치

    docker start redis
생성 후엔 start로 키면 됨

    docker ps
redis 컨테이너가 STATUS: UP 이면 실행 중

## Redis 접속 테스트
    docker exec -it redis redis-cli
입력 하면 redis-cli 프롬프트 진입

```
set foo bar 
get foo
```
> bar

출력 되면 완료

## Spring 에서 Redis 연결 정보
호스트 : localhost

포트 : 6379

## Redis 종료
    docker stop redis

# Redis setting (react)

    npm install sockjs-client @stomp/stompjs
