package me.mwcheon.designpatterns._01_creational_patterns;

public class Settings {
    /**
     * private 생성자
     * 외부에서 new를 통해 객체를 생성 하지 못 한다
     *
     */

    // 1. 가장 간단하게 만드는 방법
//    private static Settings instance;

    // 2. 이른 초기화로 미리 인스턴스를 생성한다. 바로 return 해버림
    // 멀티쓰레드에서도 안전함
    // 단점은 미리 만든다.. 비용이 많이 들거나 안쓴다면? --> getInstance를 호출 할때 만들면 좋을 텐데..
//    private static final Settings INSTANCE = new Settings();

     // 3. volatile를 사용해 double checked locking 처리
//    private static volatile Settings instance;

        // 4. static inner 클래스 사용
    private static class SettingsHolder{
        private static final  Settings INSTANCE = new Settings();
    }

    private Settings(){}

    public static Settings getInstance(){
//        return new Settings(); // 이렇게 사용해도 안된다.. new를 통해 선언된 객체는 항상 다르기 때문..
        // 아래의 방법은 멀티 쓰레드에서 사용하면 위험하다
//        if(instance == null){
//            instance = new Settings();
//        }

        // 1. 그래서 synchronized 사용해 동기화를 이슈를 처리 할 수 있는데 단점은 성능 이슈가 발생함.

        //3. 그래서 나온 것이 double checked locking
//        if (instance == null) {
//            // 다른 쓰레드가 동시에 여기로 진입 했을 경우
//            // 아래의 synchronized 에서는 하나의 쓰레드만 진입하고 instance null 체크 후 if 문을 빠져 나간다
//            // 다음에 들어오는 쓰레드는 instance가 null 이 아니기 때문에 그냥 빠져 나감
//            // 여기서 volatile라는 키워드를 써줘야지 완성 된다
//            synchronized (Settings.class) {
//                if (instance == null) {
//                    instance = new Settings();
//                }
//            }
//        }


//        return instance;
        // return INSTANCE;

        return SettingsHolder.INSTANCE;

    }

    protected Object readResolve(){
        return getInstance();
    }
}
