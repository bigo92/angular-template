import { Routes, RouterModule } from '@angular/router';
import { BasicComponent } from './basic.component';

const routes: Routes = [
  { path: '', component: BasicComponent }, //thêm dòng này
];

export const BasicRoutes = RouterModule.forChild(routes);
