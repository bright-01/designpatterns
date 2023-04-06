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
> * 



