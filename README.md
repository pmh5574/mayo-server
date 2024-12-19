## Project Convention

- 카멜
- 메서드 명은 get / post / update / delete로 통일
- 타입 추론이 되지 않는 코드는 지양
- 모든 의존성은 내부로

### 간단한 개념 설명

```
├── MayoApplication.java
├── adapter
│   ├── UserController.java
│   ├── in
│   │   └── web
│   └── out
│       └── persistence
├── application
│   └── port
│       ├── in
│       └── out
├── common
│   ├── Constants.java
│   ├── annotation
│   ├── aspect
│   ├── config
│   │   ├── QueryDslConfig.java
│   │   └── RedisConfig.java
│   └── exception
├── domain
│   ├── model
│   └── repository
└── infrastructure
```

- adapter(Interface Adapters)
  - in/web : 밖에서부터 안으로 들어오는 (In), web (Request Dto)
  - out/persistence : 영속성 계층으로부터 밖으로 나가는 (out) (Response Dto, Response Mapper)
    - 데이터 베이스와 상호작용하는 출력 어댑터가 배치될 수 있다.
    - 데이터를 애플리케이션의 Use Case와 Entity가 사용하기 편리한 형태로 변환하거나, 그 반대로 변환한다. 즉, 외부 데이터와 내부 데이터 구조 간의 변환을 담당한다.
    - 외부의 데이터가 애플리케이션 내부에서 사용될 수 있도록 하고, 애플리케이션 내부의 데이터를 외부 시스템에서 이해할 수 있도록 변환한다. 프레젠테이션 레이어로도 표현될 수 있다.
- application
  - port
    - in : 
      - application으로 들어오는 interface(입력 포트)가 정의되어 있다.
      - 입력 포트(Input inteface)에 대한 실제 구현부(기능에 대한 비즈니스 로직(usecase))이 위치할 수 있다.
      - usecase, useCaseImpl, commandHanlder, command, commandValidator ...
    - out
      - application에서 나가는 interface(출력 포트)가 정의되어 있다.
      - usecase, useCaseImpl, queryHandler, query ...
    - 애플리케이션의 핵심 비즈니스 로직과 규칙을 포함하지 않지만, 필수적인 비즈니스 로직을 구현한다. 이 계층은 Use Case를 통해 도메인의 핵심 비즈니스 로직을 호출하고, 애플리케이션의 특정 사용 사례를 처리한다.
    - 애플리케이션의 특정 기능을 구현하며, 도메인 계층의 핵심 비즈니스 로직을 호출한다. 애플리케이션 레이어로 표현될 수 있다.
- common
  - 공용 폴더가 위치할 수 있다.
  - config.. asepct.. annotation etc..
- domain
  - Project의 핵심 비즈니스 로직과 모델이 존재한다.
    - repository, entity, business entity..
  - 혹은 db 실제 구현부에 대한 추상화 계층으로 동작하도록 만들 수 있다.
  - 프로젝트의 핵심 비즈니스 규칙이나 도메인 별 핵심 비즈니스 규칙을 보유한다. 변경 가능성이 가장 낮으며, 잘 설계된 스키마(모델)는 오랜 기간 동안 변경되지 않는다.
  - 애플리케이션의 비즈니스 로직을 포함하며, 외부 계층(Frameworks & Drivers, Interface Adapters 등)의 변경에 영향을 받지 않아야 한다.
- infrastructure(Frameworks & Drivers)
  - 외부 시스템과의 인터페이스를 담당하는 가장 바깥쪽 계층이다. 이는 데이터베이스, 외부 API, 클라우드 서비스(예: AWS SDK), 서드파티 라이브러리 등을 포함한다.
  - 이 계층은 애플리케이션의 외부와 상호작용하며, 애플리케이션의 나머지 부분에 대한 직접적인 접근을 제공하지 않는다. 인프라스트럭처 계층으로 표현될 수 있다.

### 의존성 역전 원칙

모든 종속성은 내부로 향해야 한다. 만약 외부에 내부에서 외부로 향하는 의존이 발견된다면,
의존하지 않도록 개선하는게 좋다. 

### Design Pattern 관련

- Clean Architecture
  - 라인 엔지니어링 포트와 어댑터 아키텍처 구성 (https://engineering.linecorp.com/ko/blog/port-and-adapter-architecture)
  - https://betterprogramming.pub/the-clean-architecture-beginners-guide-e4b7058c1165
  - https://github.com/wikibook/clean-architecture/tree/main?tab=readme-ov-file
  - https://inblog.ai/vlogue/clean-architecture%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%9D%98%EC%A1%B4%EC%84%B1-%EA%B7%9C%EC%B9%99%EC%9D%84-%EC%A7%80%ED%82%AC-%EC%88%98-%EC%9E%88%EC%9D%84%EA%B9%8C-26869?traffic_type=internal
- Hexagonal Architecture
  - https://reflectoring.io/spring-hexagonal/
  - https://github.com/thombergs/buckpal


## Code 

- Controller Request는 Record
- Controller Response는 Record / static inner class
- Command(Write) DTO는 Record
- Query(Read) DTO도 Record 