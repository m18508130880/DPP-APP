
var app = getApp()
var token = "";
var isOK = false;
var project_Id = "";
function writeStatus(status, icon) {
  // 弹出提示框
  wx.showToast({
    title: status,
    icon: icon
  })
}
Page({
  data: {
    gjName: [],
    gjIndex: 0,
    gjSum: 0,
    gjAll: [],
    gjList: [],
    gjIdList: [],
    startId: "",
    endtId: "",
    project: [],
    select: true, 
    graphCut: "none",
    graphWidth: 0, 
    ec: {
      // 将 lazyLoad 设为 true 后，需要手动初始化图表
      lazyLoad: true
    },
    BDate: '2017-05-05',
    EDate: '2017-05-05'
  },
  onLoad: function (str) {  //初始化
    var that = this;
    project_Id = str.project_id;
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
      url: 'https://www.cjsci-tech.com/dpp-app/GJ_Info.do',
      //url: 'http://118.31.78.234/dpp-app/GJ_Info.do',
      data: {
        Cmd: "0",
        Project_Id: project_Id,
        Token: token
      },
      method: 'GET',
      success: function (res) {
        if (res.data.rst == "0000") {
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
        else if (res.data.rst = "1005") {
          wx.reLaunch({
            url: "../../index/index"
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
    // 获取组件
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

