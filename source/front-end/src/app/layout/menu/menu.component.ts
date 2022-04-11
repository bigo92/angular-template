import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Location } from '@angular/common';
import { filter } from 'rxjs/operators';

declare let $: any;
@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
  encapsulation: ViewEncapsulation.None //Mo khoa scss cho phep scss se anh huong tat ca component
})
export class MenuComponent implements OnInit {

  public menuData: any[] = [];
  public isShowFullMenuMobile = false;
  public isLoading = true;

  constructor(private rt: Router,
    private location: Location) { }

  async ngOnInit(): Promise<void> {
    const url = this.location.path();
    // gọi lần đầu
    this.getDataMenu(url);

    // goi khi thay doi url
    this.rt.events.pipe(filter(x => x instanceof NavigationEnd))
      .subscribe((x: any) => {
        // gọi khi thay đổi url
        this.getDataMenu(x.url);
      });

    this.resizeDocument();
  }

  // async getData(): Promise<void> {
  //   this.isLoading = true;
  //   const rs = await this.menuApi.getAllByUser(new IdentityMenuGetAllByUserQuery()).toPromise();
  //   if (rs.success) {
  //     let dataRaw = rs.result.map(x => {
  //       return {
  //         id: x.id,
  //         icon: x.icon,
  //         name: x.name,
  //         url: x.link,
  //         parentId: x.parentId,
  //         params: x.queryParams ? JSON.parse(x.queryParams) : null,
  //         isOpen: false,
  //         exact: true,
  //         child: [],
  //       };
  //     });

  //     // convert data to tree
  //     dataRaw.forEach(item => {
  //       item.child = dataRaw.filter(x => x.parentId === item.id);
  //     });

  //     const convertData = dataRaw.filter(x => x.parentId === null);
  //     this.menuData = convertData;
  //   } else {
  //     this.messageService.notiMessageError(rs.error);
  //   }
  //   this.isLoading = false;
  // }

  async getDataMenu(url: string): Promise<void> {
    if (this.isLoading) {
      await this.getMenuDefault();
      //await this.getData();
    }

    // this.clearActiveMenu(this.menuData);
    // this.autoOpenMenuByUrl(this.menuData, url);
  }

  async getMenuDefault(): Promise<void> {
    this.menuData = [
      {
        icon: 'icon-home4',
        name: 'Trang chủ',
        url: '/',
        isOpen: false,
        exact: true,
        child: [],
        level: 0
      },
      {
        icon: 'icon-chip',
        name: 'Quản trị ứng dụng',
        url: null,
        isOpen: false,
        exact: true,
        level: 0,
        child: [
          {
            icon: 'icon-cog4',
            name: 'Quản trị hệ thống',
            url: null,
            isOpen: false,
            exact: true,
            level: 1,
            child: [
              {
                icon: 'icon-list-unordered',
                name: 'Quản trị menu',
                url: '/qtud/menu',
                isOpen: false,
                exact: true,
                level: 2,
                child: []
              },
              {
                icon: 'icon-user-check',
                name: 'Quản trị người dùng',
                url: null,
                isOpen: false,
                exact: true,
                level: 2,
                child: [
                  {
                    icon: 'icon-collaboration',
                    name: 'Quản trị nhóm quyền',
                    url: '/qtud/quan-tri-nguoi-dung/nhom-quyen',
                    isOpen: false,
                    exact: true,
                    level: 3,
                    child: []
                  },
                  {
                    icon: 'icon-vcard',
                    name: 'Quản lý tài khoản',
                    url: '/qtud/quan-tri-nguoi-dung/tai-khoan',
                    isOpen: false,
                    exact: true,
                    level: 3,
                    child: []
                  },
                ]
              }
            ],
          }

        ],
      },
      {
        icon: 'icon-stack2',
        name: 'Danh mục',
        url: null,
        isOpen: false,
        exact: false,
        level: 1,
        child: [
          {
            icon: 'icon-map5',
            name: 'Điểm phát',
            url: '/danhmuc/diem-phat',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
          {
            icon: 'icon-price-tags',
            name: 'Sản phẩm dịch vụ',
            url: '/danhmuc/san-pham-dich-vu',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
          {
            icon: 'icon-add',
            name: 'Cộng thêm',
            url: '/danhmuc/cong-them',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
          {
            icon: 'icon-collaboration',
            name: 'Ca',
            url: '/danhmuc/ca',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
        ]
      },
      {
        icon: 'icon-cog',
        name: 'Cấu hình',
        url: null,
        isOpen: false,
        exact: false,
        level: 1,
        child: [
          {
            icon: 'icon-file-spreadsheet',
            name: 'Báo cáo',
            url: '/cauhinh/baocao',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
          {
            icon: 'icon-pie-chart5',
            name: 'Thiết lập biểu đồ',
            url: '/cauhinh/bieudo',
            isOpen: false,
            exact: false,
            level: 2,
            child: [],
          },
        ],
      },
    ];

    this.isLoading = false;
  }

  clearActiveMenu(source: any[]) {
    for (const item of source) {
      item.isOpen = false;
      if (item.child.length > 0) {
        this.clearActiveMenu(item.child);
      }
    }
  }

  private autoOpenMenuByUrl(source: any[], url: string, level: number = 0): boolean {
    let result = false;
    for (const item of source) {
      item.level = level;
      let queryParam = "";
      if (item.params) {
        for (const key in item.params) {
          const value = item.params[key];
          if (queryParam.indexOf('?') === -1) {
            queryParam += "?";
          } else {
            queryParam += "&";
          }
          queryParam += `${key}=${value}`;
        }
      }

      if (item.child.length > 0) {
        const rs = this.autoOpenMenuByUrl(item.child, url, level + 1);
        if (rs) {
          item.isOpen = true;
          result = rs;
        }
      } else if (`${item.url}${queryParam}` === url) {
        result = true;
      }
    }
    return result;
  }

  public openMenu(value: any): void {
    value.isOpen = !value.isOpen;
  }

  public resizeDocument(): void {
    if (window.screen.width <= 1366) {
      $('body').toggleClass('sidebar-xs').removeClass('sidebar-mobile-main');
    }
  }

  public hideMenuMobile() {
    $('body').removeClass('sidebar-mobile-main');
    this.isShowFullMenuMobile = false;
  }

  public showFullMenuMobile() {
    this.isShowFullMenuMobile = !this.isShowFullMenuMobile;
  }

}
