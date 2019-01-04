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
    id: '001001',
    deep: '1.2',
    size: '1000',
    inGX: [
      {
        diameter: '300',
        height: '3.52',
        gjId: '001001'
      }
    ],
    outGX: {
      diameter: '300',
      height: '3.52',
      gjId: '001001'
    },
    inPhont: [],
    outPhont: [],
    demo: '测试',

    userInfo: {},
    userName: "",
    password: "",
    multiArray: [['001', '002'], ['001', '002', '003', '004', '005', '006']],
    multiIndex: [0, 0],
    inGXHidden: false,
    outGXHidden: false,
    inHeight: 170,
    qd: '',
    zjd: '_',
    zd: ''
  },
  // 初始化
  onLoad: function () {
    var that = this;

  },
  // 输入深度
  inputDeep: function (e) {
    var index = e.target.id;
    var value = e.detail.value;
    this.setData({
      deep: value
    })
  },
  // 输入井室尺寸
  inputSize: function (e) {
    var index = e.target.id;
    var value = e.detail.value;
    this.setData({
      size: value
    })
  },
  // 更改起终点
  radioCheckedChange: function (e) {
    var val = e.target.id;
    var inHidden = true;
    var outHidden = true;
    var qd = '';
    var zjd = '';
    var zd = '';
    if (val == 'qd') {
      qd = '_';
      inHidden = true;
      outHidden = false;
    } else if (val == 'zjd') {
      zjd = '_';
      inHidden = false;
      outHidden = false;
    } else if (val == 'zd') {
      zd = '_';
      inHidden = false;
      outHidden = true;
    }
    this.setData({
      inGXHidden: inHidden,
      outGXHidden: outHidden,
      qd: qd,
      zjd: zjd,
      zd: zd
    })
  },
  // 增加入口输入框
  addInGX: function (e) {
    var inGX = this.data.inGX;
    inGX.push(
      {
        diameter: '300',
        height: '3.52',
        gjId: '001001'
      }
    );
    var inHeight = this.data.inHeight;
    this.setData({
      inHeight: inHeight + 80,
      inGX: inGX
    })
  },
  // 输入入口管径
  inputInGJ: function (e) {
    var index = e.target.id;
    var value = e.detail.value;
    var inGX = this.data.inGX;
    inGX[index].diameter = value;
    this.setData({
      inGX: inGX
    })
  },
  // 输入入口底部标高
  inputInDG: function (e) {
    var index = e.target.id;
    var value = e.detail.value;
    var inGX = this.data.inGX;
    inGX[index].height = value;
    this.setData({
      inGX: inGX
    })
  },
  // 输入入口管井编号
  inputInRK: function (e) {
    var index = e.target.id;
    var value = e.detail.value;
    var inGX = this.data.inGX;
    inGX[index].gjId = value;
    this.setData({
      inGX: inGX
    })
  },
  // 输入出口管径
  inputOutGJ: function () {
    var index = e.target.id;
    var value = e.detail.value;
    var outGX = this.data.outGX;
    outGX[index].diameter = value;
    this.setData({
      outGX: outGX
    })
  },
  // 输入出口底部标高
  inputOutDG: function () {
    var index = e.target.id;
    var value = e.detail.value;
    var outGX = this.data.outGX;
    outGX[index].height = value;
    this.setData({
      outGX: outGX
    })
  },
  // 导入照片 内外一起
  camera: function (e) {
    var id = e.target.id;
    var that = this;
    var paths = new Array();
    wx.chooseImage({
      count: 3,
      sizeType: ["compressed"],
      success: function (res) {
        paths = res.tempFilePaths;
        if (id == "inPhont") {
          that.setData({
            inPhont: paths
          })
        } else if (id == "outPhont") {
          that.setData({
            outPhont: paths
          })
        }
      },
      fail: function (res) {
        console.log(res);
      },
      complete: function (res) {

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