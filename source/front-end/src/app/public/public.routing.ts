import { Routes, RouterModule } from '@angular/router';
import { PublicComponent } from './public.component';

const routes: Routes = [
  {
    path: '', component: PublicComponent, children: [
      { path: 'sso', loadChildren: () => import('sso/PublicModule').then(x => x.PublicModule) }
    ]
  },
];

export const PublicRoutes = RouterModule.forChild(routes);
