import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/welcome' },
  { path: 'welcome', loadChildren: () => import('./pages/welcome/welcome.module').then(m => m.WelcomeModule) },
  { path: 'dashboard', loadChildren: () => import('dashboard/PublicModule').then(m => m.PublicModule) },
  { path: 'sso', loadChildren: () => import('sso/PublicModule').then(x => x.PublicModule) }
];

export const AppRoutes = RouterModule.forRoot(routes);
