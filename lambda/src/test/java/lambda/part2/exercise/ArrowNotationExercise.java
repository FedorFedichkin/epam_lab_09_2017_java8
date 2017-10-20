package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ArrowNotationExercise {

    /**
     * Simple method to get Person full name
     *
     * @param person Person for retrieving full name
     * @return Full name of Person
     */
    public String getFullName(Person person) {
        return person.getFirstName() + " " + person.getLastName();
    }

    @Test
    public void getAge() {
        // Person -> Integer
        // We can represent method call as a Function and store it in the variable
        final Function<Person, Integer> getAge = Person::getAge;

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // TASK: use BiPredicate
        BiPredicate<Person, Person> compareAges = new BiPredicate<Person, Person>() {
            @Override
            public boolean test(Person person, Person person2) {
                return person.getAge() == person2.getAge();
            }
        };

        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // Person -> String
        // Save link to method body
        final Function<Person, String> getFullName = this::getFullName;

        // (Person, Person) -> Integer
        // TASK: use ageOfPersonWithTheLongestFullName(getFullName)
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ageOfPersonWithTheLongestFullName(getFullName);

        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }

    /**
     * Get BiFunction for getting age of person with the longest full name
     *
     * @param getFullName Function for getting full name of person
     * @return Constructed BiFunction
     */
    private BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName(Function<Person, String> getFullName) {
        // Return constructed BiFunction with two parameters which apply input function for parameters
        return (p1, p2) -> {
            if ((getFullName.apply(p1).length() > getFullName.apply(p2).length())) {
                return p1.getAge();
            } else {
                return p2.getAge();
            }
        };
    }
}
