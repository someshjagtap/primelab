package com.mycompany.primelab.service.criteria;

import com.mycompany.primelab.domain.enumeration.Category;
import com.mycompany.primelab.domain.enumeration.Department;
import com.mycompany.primelab.domain.enumeration.Division;
import com.mycompany.primelab.domain.enumeration.Priority;
import com.mycompany.primelab.domain.enumeration.Reason;
import com.mycompany.primelab.domain.enumeration.Type;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.primelab.domain.Project} entity. This class is used
 * in {@link com.mycompany.primelab.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Reason
     */
    public static class ReasonFilter extends Filter<Reason> {

        public ReasonFilter() {}

        public ReasonFilter(ReasonFilter filter) {
            super(filter);
        }

        @Override
        public ReasonFilter copy() {
            return new ReasonFilter(this);
        }
    }

    /**
     * Class for filtering Type
     */
    public static class TypeFilter extends Filter<Type> {

        public TypeFilter() {}

        public TypeFilter(TypeFilter filter) {
            super(filter);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }
    }

    /**
     * Class for filtering Division
     */
    public static class DivisionFilter extends Filter<Division> {

        public DivisionFilter() {}

        public DivisionFilter(DivisionFilter filter) {
            super(filter);
        }

        @Override
        public DivisionFilter copy() {
            return new DivisionFilter(this);
        }
    }

    /**
     * Class for filtering Category
     */
    public static class CategoryFilter extends Filter<Category> {

        public CategoryFilter() {}

        public CategoryFilter(CategoryFilter filter) {
            super(filter);
        }

        @Override
        public CategoryFilter copy() {
            return new CategoryFilter(this);
        }
    }

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    /**
     * Class for filtering Department
     */
    public static class DepartmentFilter extends Filter<Department> {

        public DepartmentFilter() {}

        public DepartmentFilter(DepartmentFilter filter) {
            super(filter);
        }

        @Override
        public DepartmentFilter copy() {
            return new DepartmentFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter projectName;

    private ReasonFilter reason;

    private TypeFilter type;

    private DivisionFilter division;

    private CategoryFilter category;

    private PriorityFilter priority;

    private DepartmentFilter department;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter location;

    private StringFilter status;

    private Boolean distinct;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.projectName = other.optionalProjectName().map(StringFilter::copy).orElse(null);
        this.reason = other.optionalReason().map(ReasonFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeFilter::copy).orElse(null);
        this.division = other.optionalDivision().map(DivisionFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(CategoryFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(PriorityFilter::copy).orElse(null);
        this.department = other.optionalDepartment().map(DepartmentFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProjectName() {
        return projectName;
    }

    public Optional<StringFilter> optionalProjectName() {
        return Optional.ofNullable(projectName);
    }

    public StringFilter projectName() {
        if (projectName == null) {
            setProjectName(new StringFilter());
        }
        return projectName;
    }

    public void setProjectName(StringFilter projectName) {
        this.projectName = projectName;
    }

    public ReasonFilter getReason() {
        return reason;
    }

    public Optional<ReasonFilter> optionalReason() {
        return Optional.ofNullable(reason);
    }

    public ReasonFilter reason() {
        if (reason == null) {
            setReason(new ReasonFilter());
        }
        return reason;
    }

    public void setReason(ReasonFilter reason) {
        this.reason = reason;
    }

    public TypeFilter getType() {
        return type;
    }

    public Optional<TypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeFilter type() {
        if (type == null) {
            setType(new TypeFilter());
        }
        return type;
    }

    public void setType(TypeFilter type) {
        this.type = type;
    }

    public DivisionFilter getDivision() {
        return division;
    }

    public Optional<DivisionFilter> optionalDivision() {
        return Optional.ofNullable(division);
    }

    public DivisionFilter division() {
        if (division == null) {
            setDivision(new DivisionFilter());
        }
        return division;
    }

    public void setDivision(DivisionFilter division) {
        this.division = division;
    }

    public CategoryFilter getCategory() {
        return category;
    }

    public Optional<CategoryFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public CategoryFilter category() {
        if (category == null) {
            setCategory(new CategoryFilter());
        }
        return category;
    }

    public void setCategory(CategoryFilter category) {
        this.category = category;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public Optional<PriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public PriorityFilter priority() {
        if (priority == null) {
            setPriority(new PriorityFilter());
        }
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public DepartmentFilter getDepartment() {
        return department;
    }

    public Optional<DepartmentFilter> optionalDepartment() {
        return Optional.ofNullable(department);
    }

    public DepartmentFilter department() {
        if (department == null) {
            setDepartment(new DepartmentFilter());
        }
        return department;
    }

    public void setDepartment(DepartmentFilter department) {
        this.department = department;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getLocation() {
        return location;
    }

    public Optional<StringFilter> optionalLocation() {
        return Optional.ofNullable(location);
    }

    public StringFilter location() {
        if (location == null) {
            setLocation(new StringFilter());
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(projectName, that.projectName) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(type, that.type) &&
            Objects.equals(division, that.division) &&
            Objects.equals(category, that.category) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(department, that.department) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(location, that.location) &&
            Objects.equals(status, that.status) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            projectName,
            reason,
            type,
            division,
            category,
            priority,
            department,
            startDate,
            endDate,
            location,
            status,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProjectName().map(f -> "projectName=" + f + ", ").orElse("") +
            optionalReason().map(f -> "reason=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDivision().map(f -> "division=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalDepartment().map(f -> "department=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
