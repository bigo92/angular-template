import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: 'dashboard', loadChildren: () => import('dashboard/PublicModule').then(m => m.PublicModule) },
  { path: 'sso', loadChildren: () => import('sso/PublicModule').then(m => m.PublicModule) }
];

export const AppRoutes = RouterModule.forRoot(routes);
