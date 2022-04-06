const { ModuleFederationPlugin } = require('webpack').container;

module.exports = {
  output: {
    publicPath: "http://localhost:4200/",
    uniqueName: "shell"
  },
  optimization: {
    // Only needed to bypass a temporary bug
    runtimeChunk: false
  },
  plugins: [
    new ModuleFederationPlugin({
      remotes: {
        'dashboard': "dashboard@http://localhost:4201/remoteEntry.js",
        'sso': "sso@http://localhost:4202/remoteEntry.js",
        'example': "example@http://localhost:4203/remoteEntry.js",
      },
      shared: {
        "@angular/core": { singleton: true, strictVersion: true },
        "@angular/common": { singleton: true, strictVersion: true },
        "@angular/router": { singleton: true, strictVersion: true },
        "ng-zorro-antd/modal": { singleton: true, strictVersion: true },
        "ng-zorro-antd/message": { singleton: true, strictVersion: true },
        "ng-zorro-antd/notification": { singleton: true, strictVersion: true },
        "ng-zorro-antd/icon": { singleton: true, strictVersion: true },
        "@ant-design/icons-angular": { singleton: true, strictVersion: true },
      }
    })
  ],
};
