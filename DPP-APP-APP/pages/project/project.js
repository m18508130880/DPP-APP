var app = getApp()
var token = "";
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    center: [],
    projects: [],
    project: [],
    ok: ""
  },
  onLoad: function (str) {
    var that = this;
    token = str.token;
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/Manage_Role.do',
      //url: 'http://118.31.78.234/dpp-app/Manage_Role.do',
      data: {
        Cmd: "0",
        Id: str.uId,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var dataObj = res.data.cData;
          var projects = new Array();
          for (var i = 0; i < dataObj.length; i++) {
            var _demo = dataObj[i].demo;
            var project = {
              uId: str.uId,
              id: dataObj[i].id,
              cName: dataObj[i].cName,
              lat: dataObj[i].wX_Lat,
              lng: dataObj[i].wX_Lng,
              demo: _demo.split(",")
            }
            projects.push(project);
          }
          that.setData({
            projects: projects
          })
        }
        else if (res.data.rst == "1005") {
          wx.reLaunch({
            url:'../index/index'
          })
          writeStatus("操作超时", "loading");
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
  gotoMap: function (e) {
    var that = this;
    var project;
    var pId = e.target.id;
    var projects = that.data.projects;
    for (var i = 0; i < projects.length; i ++) {
      if (projects[i].id == pId){
        project = projects[i];
        break;
      }
    }
    wx.navigateTo({
      url: '../user/GIS/GIS?token=' + token + '&uId=' + project.uId + '&project_id=' + project.id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  }
})