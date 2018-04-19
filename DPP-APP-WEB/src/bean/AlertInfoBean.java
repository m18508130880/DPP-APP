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

import json.AlertInfoJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class AlertInfoBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_ALERT;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public AlertInfoBean()
	{
		super.className = "AlertInfoBean";
	}

	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , Date> TokenList) throws ServletException, IOException
	{
		PrintWriter output = null;
		try {
			getHtmlData(request);

			AlertInfoJsonBean json = new AlertInfoJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsKey(Token)) {
				TokenList.put(Token, new Date());
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
					// 获取管井信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						List<Object> CData = new ArrayList<Object>();
						ArrayList<?> alertInfoList = (ArrayList<?>) msgBean
								.getMsg();
						Iterator<?> alertInfoIterator = alertInfoList.iterator();
						while (alertInfoIterator.hasNext()) {
							AlertInfoBean RealJson = (AlertInfoBean) alertInfoIterator
									.next();
							AlertInfoJsonBean alertInfoJson = new AlertInfoJsonBean();
							alertInfoJson.setCpm_Id(RealJson.getCpm_Id());
							alertInfoJson.setEquip_Id(RealJson.getEquip_Id());
							alertInfoJson.setCName(RealJson.getCName());
							alertInfoJson.setAttr_Id(RealJson.getAttr_Id());
							alertInfoJson.setAttr_Name(RealJson.getAttr_Name());
							alertInfoJson.setLevel(RealJson.getLevel());
							alertInfoJson.setCTime(RealJson.getCTime());
							alertInfoJson.setCurr_Data(RealJson.getCurr_Data());
							alertInfoJson.setGJ_Id(RealJson.getGJ_Id());
							alertInfoJson.setProject_Id(RealJson.getProject_Id());
							alertInfoJson.setStatus(RealJson.getStatus());
							alertInfoJson.setUnit(RealJson.getUnit());
							alertInfoJson.setDes(RealJson.getDes());
							alertInfoJson.setLongitude(RealJson.getLongitude());
							alertInfoJson.setLatitude(RealJson.getLatitude());
							
							CData.add(alertInfoJson);
						}
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
			
			System.out.println("AppGisJson:" + jsonObj.toString() + ";");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
		
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
		case 0:// 查询最新
			Sql = " select t.cpm_id, t.equip_id, t.cname, t.attr_id, t.attr_name, t.level, t.ctime, t.cdata, t.gj_id, t.project_id, t.status, t.unit, t.des, t.longitude, t.latitude " + 
					" from view_alert_now t " + 
					" where t.project_id = '" + Project_Id + "' and t.status = '0' " + 
					" order by t.ctime ";
			break;
		case 1:// 查询全部
			Sql = " select t.cpm_id, t.equip_id, t.cname, t.attr_id, t.attr_name, t.level, t.ctime, t.cdata, t.gj_id, t.project_id, t.status, t.unit, t.des, t.longitude, t.latitude " + 
					" from view_alert_info t " + 
					" where t.project_id = '" + Project_Id + "'"+
					" order by t.ctime desc ";
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
			setCpm_Id(pRs.getString(1));
			setEquip_Id(pRs.getString(2));
			setCName(pRs.getString(3));
			setAttr_Id(pRs.getString(4));
			setAttr_Name(pRs.getString(5));
			setLevel(pRs.getString(6));
			setCTime(pRs.getString(7));
			setCurr_Data(pRs.getString(8));
			setGJ_Id(pRs.getString(9));
			setProject_Id(pRs.getString(10));
			setStatus(pRs.getString(11));
			setUnit(pRs.getString(12));
			setDes(pRs.getString(13));
			setLongitude(pRs.getString(14));
			setLatitude(pRs.getString(15));
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
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setEquip_Id(CommUtil.StrToGB2312(request.getParameter("Equip_Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setCurr_Data(CommUtil.StrToGB2312(request.getParameter("Curr_Data")));
			setGJ_Id(CommUtil.StrToGB2312(request.getParameter("GJ_Id")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			setLongitude(CommUtil.StrToGB2312(request.getParameter("Longitude")));
			setLatitude(CommUtil.StrToGB2312(request.getParameter("Latitude")));
			
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Cpm_Id;
	private String Equip_Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String Level;
	private String CTime;
	private String Curr_Data;
	private String GJ_Id;
	private String Project_Id;
	private String Status;
	private String Unit;
	private String Des;

	private String Longitude;
	private String Latitude;

	private int Cmd;
	private String Token;

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
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

	public String getCpm_Id()
	{
		return Cpm_Id;
	}

	public void setCpm_Id(String cpm_Id)
	{
		Cpm_Id = cpm_Id;
	}

	public String getProject_Id()
	{
		return Project_Id;
	}

	public void setProject_Id(String project_Id)
	{
		Project_Id = project_Id;
	}

	public String getEquip_Id()
	{
		return Equip_Id;
	}

	public void setEquip_Id(String equip_Id)
	{
		Equip_Id = equip_Id;
	}

	public String getCName()
	{
		return CName;
	}

	public void setCName(String cName)
	{
		CName = cName;
	}

	public String getAttr_Id()
	{
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id)
	{
		Attr_Id = attr_Id;
	}

	public String getAttr_Name()
	{
		return Attr_Name;
	}

	public void setAttr_Name(String attr_Name)
	{
		Attr_Name = attr_Name;
	}

	public String getLevel()
	{
		return Level;
	}

	public void setLevel(String level)
	{
		Level = level;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	public String getCurr_Data() {
		return Curr_Data;
	}

	public void setCurr_Data(String curr_Data) {
		Curr_Data = curr_Data;
	}

	public String getGJ_Id()
	{
		return GJ_Id;
	}

	public void setGJ_Id(String gJ_Id)
	{
		GJ_Id = gJ_Id;
	}

	public String getStatus()
	{
		return Status;
	}

	public void setStatus(String status)
	{
		Status = status;
	}

	public String getUnit()
	{
		return Unit;
	}

	public void setUnit(String unit)
	{
		Unit = unit;
	}

	public String getDes()
	{
		return Des;
	}

	public void setDes(String des)
	{
		Des = des;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
}