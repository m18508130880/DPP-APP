var app = getApp()
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
};
Page({
  data: {
    userInfo: {}
  },
  // 初始化
  onLoad: function () {
    var that = this;
    var userInfo = wx.getStorageSync("userInfo");
    that.setData({
      userInfo: userInfo
    })
  },
  exit: function () {
    wx.setStorageSync("userInfo", null);
    wx.setStorageSync("userName", null);
    wx.setStorageSync("password", null);
    //wx.switchTab({
    wx.reLaunch({
      url: '../../user/user'
    })
  },
  
})