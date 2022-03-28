import { Routes, RouterModule } from '@angular/router';
import { CrudBasicComponent } from './crud-basic.component';

const routes: Routes = [
  { path: '', component: CrudBasicComponent },
];

export const CrudBasicRoutes = RouterModule.forChild(routes);
