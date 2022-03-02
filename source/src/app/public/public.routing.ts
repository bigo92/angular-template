import { PublicComponent } from './public.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', component: PublicComponent },
];

export const PublicRoutes = RouterModule.forChild(routes);
