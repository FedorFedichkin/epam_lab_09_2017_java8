package lamda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class FunctionCombinationExercise {

    @Test
    public void personHasNotEmptyLastNameAndFirstName0() {
        // Person -> boolean
        final Predicate<Person> validate = p -> !p.getFirstName().isEmpty() && !p.getLastName().isEmpty();

        assertEquals(true, validate.test(new Person("a", "b", 0)));
        assertEquals(false, validate.test(new Person("", "b", 0)));
        assertEquals(false, validate.test(new Person("a", "", 0)));
    }

    // TODO
    // negate1: (Person -> boolean) -> (Person -> boolean)
    private Predicate<Person> negate1(Predicate<Person> test) {
        return person -> !test.test(person);
    }

    // TODO
    // validateFirstNameAndLastName: (Person -> boolean, Person -> boolean) -> (Person -> boolean)
    private Predicate<Person> validateFirstNameAndLastName(Predicate<Person> t1, Predicate<Person> t2) {
        return person -> t1.test(person) && t2.test(person);
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName1() {
        final Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        final Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        final Predicate<Person> validateFirstName = negate1(hasEmptyFirstName);
        final Predicate<Person> validateLastName = negate1(hasEmptyLastName);

        final Predicate<Person> validate = validateFirstNameAndLastName(validateFirstName, validateLastName);

        assertEquals(true, validate.test(new Person("a", "b", 0)));
        assertEquals(false, validate.test(new Person("", "b", 0)));
        assertEquals(false, validate.test(new Person("a", "", 0)));
    }

    // TODO
    // negate: (T -> boolean) -> (T -> boolean)
    private <T> Predicate<T> negate(Predicate<T> test) {
        // TODO
        return person -> !test.test(person);
    }

    // TODO
    // and: (T -> boolean, T -> boolean) -> (T -> boolean)
    private <T> Predicate<T> and(Predicate<T> t1, Predicate<T> t2) {
        // TODO
        return person -> t1.test(person) && t2.test(person);
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName2() {
        final Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        final Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        final Predicate<Person> validateFirstName = negate(hasEmptyFirstName); // TODO use negate
        final Predicate<Person> validateLastName = negate(hasEmptyLastName); // TODO use negate

        final Predicate<Person> validate = and(validateFirstName,validateLastName); // TODO use and

        assertEquals(true, validate.test(new Person("a", "b", 0)));
        assertEquals(false, validate.test(new Person("", "b", 0)));
        assertEquals(false, validate.test(new Person("a", "", 0)));
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName3() {
        final Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        final Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        final Predicate<Person> validateFirstName = hasEmptyFirstName.negate(); // TODO use Predicate->negate
        final Predicate<Person> validateLastName = hasEmptyLastName.negate(); // TODO use Predicate->negate

        final Predicate<Person> validate =validateFirstName.and(validateLastName); // TODO use Predicate->and

        assertEquals(true, validate.test(new Person("a", "b", 0)));
        assertEquals(false, validate.test(new Person("", "b", 0)));
        assertEquals(false, validate.test(new Person("a", "", 0)));
    }

}
