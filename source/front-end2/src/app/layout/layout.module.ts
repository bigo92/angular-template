import { LayoutRoutes } from './layout.routing';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout.component';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { IconsProviderModule } from '../icons-provider.module';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzBadgeModule } from 'ng-zorro-antd/badge';

@NgModule({
  imports: [
    CommonModule,
    IconsProviderModule,
    NzLayoutModule,
    NzGridModule,
    NzMenuModule,
    NzBreadCrumbModule,
    NzAvatarModule,
    NzDropDownModule,
    NzBadgeModule,
    LayoutRoutes
  ],
  declarations: [LayoutComponent]
})
export class LayoutModule { }
