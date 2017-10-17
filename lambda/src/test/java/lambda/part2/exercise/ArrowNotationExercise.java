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
        // TODO
        // Person -> Integer
        final Function<Person, Integer> getAge = Person::getAge;
        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TODO use BiPredicate
        // compareAges: (Person, Person) -> boolean
        final BiPredicate<Person, Person> compareAges = (person1, person2) -> person1.getAge() == person2.getAge();
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    private String getFullName(Person person) {
        return person.getFirstName() + " " + person.getLastName();
    }

    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> int
    private int ageOfPersonWithTheLongestFullName(Person person1, Person person2) {
        return getFullName(person1).length() > getFullName(person2).length() ? person1.getAge() : person2.getAge();
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // TODO
        // Person -> String
        final Function<Person, String> getFullName = this::getFullName;
        // TODO use ageOfPersonWithTheLongestFullName(getFullName)
        // (Person, Person) -> Integer
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName =
                this::ageOfPersonWithTheLongestFullName;
        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
