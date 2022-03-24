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
    // new ModuleFederationPlugin({
    //   remotes: {
    //     'mailbox': "mailbox@http://localhost:4201/remoteEntry.js",
    //     // 'calendar': "calendar@http://localhost:5400/remoteEntry.js",
    //   },
    //   shared: ["@angular/core", "@angular/common", "@angular/router"]
    // })
  ],
};
