package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.ProjectInfoJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class ProjectInfoBean extends RmiBean 
{
	public final static long serialVersionUID = RmiBean.RMI_PROJECT_INFO;
	public long getClassId()
	{
		return serialVersionUID;
	}

	public ProjectInfoBean()
	{
		super.className = "ProjectInfoBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , String> TokenList) throws ServletException, IOException
	{

		PrintWriter output = null;
		try
		{
			getHtmlData(request);
			ProjectInfoJsonBean Json = new ProjectInfoJsonBean();
			Json.setUrl(Url);
			Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
			if(TokenList.containsKey(Token))
			{

			}
			else
			{
				//鉴权失败
				Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
			}

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
			response.setCharacterEncoding("UTF-8");
			output = response.getWriter();
			output.write(jsonObj.toString());
			output.flush();

			System.out.println("AppGisJson:" + jsonObj.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			output.close();
		}

	}

	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
		case 0://查询全部
			Sql = " select  t.id, t.cname, t.Longitude, t.Latitude, t.wx_lng, t.wx_lat, t.MapLev , t.MapAngle , t.Demo "
					+ " from project_info t order by t.id";
			break;
		case 1://查询单个
			Sql = " select  t.id, t.cname, t.Longitude, t.Latitude, t.wx_lng, t.wx_lat, t.MapLev , t.MapAngle , t.Demo "
					+ " from project_info t where t.id = '" + Id + "' order by t.id";
			break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setCName(pRs.getString(2));
			setLongitude(pRs.getString(3));
			setLatitude(pRs.getString(4));
			setWX_Lng(pRs.getString(5));
			setWX_Lat(pRs.getString(6));
			setMapLev(pRs.getString(7));
			setMapAngle(pRs.getString(8));
			setDemo(pRs.getString(9));
		}
		catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		return IsOK;
	}

	public boolean getHtmlData(HttpServletRequest request)
	{
		boolean IsOK = true;
		try
		{
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
			setWX_Lng(CommUtil.StrToGB2312(request.getParameter("WX_Lng")));
			setWX_Lat(CommUtil.StrToGB2312(request.getParameter("WX_lat")));
			setMapLev(CommUtil.StrToGB2312(request.getParameter("MapLev")));
			setMapAngle(CommUtil.StrToGB2312(request.getParameter("MapAngle")));
			setDemo(CommUtil.StrToGB2312(request.getParameter("Demo")));
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}

	private String Id;
	private String CName;
	private String Longitude;
	private String Latitude;
	private String WX_Lng;
	private String WX_Lat;
	private String MapLev;
	private String MapAngle;
	private String Demo;
	private String Sid;

	private String Token;

	public String getMapAngle() {
		return MapAngle;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
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

	public void setMapAngle(String mapAngle) {
		MapAngle = mapAngle;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
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

	public String getMapLev() {
		return MapLev;
	}

	public void setMapLev(String mapLev) {
		MapLev = mapLev;
	}

	public String getDemo() {
		return Demo;
	}

	public void setDemo(String demo) {
		Demo = demo;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}


}