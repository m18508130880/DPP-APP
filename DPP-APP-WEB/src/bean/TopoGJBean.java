package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.TopoGJJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class TopoGJBean extends RmiBean {
	public final static long serialVersionUID = RmiBean.RMI_TOPOGJ;

	public long getClassId() {
		return serialVersionUID;
	}

	public TopoGJBean() {
		super.className = "TopoGJBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, Date> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			TopoGJJsonBean json = new TopoGJJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsKey(Token)) {
				TokenList.put(Token, new Date());
				List<Object> CData = new ArrayList<Object>();
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
				case 1:
					// 获取管井信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> topoGJList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(topoGJList, CData);
						json.setCData(CData);
						json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					}
					break;
				}
			} else {
				// 鉴权失败
				json.setRst(CommUtil.IntToStringLeftFillZero(
						MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
			}

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
			response.setCharacterEncoding("UTF-8");
			output = response.getWriter();
			output.write(jsonObj.toString());
			output.flush();

			//System.out.println("AppGisJson:" + jsonObj.toString() + ";");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
	}
	public List<Object> objToJson(ArrayList<?> topoGJList, List<Object> CData){
		Iterator<?> topoGJIterator = topoGJList.iterator();
		while (topoGJIterator.hasNext()) {
			TopoGJBean RealJson = (TopoGJBean) topoGJIterator
					.next();
			TopoGJJsonBean topoGJJson = new TopoGJJsonBean();
			topoGJJson.setId(RealJson.getId());
			topoGJJson.setLongitude(RealJson.getLongitude());
			topoGJJson.setLatitude(RealJson.getLatitude());
			topoGJJson.setWX_Lng(RealJson.getWX_Lng());
			topoGJJson.setWX_Lat(RealJson.getWX_Lat());
			topoGJJson.setFlag(RealJson.getFlag());
			topoGJJson.setSign(RealJson.getSign());
			topoGJJson.setBase_Height(RealJson.getBase_Height());
			topoGJJson.setTop_Height(RealJson.getTop_Height());
			topoGJJson.setProject_Id(RealJson.getProject_Id());
			topoGJJson.setEquip_Id(RealJson.getEquip_Id());
			topoGJJson.setEquip_Height(RealJson.getEquip_Height());
			topoGJJson.setCTime(RealJson.getCTime());
			topoGJJson.setCurr_Data(RealJson.getCurr_Data());
			
			CData.add(topoGJJson);
		}
		return CData;
		
	}
	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd) {
		String Sql = "";
		switch (pCmd) {
		case 0:// 查询view_topo_gj，用于地图显示
			Sql = " select t.id, t.longitude, t.latitude, t.wx_lng, t.wx_lat, t.flag, t.sign, t.project_id, t.equip_id, t.top_height, t.equip_height, t.curr_data, t.ctime, t.road, t.rotation, t.attr_id, t.des"
					+ " from view_topo_gj t "
					+ " where instr('" + Project_Id + "', project_id) > 0 "
					//+ " and road like concat('%','" + Road + "','%') "
					+ " and sign = '1' "
					+ " order by id";
			break;
		case 1:// 查询有设备的管井
			Sql = " select t.id, t.longitude, t.latitude, t.wx_lng, t.wx_lat, t.flag, t.sign, t.project_id, t.equip_id, t.top_height, t.equip_height, t.curr_data, t.ctime, t.road, t.rotation, t.attr_id, t.des"
					+ " from view_topo_gj t "
					+ " where instr('" + Project_Id + "', project_id) > 0 "
					+ " and LENGTH(equip_id) = 20"
					+ " and sign = '1' "
					+ " order by id";
			break;
		}
		return Sql;
	}

	/**
	 * 将数据库中 结果集的数据 封装到DevGjBean中
	 * 
	 */
	public boolean getData(ResultSet pRs) {
		boolean IsOK = true;
		try {
			setId(pRs.getString(1));
			setLongitude(pRs.getString(2));
			setLatitude(pRs.getString(3));
			setWX_Lng(pRs.getString(4));
			setWX_Lat(pRs.getString(5));
			setFlag(pRs.getString(6));
			setSign(pRs.getString(7));
			setProject_Id(pRs.getString(8));
			setEquip_Id(pRs.getString(9));
			setTop_Height(pRs.getString(10));
			setEquip_Height(pRs.getString(11));
			setCurr_Data(pRs.getString(12));
			setCTime(pRs.getString(13));
			setRoad(pRs.getString(14));
			setRotation(pRs.getString(15));
			setAttr_Id(pRs.getString(16));
			setDes(pRs.getString(17));
		} catch (SQLException sqlExp) {
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	/**
	 * 得到页面数据
	 * 
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) {
		boolean IsOK = true;
		try {
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setLongitude(CommUtil
					.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
			setWX_Lng(CommUtil
					.StrToGB2312(request.getParameter("WX_Lng")));
			setWX_Lat(CommUtil.StrToGB2312(request.getParameter("WX_lat")));
			setFlag(CommUtil.StrToGB2312(request.getParameter("Flag")));
			setSign(CommUtil.StrToGB2312(request.getParameter("Sign")));
			setProject_Id(CommUtil.StrToGB2312(request
					.getParameter("Project_Id")));
			setEquip_Id(CommUtil.StrToGB2312(request.getParameter("Equip_Id")));
			setTop_Height(CommUtil.StrToGB2312(request
					.getParameter("Top_Height")));
			setBase_Height(CommUtil.StrToGB2312(request
					.getParameter("Base_Height")));
			setEquip_Height(CommUtil.StrToGB2312(request
					.getParameter("Equip_Height")));
			setCurr_Data(CommUtil.StrToGB2312(request
					.getParameter("Curr_Data")));
			setCTime(CommUtil.StrToGB2312(request
					.getParameter("CTime")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
			setRotation(CommUtil.StrToGB2312(request.getParameter("Rotation")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
		} catch (Exception Exp) {
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String Id;
	private String Longitude;
	private String Latitude;
	private String WX_Lng;
	private String WX_Lat;
	private String Flag;
	private String Sign;
	private String Project_Id;
	private String Equip_Id;
	private String Top_Height;
	private String Base_Height;
	private String Equip_Height;
	private String Curr_Data;
	private String CTime;
	private String Road;
	private String Rotation;
	private String Attr_Id;
	private String Des;
	
	private String Token;
	
	private int Cmd;


	public String getWX_Lng() {
		return WX_Lng;
	}

	public void setWX_Lng(String wX_Lng) {
		WX_Lng = wX_Lng;
	}

	public String getWX_Lat() {
		return WX_Lat;
	}

	public void setWX_Lat(String wX_Lat) {
		WX_Lat = wX_Lat;
	}

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}

	public String getEquip_Id() {
		return Equip_Id;
	}

	public void setEquip_Id(String equip_Id) {
		Equip_Id = equip_Id;
	}

	public String getTop_Height() {
		return Top_Height;
	}

	public void setTop_Height(String top_Height) {
		Top_Height = top_Height;
	}

	public String getBase_Height() {
		return Base_Height;
	}

	public void setBase_Height(String base_Height) {
		Base_Height = base_Height;
	}

	public String getEquip_Height() {
		return Equip_Height;
	}

	public void setEquip_Height(String equip_Height) {
		Equip_Height = equip_Height;
	}

	public String getCurr_Data() {
		return Curr_Data;
	}

	public void setCurr_Data(String curr_Data) {
		Curr_Data = curr_Data;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getRoad() {
		return Road;
	}

	public void setRoad(String road) {
		Road = road;
	}

	public String getRotation() {
		return Rotation;
	}

	public void setRotation(String rotation) {
		Rotation = rotation;
	}

	public String getAttr_Id() {
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id) {
		Attr_Id = attr_Id;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

}