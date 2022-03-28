import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';

const routes: Routes = [
  {
    path: '', component: LayoutComponent, children: [
      { path: '', pathMatch: 'full', redirectTo: '/dashboard/home' },
      { path: 'dashboard', loadChildren: () => import('dashboard/PublicModule').then(m => m.PublicModule) },
      { path: 'example', loadChildren: () => import('example/PublicModule').then(m => m.PublicModule) }
    ]
  },
];

export const LayoutRoutes = RouterModule.forChild(routes);
