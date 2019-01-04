//初始化数据
function tabbarinit() {
 return [
      {
        "current": 0,
        "pagePath": "/pages/index/index",
        "text": "首页",
        "iconPath": "/image/home.png",
        "selectedIconPath": "/image/home_.png"
      },
      {
        "current": 0,
        "pagePath": "/pages/GIS/GIS",
        "text": "GIS地图",
        "iconPath": "/image/gis.png",
        "selectedIconPath": "/image/gis_.png"
      },
      {
        "current": 0,
        "pagePath": "/pages/check/check",
        "text": "运维",
        "iconPath": "/image/check.png",
        "selectedIconPath": "/image/check_.png"
      },
      {
        "current": 0,
        "pagePath": "/pages/user/user",
        "text": "我的",
        "iconPath": "/image/my.png",
        "selectedIconPath": "/image/my_.png"
      }
    ]

}
//tabbar 主入口
function tabbarmain(bindName = "tabdata", id, target) {
  var that = target;
  var bindData = {};
  var otabbar = tabbarinit();
  otabbar[id]['iconPath'] = otabbar[id]['selectedIconPath']//换当前的icon
  otabbar[id]['current'] = 1;
  bindData[bindName] = otabbar
  that.setData({ bindData });
}

module.exports = {
  tabbar: tabbarmain
}