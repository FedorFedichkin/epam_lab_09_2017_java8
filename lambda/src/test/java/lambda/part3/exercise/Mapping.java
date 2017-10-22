package lambda.part3.exercise;

import data.Employee;
import data.JobHistoryEntry;
import data.Person;
import org.junit.Test;
import sun.security.jca.GetInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
            List<R> result = new ArrayList<>(list.size());

            for (T value : list) {
                result.add(f.apply(value));
            }

            return new MapHelper<>(result);
            // TODO
        }

        // ([T], T -> [R]) -> [R]
        public <R> MapHelper<R> flatMap(Function<T, List<R>> f) {
            List<R> result = new ArrayList<>();

            for (T value : list) {
                List<R> rTypeList = f.apply(value);
                result.addAll(rTypeList);
            }
            // TODO
            return new MapHelper<>(result);
        }
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
                .map(e -> e.withJobHistory(e.addYears(e.getJobHistory())))
                .map(e -> e.withJobHistory(e.changeQA())).getList();
        /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qa на QA
                * */
        //.getList();

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
        List<T> list;
        Function<T, R> function;

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

            for (T value: list) {
                result.add(function.apply(value));
            }

            return result;
        }

        public <R2> LazyMapHelper<T, R2> map(Function<R, R2> f) {
            // TODO
            Function<T, R2> tr2Function = T -> {
                return f.apply(function.apply(T));
            };

            return new LazyMapHelper<>(list, tr2Function);
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
                .map(e -> e.withJobHistory(e.addYears(e.getJobHistory())))
                .map(e -> e.withJobHistory(e.changeQA())).force();
                /*
                .map(TODO) // Изменить имя всех сотрудников на John .map(e -> e.withPerson(e.getPerson().withFirstName("John")))
                .map(TODO) // Добавить всем сотрудникам 1 год опыта .map(e -> e.withJobHistory(addOneYear(e.getJobHistory())))
                .map(TODO) // Заменить все qu на QA
                */

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
