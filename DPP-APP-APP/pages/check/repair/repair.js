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
    items:[
      {
        name: "雨水井",
        value: "1",
        checked: false
      },{
        name: "污水井",
        value: "2",
        checked: false
      }, {
        name: "排出口",
        value: "3",
        checked: false
      },{
        name: "水质仪",
        value: "4",
        checked: false
      }, {
        name: "液位计",
        value: "5",
        checked: false
      }, {
        name: "控制箱",
        value: "6",
        checked: false
      }
    ],
    que: [
      {
        name: "电压不足",
        value: "1",
        checked: false
      }, {
        name: "接线断开",
        value: "2",
        checked: false
      }, {
        name: "没有充电",
        value: "3",
        checked: false
      }, {
        name: "控制损坏",
        value: "4",
        checked: false
      }, {
        name: "设备故障",
        value: "5",
        checked: false
      }, {
        name: "DTU故障",
        value: "6",
        checked: false
      }
    ],
    photo:[],
    photoDemo: false
  },
  // 初始化
  onLoad: function () {
    var that = this;

  },
  // 定位
  position: function () {
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
      id: '',
      deep: '',
      size: '',
      inGX: [
        {
          diameter: '',
          height: '',
          gjId: ''
        }
      ],
      outGX: {
        diameter: '',
        height: '',
        gjId: ''
      },
      inPhont: [],
      outPhont: [],
      demo: '',
      radioCheckVal: 1,
      inGXHidden: false,
      outGXHidden: false,
      inHeight: 170
    })
  }
})