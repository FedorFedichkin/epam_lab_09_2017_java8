package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
            // TODO
            List<R> result = new ArrayList<>();
            list.forEach(t -> result.add(f.apply(t)));
            return new MapHelper<>(result);
        }

        // ([T], T -> [R]) -> [R]
        public <R> MapHelper<R> flatMap(Function<T, List<R>> f) {
            // TODO
            List<R> result = new ArrayList<>();
            list.forEach(t -> result.addAll(f.apply(t)));
            return new MapHelper<>(result);
        }
    }

    public List<JobHistoryEntry> addOneYear(List<JobHistoryEntry> jhe){
         return new MapHelper<>(jhe).map(e -> e.withDuration(e.getDuration() + 1)).getList();
    }

    public List<JobHistoryEntry> qaToQA(List<JobHistoryEntry> jhe){
        return new MapHelper<>(jhe).map(e -> "qa".equals(e.getPosition()) ? e.withPosition("QA") : e).getList();
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
                /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qa на QA
                * */
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

        assertEquals(mappedEmployees, expectedResult);
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
            // TODO
            List<R> result = new ArrayList<>();
            list.forEach(element -> result.add(function.apply(element)));
            return result;
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f) {
            // TODO
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
                /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qu на QA
                */
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
