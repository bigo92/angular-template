import { LogoutComponent } from './logout.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path:'', component: LogoutComponent },
];

export const LogoutRoutes = RouterModule.forChild(routes);
