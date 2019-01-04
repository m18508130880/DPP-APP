var app = getApp();
var template = require('../../utils/customTabBar/customTabBar.js');
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
};
Page({
  data: {
    userInfo: {},
    userName: "",
    password: ""
  },
  // 初始化
  onLoad: function () {
    template.tabbar("tabBar", 2, this)//0表示第一个tabbar
    this.getUserInfo();
  },
  // 获取用户信息
  getUserInfo: function (){
    var userInfo = wx.getStorageSync("userInfo");
    if (!(userInfo != null && userInfo.length > 0)) {
      wx.reLaunch({
        url: '../user/user'
      })
      wx.showToast({
        title: '您必须先登录',
        icon: 'loading',
        duration: 2000
      })
      return;
    }
  },
  // 测量
  gotoMeasure: function (e) {
    wx.navigateTo({
      url: 'measure/measure'
    })
  },
  // 巡查
  gotoMaintain: function (e) {
    wx.navigateTo({
      url: 'maintain/maintain'
    })
  }, 
  // 报修
  gotoRepair: function (e) {
    wx.navigateTo({
      url: 'repair/repair'
    })
  },
  // 维修
  gotoService: function (e) {
    wx.navigateTo({
      url: 'service/service'
    })
  },
  // 安装
  gotoInstall: function (e) {
    wx.navigateTo({
      url: 'install/install'
    })
  },
  // 调试
  gotoDebug: function (e) {
    wx.navigateTo({
      url: 'debug/debug'
    })
  },
})