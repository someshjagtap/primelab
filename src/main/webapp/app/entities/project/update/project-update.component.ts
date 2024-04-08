import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Reason } from 'app/entities/enumerations/reason.model';
import { Type } from 'app/entities/enumerations/type.model';
import { Division } from 'app/entities/enumerations/division.model';
import { Category } from 'app/entities/enumerations/category.model';
import { Priority } from 'app/entities/enumerations/priority.model';
import { Department } from 'app/entities/enumerations/department.model';
import { ProjectService } from '../service/project.service';
import { IProject } from '../project.model';
import { ProjectFormService, ProjectFormGroup } from './project-form.service';

@Component({
  standalone: true,
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;
  project: IProject | null = null;
  reasonValues = Object.keys(Reason);
  typeValues = Object.keys(Type);
  divisionValues = Object.keys(Division);
  categoryValues = Object.keys(Category);
  priorityValues = Object.keys(Priority);
  departmentValues = Object.keys(Department);

  protected projectService = inject(ProjectService);
  protected projectFormService = inject(ProjectFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProjectFormGroup = this.projectFormService.createProjectFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
      if (project) {
        this.updateForm(project);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.projectFormService.getProject(this.editForm);
    if (project.id !== null) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(project: IProject): void {
    this.project = project;
    this.projectFormService.resetForm(this.editForm, project);
  }
}
