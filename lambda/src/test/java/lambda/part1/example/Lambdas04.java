package lambda.part1.example;

import data.Person;
import org.junit.Test;

@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
public class Lambdas04 {

    private void runFromCurrentThread(Runnable r) {
        r.run();
    }

    @Test
    public void closure() {
        Person person = new Person("John", "Galt", 33);

        runFromCurrentThread(new Runnable() {
            @Override
            public void run() {
                person.print();
            }
        });

        //person = new Person("a", "a", 44);
    }


    @Test
    public void closure_lambda() {
        Person person = new Person("John", "Galt", 33);

        // statement lambda
        runFromCurrentThread(() -> {
            System.out.println("Before print");
            person.print();
        });
        // expression lambda
        runFromCurrentThread(() -> person.print());
        // method reference
        runFromCurrentThread(person::print);
    }

    private Person _person = null;

    public Person get_person() {
        return _person;
    }

    static void staticMethod() {
        Runnable run = () -> {
            System.out.println();
        };
    }

    void nonStaticMethod() {
        Runnable run = () -> {
            System.out.println(this);
        };
    }

    @Test
    public void closure_this_lambda() {
        _person = new Person("John", "Galt", 33);

        //lambda expression; замыкается на this
        runFromCurrentThread(() -> /*this.*/_person.print());
        //method-reference; замыкается на конкетном объекте Person, даже если указать this явно
        runFromCurrentThread(/*this.*/_person::print);

        _person = new Person("a", "a", 1);

        runFromCurrentThread(() -> /*this.*/_person.print());
        runFromCurrentThread(/*this.*/_person::print);
    }


    //runnable будет вызван не сразу, а будет просто сформирована новая лямбда
    private Runnable runLaterFromCurrentThread(Runnable runnable) {
        return () -> {
            System.out.println("before run");
            runnable.run();
        };
    }


    @Test
    public void closure_this_lambda2() {
        _person = new Person("John", "Galt", 33);

        //замыкаемся на this
        final Runnable r1 = runLaterFromCurrentThread(() -> _person.print());
        //замыкаемся на самом Person
        final Runnable r2 = runLaterFromCurrentThread(get_person()::print);

        _person = new Person("a", "a", 1);

        //а, а, 1, так как на момент вызова поле объекта, на который ведет ссылка _person, уже поменялось
        r1.run();
        //John, Galt, 33, так как выше замкнулись на самом объекте Person. Не смотря на то, что ссылка _person уже
        //указывает на другой объект, будет выведена инфа о первом объекте Person
        r2.run();

    }

}
