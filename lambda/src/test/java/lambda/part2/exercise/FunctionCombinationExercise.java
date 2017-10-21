package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionCombinationExercise {

    @Test
    public void personHasNotEmptyLastNameAndFirstName0() {
        // Person -> boolean
        Predicate<Person> validate = p -> !p.getFirstName().isEmpty() && !p.getLastName().isEmpty();

        assertTrue(validate.test(new Person("a", "b", 0)));
        assertFalse(validate.test(new Person("", "b", 0)));
        assertFalse(validate.test(new Person("a", "", 0)));
    }

    // TODO // done
    // negate1: (Person -> boolean) -> (Person -> boolean)
    private Predicate<Person> negate1(Predicate<Person> personPredicate) {
        return p -> !personPredicate.test(p);
//            throw new UnsupportedOperationException();
    }

    // TODO // done
    // validateFirstNameAndLastName: (Person -> boolean, Person -> boolean) -> (Person -> boolean)
    private Predicate<Person> validateFirstNameAndLastName(Predicate<Person> predicate1, Predicate<Person> predicate2) {
        return p -> predicate1.test(p) && predicate2.test(p);

//            throw new UnsupportedOperationException();
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName1() {
        Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        Predicate<Person> validateFirstName = negate1(hasEmptyFirstName);
        Predicate<Person> validateLastName = negate1(hasEmptyLastName);

        Predicate<Person> validate = validateFirstNameAndLastName(validateFirstName, validateLastName);

        assertTrue(validate.test(new Person("a", "b", 0)));
        assertFalse(validate.test(new Person("", "b", 0)));
        assertFalse(validate.test(new Person("a", "", 0)));
    }

    // TODO // done
    // negate: (T -> boolean) -> (T -> boolean)
    private <T> Predicate<T> negate(Predicate<T> predicate) {
       return t -> !predicate.test(t);
//        throw new UnsupportedOperationException();
    }

    // TODO // done
    // and: (T -> boolean, T -> boolean) -> (T -> boolean)
    private <T> Predicate<T> and(Predicate<T> t1, Predicate<T> t2) {
        return t -> t1.test(t) && t2.test(t);
//        throw new UnsupportedOperationException();
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName2() {
        Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        Predicate<Person> validateFirstName = negate(hasEmptyFirstName); // TODO use negate // done
        Predicate<Person> validateLastName = negate(hasEmptyLastName); // TODO use negate // done

        Predicate<Person> validate = and(validateFirstName, validateLastName); // TODO use and // done

        assertTrue(validate.test(new Person("a", "b", 0)));
        assertFalse(validate.test(new Person("", "b", 0)));
        assertFalse(validate.test(new Person("a", "", 0)));
    }

    @Test
    public void personHasNotEmptyLastNameAndFirstName3() {
        Predicate<Person> hasEmptyFirstName = p -> p.getFirstName().isEmpty();
        Predicate<Person> hasEmptyLastName = p -> p.getLastName().isEmpty();

        Predicate<Person> validateFirstName = p -> hasEmptyFirstName.negate().test(p); // TODO use Predicate->negate
        Predicate<Person> validateLastName = p -> hasEmptyLastName.negate().test(p); // TODO use Predicate->negate

        Predicate<Person> validate = p -> validateFirstName.and(validateLastName).test(p); // TODO use Predicate->and

        assertTrue(validate.test(new Person("a", "b", 0)));
        assertFalse(validate.test(new Person("", "b", 0)));
        assertFalse(validate.test(new Person("a", "", 0)));
    }

}
