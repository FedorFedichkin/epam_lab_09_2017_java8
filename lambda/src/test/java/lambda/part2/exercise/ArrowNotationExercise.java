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
        final Function<Person, Integer> getAge = p -> p.getAge(); // done

        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // done
        BiPredicate<Person,Person> compareAges = (person, person2) -> (person.getAge() - person2.getAge()) == 0;
        // compareAges: (Person, Person) -> boolean

        //throw new UnsupportedOperationException("Not implemented");
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // TODO
    // getFullName: Person -> String
    public Function<Person,String> getFullName() {
        return p -> p.getFirstName() + p.getLastName();
    }


    // TODO
    // ageOfPersonWithTheLongestFullName: (Person -> String) -> ((Person, Person) -> int)
    //
    public BiFunction<Person,Person,Integer> getAgeOfPersonWithTheLongestFullName (final Function<Person,String> personStringFunction) {
        return (p1,p2) -> {
            if(personStringFunction.apply(p1).length() > personStringFunction.apply(p2).length()) {
                return p1.getAge();
            } else {
                return p2.getAge();
            }
        };
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullNameTest() {
        assertEquals(
                Integer.valueOf(1),
                getAgeOfPersonWithTheLongestFullName(getFullName()).apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
