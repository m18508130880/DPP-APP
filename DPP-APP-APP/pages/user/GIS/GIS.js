
var app = getApp()
var token = "";
var iconWJ = "/image/map_wj_middle.gif";
var iconYJ = "/image/map_yj_middle.gif";
var gjArray;
var isOK = false;
var d = new Date();
var today = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    select: true,
    project: [],
    markers: [],
    polyline: [],
    graphCut: "none",
    gjArray: [],
    realWaterLev: [],
    alert: [],
    scale: 17,
    center:[]
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
      },
      center:{
        lng: str.lng,
        lat: str.lat
      }
    })
    token = str.token;
    gjArray = [];
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/ToPo_GJ.do',
      //url: 'http://118.31.78.234/dpp-app/ToPo_GJ.do',
      data: {
        Cmd: "0",
        Token: token,
        Project_Id: project_Id
      },
      method: 'GET',
      success: function (res) {
        console.log(res.data)
        if (res.data.rst == "0000") {
          var gjList = res.data.cData;
          var markerArray = new Array();
          var color = "";
          for (var i = 0; i < gjList.length; i++) {
            if (gjList[i].id.indexOf("WJ") > -1) {
              color = iconWJ;
            } else {
              color = iconYJ;
            }
            markerArray.push( {
              id: gjList[i].id,
              latitude: gjList[i].wX_Lat,
              longitude: gjList[i].wX_Lng,
              iconPath: color
            })
            var gjObj = new Object();
            gjObj.tId = gjList[i].id;
            gjObj.tLng = gjList[i].wX_Lat;
            gjObj.tLat = gjList[i].wX_Lng;
            gjArray.push(gjObj);
            if(gjList[i].equip_Id.length == 20) {
              var WL = gjList[i].top_Height * 1 - gjList[i].equip_Height * 1 + gjList[i].curr_Data * 1
              markerArray.push({
                id: gjList[i].id,
                latitude: gjList[i].wX_Lat,
                longitude: gjList[i].wX_Lng,
                label: {
                  content: WL.toFixed(3),
                  x: -20,
                  bgColor: "#FFFF00",
                  fontSize: 16
                }
              })
            }
          }
          wx.request({
            url: 'https://www.cjsci-tech.com/dpp-app/ToPo_GX.do',
            //url: 'http://118.31.78.234/dpp-app/ToPo_GX.do',
            data: {
              Cmd: "0",
              Token: token,
              Project_Id: project_Id
            },
            method: 'GET',
            success: function (res) {
              console.log(res.data)
              if (res.data.rst == "0000") {
                var gxList = res.data.cData;
                var polylineArray = new Array();
                var color = "";var a = "";
                var count = 0;
                for (var i = 0; i < gxList.length; i++) {
                  var gjStartId = gxList[i].start_Id;
                  var gjEndId = gxList[i].end_Id;
                  var gjStart = that.gjGet(gjStartId);
                  var gjEnd = that.gjGet(gjEndId);
                  if (null == gjStart || null == gjEnd) { continue; }
                  if (gxList[i].id.indexOf("WG") > -1) {
                    color = "#FF0000DD";
                  } else {
                    color = "#0000FFDD";
                  }
                  if (gxList[i].id.indexOf("WG99") > -1 || gxList[i].id.indexOf("YG99") > -1) { continue; }
                  polylineArray[count] = {
                    points: [{
                      latitude: Number(gjStart.tLng),
                      longitude: Number(gjStart.tLat)
                    }, {
                      latitude: Number(gjEnd.tLng),
                      longitude: Number(gjEnd.tLat)
                    }],
                    color: color,
                    width: 2,
                    id: gxList[i].id
                  }
                  count ++;
                }
                console.log(a)
                console.log(polylineArray)
                that.setData({
                  polyline: polylineArray
                })
              }
              else if (res.data.rst == "1005") {
                wx.reLaunch({
                  url: '../../index/index'
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
          that.setData({
            markers: markerArray
          })
          console.log(that.data)
        }
        else if (res.data.rst == "1005") {
          wx.reLaunch({
            url: '../../index/index'
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
    }),
    //获取告警信息
      wx.request({
        url: 'https://www.cjsci-tech.com/dpp-app/Alert_Info.do',
        //url: 'http://118.31.78.234/dpp-app/Alert_Info.do',
        data: {
          Cmd: "0",
          Token: token,
          Project_Id: project_Id
        },
        method: 'GET',
        success: function (res) {
          console.log(res.data);
          if (res.data.rst == "0000") {
            var dataObj = res.data.cData;
            var alerts = new Array();
            for (var i = 0; i < dataObj.length; i++) {
              alerts[i] = {
                attr_Name: dataObj[i].attr_Name,
                cTime: dataObj[i].cTime,
                curr_Data: dataObj[i].curr_Data,
                des: dataObj[i].des,
                gJ_Id: dataObj[i].gJ_Id,
              }
            }
            console.log(alerts)
            that.setData({
              alert: alerts
            })
          }
          else if (res.data.rst == "1005") {
            wx.reLaunch({
              url: '../../index/index'
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
  gjGet: function (objValue) {
    var that = this;
    //console.log(gjArray)
    for (var i = 0; i < gjArray.length; i ++) {
      if ((gjArray[i].tId) == objValue) {
        return gjArray[i];
      }
    }
  },
  //点击缩放按钮 
  zoom: function (e) {
    var that = this;
    var scale = that.data.scale;
    if (e.target.id === "small") {
      --scale;
    } else {
      ++scale;
    }
    if (scale > 20 || scale < 6){return}
    that.setData({
      scale: scale
    })
  }, 
  // 定位自己位置
  positioning(e) {
    var that = this;
    wx.getLocation({
      type: 'gcj02', //返回可以用于wx.openLocation的经纬度  
      success: function (res) {
        var latitude = res.latitude
        var longitude = res.longitude
        that.setData({
          center: {
            lng: longitude,//经度   
            lat: latitude,//纬度   
          }
        })
      }
    })
  },
  // 回到项目中心
  project: function () {
    var that = this;
    that.setData({
      center: {
        lng: that.data.project.lng,//经度   
        lat: that.data.project.lat,//纬度   
      }
    })
  },
  alert: function () {
    var that = this;
    var project = that.data.project;
    wx.navigateTo({
      url: '../Alert/Alert?token=' + token + '&uId=' + project.uId + '&project_id=' + project.project_id + '&lat=' + project.lat + '&lng=' + project.lng
    })
  },
  /**
   * 勿删
   * 时间选择器
   */
  BDateChange: function (e) { //时间选择器
    this.setData({
      BDate: e.detail.value
    })
  },
  EDateChange: function (e) { //时间选择器
    this.setData({
      EDate: e.detail.value
    })
  },
  onReady: function () {
    // 页面渲染完成
    // 获取组件
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  }
})