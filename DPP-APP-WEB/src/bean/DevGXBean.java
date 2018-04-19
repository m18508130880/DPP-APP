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

import json.DevGXJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class DevGXBean extends RmiBean
{	
	public final static long serialVersionUID =RmiBean.RMI_DEVGX;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public DevGXBean()
	{
		super.className = "DevGXBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , Date> TokenList) throws ServletException, IOException
	{
		PrintWriter output = null;
		try {
			getHtmlData(request);

			DevGXJsonBean json = new DevGXJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsKey(Token)) {
				TokenList.put(Token, new Date());
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
				case 1:
					// 获取管线信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						List<Object> CData = new ArrayList<Object>();
						ArrayList<?> devGXList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(devGXList, CData);
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
	
	public List<Object> objToJson(ArrayList<?> devGXList, List<Object> CData){
		Iterator<?> devGXIterator = devGXList.iterator();
		while (devGXIterator.hasNext()) {
			DevGXBean RealJson = (DevGXBean) devGXIterator
					.next();
			DevGXJsonBean devGXJson = new DevGXJsonBean();
			devGXJson.setId(RealJson.getId());
			devGXJson.setDiameter(RealJson.getDiameter());
			devGXJson.setLength(RealJson.getLength());
			devGXJson.setStart_Id(RealJson.getStart_Id());
			devGXJson.setEnd_Id(RealJson.getEnd_Id());
			devGXJson.setStart_Height(RealJson.getStart_Height());
			devGXJson.setEnd_Height(RealJson.getEnd_Height());
			devGXJson.setMaterial(RealJson.getMaterial());
			devGXJson.setBuried_Year(RealJson.getBuried_Year());
			devGXJson.setData_Lev(RealJson.getData_Lev());
			devGXJson.setProject_Id(RealJson.getProject_Id());
			devGXJson.setProject_Name(RealJson.getProject_Name());
			devGXJson.setEquip_Id(RealJson.getEquip_Id());
			devGXJson.setEquip_Name(RealJson.getEquip_Name());
			devGXJson.setCurr_Data(RealJson.getCurr_Data());
			devGXJson.setRoad(RealJson.getRoad());
			devGXJson.setFlag(RealJson.getFlag());
			
			CData.add(devGXJson);
		}
		return CData;
	}
	
	public String getSql(int pCmd)
	{  
		String Sql = "";
		switch (pCmd)
		{
		case 0://查询（类型&项目）
			Sql = " select t.id, t.diameter, t.length, t.start_id, t.end_id, t.start_height, t.end_height, t.material, t.buried_year, t.data_lev, t.project_id, t.project_name, t.equip_id, t.equip_name ,round((t.curr_data),2), t.road, t.flag" +
					" from view_dev_gx t " +		
					" where t.project_id = '" + Project_Id + "' " +
					" order by t.id ";
			break;
		case 1://查询（项目&子系统）
			Sql = " select t.id, t.diameter, t.length, t.start_id, t.end_id, t.start_height, t.end_height, t.material, t.buried_year, t.data_lev, t.project_id, t.project_name, t.equip_id, t.equip_name ,round((t.curr_data),2), t.road, t.flag" +
					" from view_dev_gx t " +	
					" where t.project_id = '" + Project_Id + "'" + 
					" and substr(t.id, 3, 3) = '"+ Subsys_Id.substring(2,5) +"'" +
					" order by t.id ";
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
			setDiameter(pRs.getString(2));
			setLength(pRs.getString(3));
			setStart_Id(pRs.getString(4));
			setEnd_Id(pRs.getString(5));
			setStart_Height(pRs.getString(6));
			setEnd_Height(pRs.getString(7));
			setMaterial(pRs.getString(8));
			setBuried_Year(pRs.getString(9));
			setData_Lev(pRs.getString(10));
			setProject_Id(pRs.getString(11));
			setProject_Name(pRs.getString(12));
			setEquip_Id(pRs.getString(13));
			setEquip_Name(pRs.getString(14));
			setCurr_Data(pRs.getString(15));
			setRoad(pRs.getString(16));
			setFlag(pRs.getString(17));
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
			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setDiameter(CommUtil.StrToGB2312(request.getParameter("Diameter")));
			setLength(CommUtil.StrToGB2312(request.getParameter("Length")));
			setStart_Id(CommUtil.StrToGB2312(request.getParameter("Start_Id")));
			setEnd_Id(CommUtil.StrToGB2312(request.getParameter("End_Id")));
			setStart_Height(CommUtil.StrToGB2312(request.getParameter("Start_Height")));
			setEnd_Height(CommUtil.StrToGB2312(request.getParameter("End_Height")));
			setMaterial(CommUtil.StrToGB2312(request.getParameter("Material")));
			setBuried_Year(CommUtil.StrToGB2312(request.getParameter("Buried_Year")));
			setData_Lev(CommUtil.StrToGB2312(request.getParameter("Data_Lev")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setProject_Name(CommUtil.StrToGB2312(request.getParameter("Project_Name")));
			setEquip_Id(CommUtil.StrToGB2312(request.getParameter("Equip_Id")));
			setEquip_Name(CommUtil.StrToGB2312(request.getParameter("Equip_Name")));
			setSubsys_Id(CommUtil.StrToGB2312(request.getParameter("Subsys_Id")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
			setFlag(CommUtil.StrToGB2312(request.getParameter("Flag")));
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));
			
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Id;
	private String Diameter;
	private String Length;	
	private String Start_Id;
	private String End_Id;
	private String Start_Height;
	private String End_Height;
	private String Material;
	private String Buried_Year;
	private String Data_Lev;
	private String Road;
	private String Flag;
	
	private String Project_Id;	
	private String Project_Name;	
	private String Equip_Id;	
	private String Equip_Name;
	private String Curr_Data;
	private String Subsys_Id;

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

	public String getFlag()
	{
		return Flag;
	}

	public void setFlag(String flag)
	{
		Flag = flag;
	}

	public String getRoad()
	{
		return Road;
	}

	public void setRoad(String road)
	{
		Road = road;
	}
	
	public String getCurr_Data() {
		return Curr_Data;
	}

	public void setCurr_Data(String curr_Data) {
		Curr_Data = curr_Data;
	}

	
	
	public String getSubsys_Id() {
		return Subsys_Id;
	}

	public void setSubsys_Id(String subsys_Id) {
		Subsys_Id = subsys_Id;
	}

	public String getStart_Height() {
		return Start_Height;
	}

	public void setStart_Height(String start_Height) {
		Start_Height = start_Height;
	}

	public String getEnd_Height() {
		return End_Height;
	}

	public void setEnd_Height(String end_Height) {
		End_Height = end_Height;
	}


	public String getBuried_Year() {
		return Buried_Year;
	}

	public void setBuried_Year(String buried_Year) {
		Buried_Year = buried_Year;
	}

	public String getData_Lev() {
		return Data_Lev;
	}

	public void setData_Lev(String data_Lev) {
		Data_Lev = data_Lev;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDiameter() {
		return Diameter;
	}

	public void setDiameter(String diameter) {
		Diameter = diameter;
	}

	public String getLength() {
		return Length;
	}

	public void setLength(String length) {
		Length = length;
	}

	public String getStart_Id() {
		return Start_Id;
	}

	public void setStart_Id(String start_Id) {
		Start_Id = start_Id;
	}

	public String getEnd_Id() {
		return End_Id;
	}

	public void setEnd_Id(String end_Id) {
		End_Id = end_Id;
	}

	public String getMaterial() {
		return Material;
	}

	public void setMaterial(String material) {
		Material = material;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}

	public String getProject_Name() {
		return Project_Name;
	}

	public void setProject_Name(String project_Name) {
		Project_Name = project_Name;
	}

	public String getEquip_Id() {
		return Equip_Id;
	}

	public void setEquip_Id(String equip_Id) {
		Equip_Id = equip_Id;
	}

	public String getEquip_Name() {
		return Equip_Name;
	}

	public void setEquip_Name(String equip_Name) {
		Equip_Name = equip_Name;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}


