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
    
  },
  // 初始化
  onLoad: function () {
    var that = this;
    //that.Login();
  }
})