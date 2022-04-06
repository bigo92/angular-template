import { Routes, RouterModule } from '@angular/router';
import { Layout2Component } from './layout2.component';

const routes: Routes = [
  { path: '', component: Layout2Component },
];

export const Layout2Routes = RouterModule.forChild(routes);
