import { Routes, RouterModule } from '@angular/router';
import { PublicComponent } from './public.component';

const routes: Routes = [
  {
    path: '', component: PublicComponent, children: [
      { path: 'crud-basic', loadChildren: () => import('./crud-basic/crud-basic.module').then(x => x.CrudBasicModule) }
    ]
  },
];

export const PublicRoutes = RouterModule.forChild(routes);
