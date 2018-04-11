package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.DevGJJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class DevGJBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_DEVGJ;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public DevGJBean()
	{
		super.className = "DevGJBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , String> TokenList) throws ServletException, IOException
	{
		PrintWriter output = null;
		try {
			getHtmlData(request);
			DevGJJsonBean json = new DevGJJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			List<Object> CData = new ArrayList<Object>();
			if (TokenList.containsKey(Token)) {
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
					// 获取管井信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> devGJList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(devGJList, CData);
						json.setCData(CData);
						json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					}
					break;
				case 1:
					ArrayList<?> gjObj = (ArrayList<?>)msgBean.getMsg();
					
					DevGXBean tmpGXBean = new DevGXBean();
					tmpGXBean.setProject_Id(Project_Id);
					tmpGXBean.setSubsys_Id(Subsys_Id);
					msgBean = pRmi.RmiExec(1, tmpGXBean, 0, 25);
					ArrayList<?> gxObj = (ArrayList<?>)msgBean.getMsg();
					
					//tmpGXBean.objToJson(gxObj, CData);
					GjGxUtil analog = new GjGxUtil();
					ArrayList<?> gjList = analog.AnalogGJList(gjObj, gxObj, Subsys_Id+"001");
					System.out.println("gjList.size:"+gjList.size());
					CData = objToJson(gjList, CData);
					json.setCData(CData);
					json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
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
			
			System.out.println("AppGisJson:" + jsonObj.toString() + ";");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
		
	}

	public List<Object> objToJson(ArrayList<?> devGJList, List<Object> CData){
		Iterator<?> devGJIterator = devGJList.iterator();
		while (devGJIterator.hasNext()) {
			DevGJBean RealJson = (DevGJBean) devGJIterator
					.next();
			DevGJJsonBean devGJJson = new DevGJJsonBean();
			devGJJson.setId(RealJson.getId());
			devGJJson.setLongitude(RealJson.getLongitude());
			devGJJson.setLatitude(RealJson.getLatitude());
			devGJJson.setTop_Height(RealJson.getTop_Height());
			devGJJson.setBase_Height(RealJson.getBase_Height());
			devGJJson.setSize(RealJson.getSize());
			devGJJson.setIn_Id(RealJson.getIn_Id());
			devGJJson.setOut_Id(RealJson.getOut_Id());
			devGJJson.setMaterial(RealJson.getMaterial());
			devGJJson.setFlag(RealJson.getFlag());
			devGJJson.setData_Lev(RealJson.getData_Lev());
			devGJJson.setCurr_Data(RealJson.getCurr_Data());
			devGJJson.setSign(RealJson.getSign());
			devGJJson.setProject_Id(RealJson.getProject_Id());
			devGJJson.setProject_Name(RealJson.getProject_Name());
			devGJJson.setEquip_Id(RealJson.getEquip_Id());
			devGJJson.setEquip_Name(RealJson.getEquip_Name());
			devGJJson.setEquip_Height(RealJson.getEquip_Height());
			devGJJson.setEquip_Tel(RealJson.getEquip_Tel());
			devGJJson.setIn_Img(RealJson.getIn_Img());
			devGJJson.setOut_Img(RealJson.getOut_Img());
			devGJJson.setEquip_Time(RealJson.getEquip_Time());
			devGJJson.setRoad(RealJson.getRoad());
			devGJJson.setRotation(RealJson.getRotation());
			
			CData.add(devGJJson);
		}
		return CData;
	}
	/**
	 * 获取相应sql语句
	 * 
	 */
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
		case 0:// 查询（类型&项目）
			Sql = " select t.id, t.Longitude, t.latitude, t.top_Height, t.base_height, t.Size, t.in_id, t.out_id, t.Material, t.Flag, t.Data_Lev, round((t.curr_data),2) , t.sign , t.project_id, t.project_name, t.equip_id ,t.equip_name ,t.equip_height ,t.equip_tel, t.In_Img, t.Out_Img, t.equip_time, t.road, t.rotation" + 
					" from view_dev_gj t " +
					" where t.project_id = '" + Project_Id + "' " + 
					" order by t.id  ";
			break;
		case 1:// 查询（项目&子系统）
			Sql = " select t.id, t.Longitude, t.latitude, t.top_Height, t.base_height, t.Size, t.in_id, t.out_id, t.Material, t.Flag, t.Data_Lev, round((t.curr_data),2) , t.sign , t.project_id, t.project_name, t.equip_id ,t.equip_name ,t.equip_height ,t.equip_tel, t.In_Img, t.Out_Img, t.equip_time, t.road, t.rotation" + 
					" from view_dev_gj t " + 
					" where t.project_id = '" + Project_Id + "'" + 
					" and substr(t.id, 3, 3) = '" + Subsys_Id.substring(2) + "'" + 
					" order by t.id";
			break;

		}
		return Sql;
	}

	/**
	 * 将数据库中 结果集的数据 封装到DevGjBean中
	 * 
	 */
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setLongitude(pRs.getString(2));
			setLatitude(pRs.getString(3));
			setTop_Height(pRs.getString(4));
			setBase_Height(pRs.getString(5));
			setSize(pRs.getString(6));
			setIn_Id(pRs.getString(7));
			setOut_Id(pRs.getString(8));
			setMaterial(pRs.getString(9));
			setFlag(pRs.getString(10));
			setData_Lev(pRs.getString(11));
			setCurr_Data(pRs.getString(12));
			setSign(pRs.getString(13));
			setProject_Id(pRs.getString(14));
			setProject_Name(pRs.getString(15));
			setEquip_Id(pRs.getString(16));
			setEquip_Name(pRs.getString(17));
			setEquip_Height(pRs.getString(18));
			setEquip_Tel(pRs.getString(19));
			setIn_Img(pRs.getString(20));
			setOut_Img(pRs.getString(21));
			setEquip_Time(pRs.getString(22));
			setRoad(pRs.getString(23));
			setRotation(pRs.getString(24));
		}
		catch (SQLException sqlExp)
		{
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
	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
			setTop_Height(CommUtil.StrToGB2312(request.getParameter("Top_Height")));
			setBase_Height(CommUtil.StrToGB2312(request.getParameter("Base_Height")));
			setSize(CommUtil.StrToGB2312(request.getParameter("Size")));
			setIn_Id(CommUtil.StrToGB2312(request.getParameter("In_Id")));
			setOut_Id(CommUtil.StrToGB2312(request.getParameter("Out_Id")));
			setMaterial(CommUtil.StrToGB2312(request.getParameter("Material")));
			setFlag(CommUtil.StrToGB2312(request.getParameter("Flag")));
			setData_Lev(CommUtil.StrToGB2312(request.getParameter("Data_Lev")));
			setCurr_Data(CommUtil.StrToGB2312(request.getParameter("Curr_Data")));
			setSign(CommUtil.StrToGB2312(request.getParameter("Sign")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setEquip_Id(CommUtil.StrToGB2312(request.getParameter("Equip_Id")));
			setEquip_Name(CommUtil.StrToGB2312(request.getParameter("Equip_Name")));
			setEquip_Height(CommUtil.StrToGB2312(request.getParameter("Equip_Height")));
			setEquip_Tel(CommUtil.StrToGB2312(request.getParameter("Equip_Tel")));
			setIn_Img(CommUtil.StrToGB2312(request.getParameter("In_Img")));
			setOut_Img(CommUtil.StrToGB2312(request.getParameter("Out_Img")));
			setEquip_Time(CommUtil.StrToGB2312(request.getParameter("Equip_Time")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
			setRotation(CommUtil.StrToGB2312(request.getParameter("Rotation")));
			
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			setSubsys_Id(CommUtil.StrToGB2312(request.getParameter("Subsys_Id")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String	Id;
	private String	Longitude;
	private String	Latitude;
	private String	Top_Height;
	private String	Base_Height;
	private String	Size;
	private String	In_Id;
	private String	Out_Id;
	private String	Material;
	private String	Flag;
	private String	Data_Lev;

	private String	Sign;
	private String	Project_Id;
	private String	Project_Name;
	private String	Equip_Id;
	private String	Equip_Name;
	private String	Equip_Height;
	private String	Equip_Tel;
	private String	Equip_Time;
	private String	Road;
	private String	Rotation;

	private String	Curr_Data;
	private String	Subsys_Id;
	private String	In_Img;
	private String	Out_Img;
	
	private String	WX_Lng;
	private String	WX_Lat;

	private int Cmd;
	private String Token;

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

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

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getRotation()
	{
		return Rotation;
	}

	public void setRotation(String rotation)
	{
		Rotation = rotation;
	}

	public String getRoad()
	{
		return Road;
	}

	public void setRoad(String road)
	{
		Road = road;
	}

	public String getEquip_Time()
	{
		return Equip_Time;
	}

	public void setEquip_Time(String equip_Time)
	{
		Equip_Time = equip_Time;
	}

	public String getEquip_Tel()
	{
		return Equip_Tel;
	}

	public void setEquip_Tel(String equip_Tel)
	{
		Equip_Tel = equip_Tel;
	}

	public String getSubsys_Id()
	{
		return Subsys_Id;
	}

	public void setSubsys_Id(String subsys_Id)
	{
		Subsys_Id = subsys_Id;
	}

	public String getSize()
	{
		return Size;
	}

	public void setSize(String size)
	{
		Size = size;
	}

	public String getData_Lev()
	{
		return Data_Lev;
	}

	public void setData_Lev(String data_Lev)
	{
		Data_Lev = data_Lev;
	}

	public String getFlag()
	{
		return Flag;
	}

	public void setFlag(String flag)
	{
		Flag = flag;
	}

	public String getEquip_Id()
	{
		return Equip_Id;
	}

	public void setEquip_Id(String equip_Id)
	{
		Equip_Id = equip_Id;
	}

	public String getEquip_Name()
	{
		return Equip_Name;
	}

	public void setEquip_Name(String equip_Name)
	{
		Equip_Name = equip_Name;
	}

	public String getProject_Name()
	{
		return Project_Name;
	}

	public String getEquip_Height()
	{
		return Equip_Height;
	}

	public void setEquip_Height(String equip_Height)
	{
		Equip_Height = equip_Height;
	}

	public void setProject_Name(String project_Name)
	{
		Project_Name = project_Name;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getLongitude()
	{
		return Longitude;
	}

	public void setLongitude(String longitude)
	{
		Longitude = longitude;
	}

	public String getLatitude()
	{
		return Latitude;
	}

	public void setLatitude(String latitude)
	{
		Latitude = latitude;
	}

	public String getTop_Height()
	{
		return Top_Height;
	}

	public void setTop_Height(String top_Height)
	{
		Top_Height = top_Height;
	}

	public String getBase_Height()
	{
		return Base_Height;
	}

	public void setBase_Height(String base_Height)
	{
		Base_Height = base_Height;
	}

	public String getIn_Id()
	{
		return In_Id;
	}

	public void setIn_Id(String in_Id)
	{
		In_Id = in_Id;
	}

	public String getOut_Id()
	{
		return Out_Id;
	}

	public void setOut_Id(String out_Id)
	{
		Out_Id = out_Id;
	}

	public String getMaterial()
	{
		return Material;
	}

	public void setMaterial(String material)
	{
		Material = material;
	}

	public String getCurr_Data()
	{
		return Curr_Data;
	}

	public void setCurr_Data(String curr_Data)
	{
		Curr_Data = curr_Data;
	}

	public String getProject_Id()
	{
		return Project_Id;
	}

	public void setProject_Id(String project_Id)
	{
		Project_Id = project_Id;
	}
	
	public String getSign()
	{
		return Sign;
	}

	public void setSign(String sign)
	{
		Sign = sign;
	}

	public String getIn_Img()
	{
		return In_Img;
	}

	public void setIn_Img(String in_Img)
	{
		In_Img = in_Img;
	}

	public String getOut_Img()
	{
		return Out_Img;
	}

	public void setOut_Img(String out_Img)
	{
		Out_Img = out_Img;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}