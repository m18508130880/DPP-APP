var app = getApp()
var token = "";
var iconWJ = "/image/map_wj_middle.gif";
var iconYJ = "/image/map_yj_middle.gif";
var gjArray;
var isOK = false;
Page({
  data: {
    select: true,
    project: [],
    markers: [],
    polyline: [],
    gjArray: []
  },
  onLoad: function (str) {
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
    wx.getLocation({
      type: 'gcj02', //返回可以用于wx.openLocation的经纬度
      success: function (res) {
        console.log(res)
        wx.openLocation({
          latitude: res.latitude,
          longitude: res.longitude,
          scale: 20
        })
      }
    })
    //gjArray = [];
    // wx.request({
    //   url: 'http://118.31.78.234/dpp-app/ToPo_GJ.do',
    //   data: {
    //     Cmd: "0",
    //     Token: token,
    //     Project_Id: project_Id
    //   },
    //   method: 'GET',
    //   success: function (res) {
    //     console.log(res.data)
    //     if (res.data.rst = "0000") {
    //       var gjList = res.data.cData;
    //       var markerArray = new Array();
    //       var color = "";
    //       for (var i = 0; i < gjList.length; i++) {
    //         if (gjList[i].id.indexOf("WJ") > -1) {
    //           color = iconWJ;
    //         } else {
    //           color = iconYJ;
    //         }
    //         markerArray[i] = {
    //           id: gjList[i].id,
    //           latitude: gjList[i].wX_Lat,
    //           longitude: gjList[i].wX_Lng,
    //           iconPath: color
    //         }
    //         var gjObj = new Object();
    //         gjObj.tId = gjList[i].id;
    //         gjObj.tLng = gjList[i].wX_Lat;
    //         gjObj.tLat = gjList[i].wX_Lng;
    //         gjArray.push(gjObj);
    //       }
    //       wx.request({
    //         url: 'http://118.31.78.234/dpp-app/ToPo_GX.do',
    //         data: {
    //           Cmd: "0",
    //           Token: token,
    //           Project_Id: project_Id
    //         },
    //         method: 'GET',
    //         success: function (res) {
    //           console.log(res.data)
    //           if (res.data.rst = "0000") {
    //             var gxList = res.data.cData;
    //             var polylineArray = new Array();
    //             var color = ""; var a = "";
    //             var count = 0;
    //             for (var i = 0; i < gxList.length; i++) {
    //               var gjStartId = gxList[i].start_Id;
    //               var gjEndId = gxList[i].end_Id;
    //               var gjStart = that.gjGet(gjStartId);
    //               var gjEnd = that.gjGet(gjEndId);
    //               if (null == gjStart || null == gjEnd) { continue; }
    //               if (gxList[i].id.indexOf("WG") > -1) {
    //                 color = "#FF0000DD";
    //               } else {
    //                 color = "#0000FFDD";
    //               }
    //               if (gxList[i].id.indexOf("WG99") > -1 || gxList[i].id.indexOf("YG99") > -1) { continue; }
    //               polylineArray[count] = {
    //                 points: [{
    //                   latitude: Number(gjStart.tLng),
    //                   longitude: Number(gjStart.tLat)
    //                 }, {
    //                   latitude: Number(gjEnd.tLng),
    //                   longitude: Number(gjEnd.tLat)
    //                 }],
    //                 color: color,
    //                 width: 2,
    //                 id: gxList[i].id
    //               }
    //               count++;
    //             }
    //             console.log(a)
    //             console.log(polylineArray)
    //             that.setData({
    //               polyline: polylineArray
    //             })
    //           }
    //         },
    //         fail: function (res) {
    //           console.log("fail")
    //           console.log(res)
    //         },
    //         complete: function () {
    //         }
    //       })
    //       that.setData({
    //         markers: markerArray
    //       })
    //       console.log(that.data)
    //     }
    //   },
    //   fail: function (res) {
    //     console.log("fail")
    //     console.log(res)
    //   },
    //   complete: function () {
    //   }
    // })
  },
  gjGet: function (objValue) {
    var that = this;
    //console.log(gjArray)
    for (var i = 0; i < gjArray.length; i++) {
      if ((gjArray[i].tId) == objValue) {
        return gjArray[i];
      }
    }
  },
  markertap: function (e) {
    console.log(e)
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
  gjInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GJInfo/GJInfo?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  gxInfo: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../GXInfo/GXInfo?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
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
  }
})