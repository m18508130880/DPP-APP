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
    vol:"",
    radio:[{
      name:"正常",
      value: "0",
      checked: true
    },{
        name: "异常",
        value: "1",
        checked: false
    }],
    dtuStatus:{
      height: 75,
      que: true,
      code: "",
      text: "",
    },
    equipStatus: {
      height: 75,
      que: true,
      code: "",
      text: "",
    },
    powerStatus: {
      height: 75,
      que: true,
      code: "",
      text: "",
    },
    signalStatus: {
      height: 75,
      que: true,
      code: "",
      text: "",
    },
    photo: [],
    photoDemo: false,
  },
  // 初始化
  onLoad: function () {
    var that = this;

  },
  // 定位
  position: function (){
    wx.getLocation({
      type: 'wgs84',
      success(res) {
        console.log(res)
        const latitude = res.latitude
        const longitude = res.longitude
        const speed = res.speed
        const accuracy = res.accuracy
      }
    })
  },
  // 工作电压
  inputVol: function (e) {
    var value = e.detail.value;
    this.setData({
      vol: value
    })
  },
  // DTU状态
  dtuStatus: function (e) {
    console.log(e)
    var index = e.detail.value;
    var que = true;
    var height = 75;
    var code = "0"
    if(index == "1"){
      que = false;
      height = 220;
      code = "1";
    }
    this.setData({
      dtuStatus:{
        height: height,
        que: que,
        code: code
      }
    })
  },
  // DTU问题说明
  inputDtuText: function () {
    var value = e.detail.value;
    var dtuStatus = this.data.dtuStatus;
    dtuStatus.text = value;
    this.setData({
      dtuStatus: dtuStatus
    })
  },
  // 设备状态
  equipStatus: function (e) {
    //console.log(e)
    var index = e.detail.value;
    var que = true;
    var height = 75;
    var code = "0"
    if (index == "1") {
      que = false;
      height = 220;
      code = "1";
    }
    this.setData({
      equipStatus: {
        height: height,
        que: que,
        code: code
      }
    })
  },
  // 设备问题说明
  inputEquipText: function () {
    var value = e.detail.value;
    var equipStatus = this.data.equipStatus;
    equipStatus.text = value;
    this.setData({
      equipStatus: equipStatus
    })
  },
  // 电源状态
  powerStatus: function (e) {
    //console.log(e)
    var index = e.detail.value;
    var que = true;
    var height = 75;
    var code = "0"
    if (index == "1") {
      que = false;
      height = 220;
      code = "1";
    }
    this.setData({
      powerStatus: {
        height: height,
        que: que,
        code: code
      }
    })
  },
  // 电源问题说明
  inputPowerText: function () {
    var value = e.detail.value;
    var powerStatus = this.data.powerStatus;
    powerStatus.text = value;
    this.setData({
      powerStatus: powerStatus
    })
  },
  // 信号状态
  signalStatus: function (e) {
    //console.log(e)
    var index = e.detail.value;
    var que = true;
    var height = 75;
    var code = "0"
    if (index == "1") {
      que = false;
      height = 220;
      code = "1";
    }
    this.setData({
      signalStatus: {
        height: height,
        que: que,
        code: code
      }
    })
  },
  // 信号问题说明
  inputSignalText: function () {
    var value = e.detail.value;
    var signalStatus = this.data.signalStatus;
    signalStatus.text = value;
    this.setData({
      signalStatus: signalStatus
    })
  },
  // 导入照片
  camera: function (e) {
    var id = e.target.id;
    var that = this;
    var paths = new Array();
    wx.chooseImage({
      count: 3,
      sizeType: ["compressed"],
      success: function (res) {
        paths = res.tempFilePaths;
        that.setData({
          photo: paths
        })
      },
      fail: function (res) {
        console.log(res);
      },
      complete: function (res) {
        var photo = that.data.photo;
        var photoDemo = false;
        console.log(photo)
        if (photo != null && photo.length > 0) {
          photoDemo = true;
        }
        that.setData({
          photoDemo: photoDemo
        })
      }
    })
    
  },
  // 提交
  commit: function () {
    var that = this;
    console.log(that.data)
  },
  // 重置
  doRest: function () {
    this.setData({
      
    })
  }
})