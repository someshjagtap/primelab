package com.mycompany.primelab.service.mapper;

import com.mycompany.primelab.domain.Project;
import com.mycompany.primelab.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
