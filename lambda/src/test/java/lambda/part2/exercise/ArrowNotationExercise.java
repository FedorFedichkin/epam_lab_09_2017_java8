package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ArrowNotationExercise {

    @Test
    public void getAge() {
        // Person -> Integer
        final Function<Person, Integer> getAge = Person::getAge; // TODO

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean

        final BiPredicate<Person, Person> compareAges = (a, b) -> a.getAge() >= b.getAge();
        //throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    @Test
    public void getFullName() {
        final Function<Person, String> getFullName = a -> a.getFirstName() + " " + a.getLastName();
        assertEquals("a b", getFullName.apply(new Person("a", "b", 22)));
    }

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> ((Person, Person) -> int)
    @Test
    public void ageOfPersonWithTheLongestFullName() {
        final BiPredicate<Person, Person> compareAges = (a, b) -> a.getAge() >= b.getAge();
        //throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    private String getFullName(Person p){
        return p.getFirstName() + " " + p.getLastName();
    }

    private BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName(Function<Person, String> toFullName){
        return (o1, o2) -> toFullName.apply(o1).length() > toFullName.apply(o2).length() ? o1.getAge() : o2.getAge();
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String

        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ageOfPersonWithTheLongestFullName(this::getFullName);

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
