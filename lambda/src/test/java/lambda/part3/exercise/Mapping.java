package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            List<R> result = new ArrayList<>(this.list.size());
            this.list.forEach(element -> result.add(f.apply(element)));
            return new MapHelper<>(result);
        }

        // ([T], T -> [R]) -> [R]
        public <R> MapHelper<R> flatMap(Function<T, List<R>> f) {
            List<R> result = new ArrayList<>(this.list.size());
            this.list.forEach(element -> result.addAll(f.apply(element)));
            return new MapHelper<>(result);
        }
    }

    List<JobHistoryEntry> addOneYear(List<JobHistoryEntry> jobHistoryEntry) {
        List<JobHistoryEntry> result = new ArrayList<>(jobHistoryEntry.size());
        jobHistoryEntry.forEach(entry -> result.add(entry.withDuration(entry.getDuration() + 1)));
        return result;
    }

    List<JobHistoryEntry> qaToUpperCase(List<JobHistoryEntry> jobHistoryEntry) {
        List<JobHistoryEntry> result = new ArrayList<>(jobHistoryEntry.size());
        jobHistoryEntry.forEach(entry ->
                result.add(entry.getPosition().equals("qa") ? entry.withPosition("QA") : entry));
        return result;
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
                .map(e -> e.withJobHistory(qaToUpperCase(e.getJobHistory()))) // Заменить все qa на QA
                .map(e -> e.withPerson(e.getPerson().withFirstName("John")))

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
        private final Function<T, R> function;

        public LazyMapHelper(List<T> list, Function<T, R> function) {
            this.list = list;
            this.function = function;
        }

        public static <T> LazyMapHelper<T, T> from(List<T> list) {
            return new LazyMapHelper<>(list, Function.identity());
        }

        public List<R> force() {
            List<R> result = new ArrayList<>(list.size());
            list.forEach(element -> result.add(this.function.apply(element)));
            return result;
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f) {
            return new LazyMapHelper<>(this.list, function.andThen(f));
        }
    }

    private static class LazyFlatMapHelper<T, R> {
        private final List<T> list;
        private final Function<T, List<R>> mapper;

        LazyFlatMapHelper(List<T> list, Function<T, List<R>> mapper) {
            this.list = list;
            this.mapper = mapper;
        }

        public static <T> LazyFlatMapHelper<T, T> from(List<T> list) {
            return new LazyFlatMapHelper<>(list, Collections::singletonList);
        }

        public <U> LazyFlatMapHelper<T, U> flatMap(Function<R, List<U>> remapper) {
            return new LazyFlatMapHelper<>(this.list, mapper.andThen(result -> result.stream()
                    .flatMap(element -> remapper.apply(element).stream())
                    .collect(Collectors.toList())));
        }

        public List<R> force() {
            ArrayList<R> result = new ArrayList<>();
            list.stream().map(mapper).forEach(result::addAll);
            return result;
        }
    }

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
                .map(e -> e.withJobHistory(qaToUpperCase(e.getJobHistory()))) // Заменить все qa на QA
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
