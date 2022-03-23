import { Routes, RouterModule } from '@angular/router';
import { PublicComponent } from './public.component';

const routes: Routes = [
  {
    path: '', component: PublicComponent, children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', loadChildren: () => import('./login/login.module').then(m => m.LoginModule) },
      { path: 'logout', loadChildren: () => import('./logout/logout.module').then(m => m.LogoutModule) }
    ]
  },
];

export const PublicRoutes = RouterModule.forChild(routes);
