package lambda.part1.example;

import data.Person;
import org.junit.Test;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public class Lambdas05 {
    private <T> void printResult(T t, Function<T, String> function) {
        System.out.println(function.apply(t));
    }

    private final Person person = new Person("John", "Galt", 33);

    @Test
    public void printField() {
        //объект person типа Person, который указан первым параметром, неявно становится первым аргументом тут:
        //Function<T, String> function.
        //Person::getLastName здесь выступает в роли Function<T, String> function, которая из неявно переданного
        //объекта типа Person "достает" объект типа String при помощи метода getLastName
        printResult(person, Person::getLastName);

//        BiFunction<Person, String, Person> changeFirstName = Person::withFirstName;

//        printResult(changeFirstName.apply(person, "newName"), Person::getFirstName);
    }


    private static class PersonHelper {
        public static String stringRepresentation(Person person) {
            return person.toString();
        }
    }


    @Test
    public void printStringRepresentation() {
        printResult(person, PersonHelper::stringRepresentation);
    }

    @Test
    public void exception() {
        Runnable r = () -> {
            //Thread.sleep(100);
            person.print();
        };

        r.run();
    }

    @FunctionalInterface
    private interface DoSomething {
        void doSmth();
    }

    private void conflict(Runnable r) {
        System.out.println("Runnable");
        r.run();
    }

    private void conflict(DoSomething d) {
        System.out.println("DoSomething");
        d.doSmth();
    }

    private String printAndReturn() {
        person.print();
        return person.toString();
    }

    @Test
    public void callConflict() {
        //conflict(this::printAndReturn);
    }

    private interface PersonFactory {
        Person create(String name, String lastName, int age);
    }

    private void withFactory(PersonFactory pf) {
        pf.create("name", "lastName", 33).print();
    }

    @Test
    public void factory() {
        withFactory(Person::new);
    }
}
