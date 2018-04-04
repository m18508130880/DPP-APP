// pages/user/data/data.js
var app = getApp()
var token = "";
var isOK = false;
Page({
  data: {
    gjName: [],
    gjIndex: 0,
    gjSum: 0,
    gjAll: [],
    gjList: [],
    project: [],
    select: true,
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    var that = this;
    var project_Id = str.project_id;
    that.setData({
      project: {
        uId: str.uId,
        project_id: project_Id,
        lng: str.lng,
        lat: str.lat
      }
    })
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
          //console.log(dataObj)
          var gjNames = new Array();
          var gjLists = new Array();
          var names = "";
          gjNames[0] = "全部系统";
          var m = 1;
          for (var i = 0; i < dataObj.length; i++) {
            var name = dataObj[i].id.substr(0, 5);
            if (names.indexOf(name) < 0 && (name.substr(0, 3) == "YJ0" || name.substr(0, 3) == "WJ0")) {
              names += name;
              gjNames[m] = name;
              m ++;
            }
            gjLists[i] = {
              id: dataObj[i].id,
              sub_Id: dataObj[i].id.substr(5),
              road: dataObj[i].road,
              top_Height: dataObj[i].top_Height,
              base_Height: dataObj[i].base_Height,
              size: dataObj[i].size,
              material: dataObj[i].material
            }
          }
          console.log(gjLists)
          that.setData({
            gjName: gjNames,
            gjAll: gjLists,
            gjList: gjLists,
            gjSum: dataObj.length
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
  changeF: function (e) {
    var that = this;
    console.log(e)
    if (isOK) {
      that.setData({
        select: isOK
      })
      isOK = !isOK;
    } else {
      that.setData({
        select: isOK
      })
      isOK = !isOK;
    }
  },
  gis: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gxInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  alert: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gjPos: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  changeP: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../../project/project?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },

  bindCasPickerChange: function (e) {
    var that = this;
    var gjN = that.data.gjName[e.detail.value];
    var gjA = that.data.gjAll;
    var count = 0;
    var gjL = [];
    for (var i = 0; i < gjA.length; i ++) {
      if (gjA[i].id.indexOf(gjN) > -1) {
        gjL[count] = gjA[i];
        count ++;
      }
    }
    this.setData({
      gjIndex: e.detail.value,
      gjList: gjL,
      gjSum: count
    })
    if (count == 0) {
      this.setData({
        gjIndex: e.detail.value,
        gjList: gjA,
        gjSum: gjA.length
      })
    }
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
