var app = getApp()
var token = "";
var iconPath = "../../image/marke.gif";
var dataAll;
Page({
  data: {
    center: [],
    dataAll: [],
    project: [],
    ok: ""
  },
  onLoad: function (str) {
    dataAll = [];
    var that = this;
    token = str.token;
    wx.request({
      url: 'http://118.31.78.234/dpp-app/Manage_Role.do',
      data: {
        Cmd: "0",
        Id: str.id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        if (res.data.rst = "0000") {
          var dataObj = res.data.cData;
          console.log(dataObj)
          for (var i = 0; i < dataObj.length; i++) {
            var project = {
              id: dataObj[i].id,
              cName: dataObj[i].cName,
              lat: dataObj[i].wX_Lat,
              lng: dataObj[i].wX_Lng
            }
            dataAll.push(project);
          }
          console.log(dataAll)
          that.setData({
            dataAll: dataAll
          })
        }
      },
      fail: function (res) {
        console.log("fail")
        console.log(res)
      },
      complete: function () {
      }
    }),
    wx.getLocation({ //获取当前位置
      success: function (res) {
        that.setData({
          center:{
            latitude: res.latitude,
            longitude: res.longitude
          }
        })
      },
    })
  },
  changeMap: function (e) {
    var that = this;
    var id = e.target.id;
    var project = null;
    var center = null;
    var dataAll = that.data.dataAll;
    for(var i = 0; i < dataAll.length; i ++){
      if (id == dataAll[i].id){
        project = dataAll[i];
        center = {
          latitude: dataAll[i].lat,
          longitude: dataAll[i].lng
        }
      }
    }
    if (project != null && center != null){
      that.setData({
        project: project,
        center: center,
        ok: "color_cornflowerblue"
      })
    }
    console.log(that.data.center);
  },
  gotoMap: function (e) {
    var that = this;
    var project = that.data.project;
    if ((that.data.ok).length > 0){
      wx.navigateTo({
        url: '../user/GIS/GIS?token=' + token + '&project_id=' + project.id + '&lat=' + project.lat + '&lng=' + project.lng
      })
    }
  }
})