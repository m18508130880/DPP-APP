App({
  config:{
    imgPath: "https://www.cjscitech.cn/dpp-app/skin/images/"
  },
  Login: function () {
    var md5 = require("libs/md5.js");
    var that = this;
    var status = "";
    var icon = "";
    var userName = wx.getStorageSync('userName');
    var password = wx.getStorageSync('password');
    if (userName != null && userName.length <= 0) {
      return;
    }
    if (password != null && password.length <= 0) {
      return;
    }
    wx.request({
      url: 'https://www.cjscitech.cn/dpp-app/Login.do',
      //url: 'http://118.31.78.234/dpp-app/Login.do',
      data: {
        Id: userName,
        StrMd5: md5.hex_md5(userName + password),
        newDate: new Date()
      },
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      method: 'POST',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          wx.setStorageSync('userInfo', res.data);
        }
      }
    })
  }
})