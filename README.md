# 디자인패턴

## 생성 패턴 ( Creational Patterns )

### 싱글톤 ( Singleton ) 패턴
* 인스턴스를 오직 한개만 제공하는 클래스
* 시스템 런타임, 환경 세팅에 대한 정보 등, 인스턴스가 여러개 일 때 문제가 생길 수 있음
* 인스턴스를 오잭 한개만 만들어 제공하는 클래스가 필요
* Singleton -- instance : Singleton | +getInstance(): Singleton
* ### 구현 방법
> #### private 생성자에 static 메소드
> <pre><code>private static Settings1 instance; 
>    private Settings1() { }
>
>    public static Settings1 getInstance() {
>        if (instance == null) {
>            instance = new Settings1();
>        }
>
>        return instance;
>    }
> </code></pre>
> 1. 생성자를 private으로 만드는 이유?   
> * 외부에서 인스턴스를 만드는 것을 차단 할 수 있다 ( new )
> 2. getInstance() 메소드를 static 으로 선언한 이유?   
> * 별도의 메모리 영역을 얻으면서 한번의 new 연산자로 인스턴스를 사용하기 때문에 메모리 낭비를 방지 - 최초 한번만 메모리에 할당
> 3. getInstance() 가 멀티쓰레드 환경에서 안전하지 않은 이유?   
>  * 동시성 문제
>  * 해결법은? synchronized , eager initialization, double checked locking, static inner, enum
> #### synchronized 를 사용해 멀티쓰레드 환경에서 안전하게 만드는 방법
> 
> <pre><code>public static synchronized Settings2 getInstance() {
>       if (instance == null) {
>           instance = new Settings2();
>       }
>       return instance;
>    } </code></pre>
> 1. 자바의 동기화 블럭 처리 방법?   
>  * 단 하나의 스레드만 처리 할 수 있도록 임계 영역 ( critical section )이라 한다. 자바는 임계 역역을 지정하기 위해 동기화 블록을 제공하는데 synchronized 메소드를 통해 처리.   
>  * synchronized가 메소드가 호출된 시점부터 해당 메소드가 포함된 객체의 Lock을얻어 작업을 수행 하다가 메소드가 종료되면 Lock를 반환함 
> 2. getInstance() 메소드 동기화 시 사용하는 락(lock)은 인스턴스의 락인가 클래스의 락인가? 그 이유는?   
>  * 클래스 락. 인스턴스락 이라면 동기화 시 하나의 객체를 보장할 수 없음.   
>  * static 메서드에 synchronized를 걸면 class단위로 lock가 걸림
>  * 객체가 lock가 걸려버렸다? 객체가 
> #### 이른 초기화 ( eager initialization )을 사용하는 방법
> <pre><code> private static volatile Settings3 instance;
>    private Settings3() { }
>    public static Settings3 getInstance() {
>        if (instance == null) {
>            synchronized (Settings3.class) {
>                if (instance == null) {
>                    instance = new Settings3();
>                }
>            }
>        }
>        return instance;
>    } </code></pre>
> 1. 이른 초기화가 단점이 될 수도 있는 이유?
> *  메모리 할당 시 생성 되므로 리소스가 많이 필요하다면 효율이 떨어짐    
> 2. 만약에 생성자에서 checked 예외를 던진다면 이코드는 어떻게 변경해야 할까?
> * try-catch를 통해 예외 처리를 함. 안하면 이른 초기화 불가능
> 
> #### double checked locking으로 효율적인 동기화 블럭 만들기
> 
> <pre><code> public static Settings getInstance() {
>    if (instance == null) {
>        synchronized (Settings.class) {
>            if (instance == null) {
>                instance = new Settings();
>            }
>        }
>    }
>    return instance;
> }</code></pre>
> 1. double check locking 이라고 부르는 이유?
> * 인스턴스 생성 여부 체크, synchronized 블럭에서 한번 더 체크
> * 메서드에 synchronized를 제외하여 오버헤드를 줄이고, 인스턴스에 대한 null 체크 synchronized 밖에서 하고, 안에서 null체크.
> 2. instance 변수는 어떻게 정의 되는가? 이유는? 
> * volatile 키워드를 사용
> * Multi Thread 환경에서 하나의 Thread만 read&write하고 나머지 Thread에서 read하는 상황에서 가장 최신값을 보장   
> 인스턴스를 메인 메모리에 저장하고 읽기 때문에 값 불일치 문제를 해결   
> 접근 성능은 느리지만, 변수의 접근(read, write)에 대해 정합성을 보장
> 
> #### static inner 클래스를 사용하는 방법
> <pre><code>private Settings() {}
> private static class SettingsHolder {
>   private static final Settings SETTINGS = new Settings();
> }
> public static Settings getInstance() {
>   return SettingsHolder.SETTINGS;
> }</code></pre>
> 1. 이 방법은 static final를 썼는데도 왜 지연 초기화(lazy initialization)라고 볼 수 있는가?
> * inner class는 클래스가 처음 사용될 때 초기화가 수행 - app이 로드 될 때 실행 되지 않음
> * static 필드는 클래스가 처음 로딩될 때 정적인 메모리 공간에 만들어짐
> holder가 가지고 있는 클래스가 로딩되는 시점은 getInstance()를 호출할 때 로딩되기 때문에 lazy-initialization
> * getInstance()를 호출하는 시점에 inner class인 SettingsHolder에 접근하게 되고, 이때 초기화가 수행되기 때문에 지연 초기화처럼 수행
> 
> #### 싱글톤 패턴 구현을 깨뜨리는 방법 1 - 리플렉션
> <pre><code>Settings settings = Settings.getInstance();
> Constructor<Settings> declaredConstructor = Settings.class.getDeclaredConstructor();
> declaredConstructor.setAccessible(true);
> Settings settings1 = declaredConstructor.newInstance();
> System.out.println(settings == settings1);</code></pre>
> 1. 리플렉션에 대해 설명
> * 구체적인 클래스의 타입을 몰라도 안에 선언되어 있는 함수, 변수들에 접근을 할 수 있는 자바의 API
> 2. setAccessible(true)를 사용 하는 이유
> * 기본생성자는 private.. -> 외부 호출 불가능 -> setAccessible(true)를 통해 Constructor<Settings5> 타입으로 받는 declaredConstructor,   
> * 기본 생성자를 사용가능하게 해 newInstance()사용해새로운 객체를 만들 수 있게하기 때문
> ### 싱글톤(Singleton) 패턴 구현을 깨뜨리는 방법 2 - 직렬화 & 역직렬화
> <pre><code>Settings settings = Settings.getInstance();
> Settings settings1 = null;
> try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
> out.writeObject(settings);
> }
> try (ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))) {
> settings1 = (Settings) in.readObject();
> }
> System.out.println(settings == settings1);</code></pre>
> 1. 자바의 직렬화 & 역직렬화에 대해설명
> * 직렬화 : 자바 시스템 내부에서 사용되는 Object 또는 Data를 외부의 자바 시스템에서도 사용할 수 있도록 byte형태로 변환.
> * 역직렬화 : byte로 변환된 data를 원래대로 Object나 dat로 변환하는 기술 
> 2. Serializableld란? 그 사용 이유?
> * Srializable를 상속받는 경우 클래스의 버전관리를 위해 serialVersionUID를 사용한다.   
>   이 serialVersionUID 변수를 명시적으로 선언해 주지 않으면 컴파일러가 계산한 값을 부여하는데 Serialzable Class 또는 Outer Class에 변경이 있으면 SerialVersionUID 값이 바뀌게 된다.   
>   만약 Serialize 할 때와 Deserialize 할때 SerialVersionUID 값이 다르면 InvalidClassExceptions가 발생하고 저장된 값을 객체로 Restore 할 수 없다.
> 3. try-resource 블럭에 대해 설명
> * try-resource 블럭은 기존의 try-catch-final 블럭에서 사용하고 꼭 종료해줘야 하는 resource를 사용할 때 final 블럭에서 resource를 해제하는데, try-resource 블럭을 사용하면 따로 명시적으로 resource를 해제해주지 않아도 자동으로 해제해 준다
> ### 싱글톤 패턴을 구현 하는 방법 6 - enum
> <pre><code>public enum Settings {
>    INSTANCE;
> }</code></pre>
> 1. enum 타입의 인스턴스를 리플렉션을 통해 만들 수 있는가?
> * 없음. enum 타입은 리플렉션을 할 수 없도록 막혀있음
> 2. enum으로 싱글톤 타입을 구현할 때의 단점은?
> * 클래스가 메모리에 할당되는 시점에 인스턴스가 미리 만들어진다. 초기화 시점이 문제가 되지 않는다면 가장 안전한 방법
> 3. 직렬화 & 역직렬화 시에 별도로 구현해야 하는 메소드가 있는가?
> * enum 타입은 enum 클래스를 상속받는데, 이 클래스는 Serializable을 이미 구현하고 있기 때문에 추가적인 구현이 필요없다

