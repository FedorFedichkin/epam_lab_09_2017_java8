package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"WeakerAccess"})
public class Mapping {

    private static class MapHelper<T> {

        private final List<T> list;

        public MapHelper(List<T> list) {
            this.list = list;
        }

        public List<T> getList() {
            return list;
        }

        // ([T], T -> R) -> [R]
        public <R> MapHelper<R> map(Function<T, R> f) {
            final List<R> res = new ArrayList<>(this.list.size());
            for (T t: list) {
                res.add(f.apply(t));
            }
            return new MapHelper<>(res);
        }

        // ([T], T -> [R]) -> [R]
        public <R> MapHelper<R> flatMap(Function<T, List<R>> f) {
            final List<R> res = new ArrayList<>(this.list.size());
            for (T t: list) {
                res.addAll(f.apply(t));
            }
            return new MapHelper<>(res);
        }
    }

    public static List<JobHistoryEntry> addOneYear(List<JobHistoryEntry> a) {
        for (int i = 0; i < a.size(); i++) {
            a.set(i,new JobHistoryEntry(a.get(i).getDuration() + 1, a.get(i).getPosition(), a.get(i).getEmployer()));
        }
        return a;
    }

    public static List<JobHistoryEntry> qaToQA (List<JobHistoryEntry> a) {
        for (JobHistoryEntry data: a) {
            if (Objects.equals(data.getPosition(),"qa")) {
               a.set(a.indexOf(data),data.withPosition("QA"));
            }
        }
        return a;
    }

    @Test
    public void mapping() {
        List<Employee> employees = Arrays.asList(
            new Employee(new Person("a", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(2, "dev", "epam"),
                        new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(new Person("b", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(3, "qa", "yandex"),
                        new JobHistoryEntry(1, "qa", "epam"),
                        new JobHistoryEntry(1, "dev", "abc")
                )),
            new Employee(new Person("c", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(5, "qa", "epam")
                ))
        );

        List<Employee> mappedEmployees = new MapHelper<>(employees)
                .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(e -> e.withJobHistory(qaToQA(e.getJobHistory())))
                .getList();

        List<Employee> expectedResult = Arrays.asList(
            new Employee(new Person("John", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(3, "dev", "epam"),
                        new JobHistoryEntry(2, "dev", "google")
                )),
            new Employee(new Person("John", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(4, "QA", "yandex"),
                        new JobHistoryEntry(2, "QA", "epam"),
                        new JobHistoryEntry(2, "dev", "abc")
                )),
            new Employee(new Person("John", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(6, "QA", "epam")
                ))
        );

        assertEquals(expectedResult, mappedEmployees);
    }

    private static class LazyMapHelper<T, R> {

        private final List<T> list;
        private final Function<T,R> function;

        public LazyMapHelper(List<T> list, Function<T, R> function) {
            this.list = list;
            this.function = function;
        }

        public static <T> LazyMapHelper<T, T> from(List<T> list) {
            return new LazyMapHelper<>(list, Function.identity());
        }

        public List<R> force() {
            final List<R> res = new ArrayList<>(this.list.size());
            for (T t: list) {
                res.add(function.apply(t));
            }
            return res;
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f) {
            return new LazyMapHelper<>(list, function.andThen(f));
        }
    }

    // TODO * LazyFlatMapHelper

    @Test
    public void lazyMapping() {
        List<Employee> employees = Arrays.asList(
            new Employee(
                new Person("a", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(2, "dev", "epam"),
                        new JobHistoryEntry(1, "dev", "google")
                )),
            new Employee(
                new Person("b", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(3, "qa", "yandex"),
                        new JobHistoryEntry(1, "qa", "epam"),
                        new JobHistoryEntry(1, "dev", "abc")
                )),
            new Employee(
                new Person("c", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(5, "qa", "epam")
                ))
        );

        List<Employee> mappedEmployees = LazyMapHelper.from(employees)
                .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(e -> e.withJobHistory(qaToQA(e.getJobHistory())))
                .force();

        List<Employee> expectedResult = Arrays.asList(
            new Employee(new Person("John", "Galt", 30),
                Arrays.asList(
                        new JobHistoryEntry(3, "dev", "epam"),
                        new JobHistoryEntry(2, "dev", "google")
                )),
            new Employee(new Person("John", "Doe", 40),
                Arrays.asList(
                        new JobHistoryEntry(4, "QA", "yandex"),
                        new JobHistoryEntry(2, "QA", "epam"),
                        new JobHistoryEntry(2, "dev", "abc")
                )),
            new Employee(new Person("John", "White", 50),
                Collections.singletonList(
                        new JobHistoryEntry(6, "QA", "epam")
                ))
        );

        assertEquals(mappedEmployees, expectedResult);
    }
}
