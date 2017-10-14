package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ArrowNotationExercise {

    private static boolean compareAges(Person person1, Person person2) {
        return (person1.getAge() == person2.getAge());
    }

    @Test
    public void getAge() {
        // Person -> Integer
//        final Function<Person, Integer> getAge = person -> person.getAge();
        final Function<Person, Integer> getAge = Person::getAge;

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    // TODO use BiPredicate
    // compareAges: (Person, Person) -> boolean
    @Test
    public void compareAgesTest() {
//        BiFunction<Person, Person, Boolean> compareAges = ((person1, person2) -> compareAges(person1, person2));
        BiFunction<Person, Person, Boolean> compareAges = ArrowNotationExercise::compareAges;

//        throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.apply(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> ((Person, Person) -> int)
    //

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        final Function<Person, String> getFullName = person -> person.getFirstName() + " " + person.getLastName();

        // (Person, Person) -> Integer
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName =
                (person1, person2) -> getFullName.apply(person1).length() > getFullName.apply(person2).length() ? person1.getAge() : person2.getAge();

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
