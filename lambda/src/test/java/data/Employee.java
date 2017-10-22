package data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private final Person person;
    private final List<JobHistoryEntry> jobHistory;

    public Employee(Person person, List<JobHistoryEntry> jobHistory) {
        this.person = person;
        this.jobHistory = jobHistory;
    }

    public Employee withPerson(Person p) {
        return new Employee(p, jobHistory);
    }

    public Employee withJobHistory(List<JobHistoryEntry> h) {
        return new Employee(person, h);
    }

    public Person getPerson() {
        return person;
    }

    public List<JobHistoryEntry> getJobHistory() {
        return new ArrayList<>(jobHistory);
    }

    public List<JobHistoryEntry> addYears(List<JobHistoryEntry> before) {
        List<JobHistoryEntry> newHistory = new ArrayList<>();

        for (JobHistoryEntry entry: before) {
            entry = entry.withDuration(entry.getDuration()+1);
            newHistory.add(entry);
        }

        return newHistory;
    }

    public int workExperience() {
        int workExperience = 0;

        for (JobHistoryEntry value : jobHistory) {
            workExperience += value.getDuration();
        }

        return workExperience;
    }

    public List<JobHistoryEntry> changeQA() {
        List<JobHistoryEntry> changedList = new ArrayList<>(jobHistory.size());

        for (JobHistoryEntry entry : jobHistory) {
            if (entry.getPosition().equals("qa")) {
                entry = entry.withPosition("QA");
            }

            changedList.add(entry);
        }
        return changedList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("person", person)
                .append("jobHistory", jobHistory)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return new EqualsBuilder()
                .append(person, employee.person)
                .append(jobHistory, employee.jobHistory)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(person)
                .append(jobHistory)
                .toHashCode();
    }
}
