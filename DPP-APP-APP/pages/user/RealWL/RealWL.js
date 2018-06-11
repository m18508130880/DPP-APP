var app = getApp()
var token = "";
var isOK = false;
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({

  /**
   * 页面的初始数据
   */
  data: {
    select: true,
    project:[],
    markers: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
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
    wx.request({
      url: 'https://www.cjsci-tech.com/dpp-app/RealWL.do',
      //url: 'http://118.31.78.234/dpp-app/RealWL.do',
      data: {
        Cmd: "1",
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
          var WL = 0.0;
          for (var i = 0; i < gjList.length; i++) {
            WL = gjList[i].top_Height*1 - gjList[i].equip_Height*1 + gjList[i].curr_Data*1
            console.log(WL)
            markerArray[i] = {
              id: gjList[i].id,
              latitude: gjList[i].wX_Lat,
              longitude: gjList[i].wX_Lng,
              //iconPath: color
              label:{
                content: WL.toFixed(3),
                x: -20,
                bgColor: "#FFFF00",
                fontSize: 16
              }
            }
          }
          console.log(markerArray)
          that.setData({
            markers: markerArray
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

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  }
})