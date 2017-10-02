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
            System.out.println("before print");
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

    @Test
    public void closure_this_lambda() {
        _person = new Person("John", "Galt", 33);

        runFromCurrentThread(() -> /*this.*/_person.print());
        runFromCurrentThread(/*this.*/_person::print);

        _person = new Person("a", "a", 1);

    }


    private Runnable runLater(Runnable r) {
        return () -> {
            System.out.println("before run");
            r.run();
        };
    }


    @Test
    public void closure_this_lambda2() {
        _person = new Person("John", "Galt", 33);

        //final Person person = _person;
        final Runnable r1 = runLater(() -> _person.print());
        final Runnable r2 = runLater(get_person()::print);

        _person = new Person("a", "a", 1);

        r1.run();
        r2.run();

    }

}
