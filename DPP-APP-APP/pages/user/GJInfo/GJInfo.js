// pages/user/data/data.js
var app = getApp()
var token = "";
Page({
  data: {
    gjName: [],
    gjList: [],
    project: [],
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    var that = this;
    var project_Id = str.project_id;
    token = str.token;
    wx.request({
      url: 'http://118.31.78.234/dpp-app/GJ_Info.do',
      data: {
        Cmd: "0",
        Project_Id: project_Id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        if (res.data.rst = "0000") {
          var dataObj = res.data.cData;
          console.log(dataObj)
          var gjNames;
          var gjLists;
          for (var i = 0; i < dataObj.length; i++) {
            var gjN = {
              id: dataObj[i].id
            }
            var gjL = {
              id: dataObj[i].id
            }
            gjNames.push(gjN);
            gjLists.push(gjL);
          }
          console.log(gjNames)
          console.log(gjLists)
          that.setData({
            gjName: gjNames,
            gjList: gjLists
          })
        }
      },
      fail: function (res) {
        console.log("fail")
        console.log(res)
      },
      complete: function () {
      }
    })
  },



  /**
   * 勿删
   * 时间选择器
   */
  BDateChange: function(e) { //时间选择器
    this.setData({
      BDate: e.detail.value
    })
  },
  EDateChange: function(e) { //时间选择器
    this.setData({
      EDate: e.detail.value
    })
  },
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})
