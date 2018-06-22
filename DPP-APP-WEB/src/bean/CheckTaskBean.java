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

import json.CheckTaskJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class CheckTaskBean extends RmiBean 
{
	public final static long serialVersionUID = RmiBean.RMI_CHECK_TASK;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public CheckTaskBean()
	{
		super.className = "CheckTaskBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, String> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			CheckTaskJsonBean json = new CheckTaskJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsValue(Token)) {
				List<Object> CData = new ArrayList<Object>();
				msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
				switch (Cmd) {
				case 0:
				case 1:
					// 获取管井信息
					if (msgBean.getStatus() == MsgBean.STA_SUCCESS) {
						ArrayList<?> checkTaskList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(checkTaskList, CData);
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
	
	public List<Object> objToJson(ArrayList<?> list, List<Object> CData){
		Iterator<?> iterator = list.iterator();
		while (iterator.hasNext()) {
			CheckTaskBean RealJson = (CheckTaskBean) iterator.next();
			CheckTaskBean json = new CheckTaskBean();
			json.setSN(RealJson.getSN());
			json.setLeader(RealJson.getLeader());
			json.setStaff(RealJson.getStaff());
			json.setCTime(RealJson.getCTime());
			json.setEnd_Time(RealJson.getEnd_Time());
			json.setProject_Id(RealJson.getProject_Id());
			json.setGJ_List(RealJson.getGJ_List());
			json.setGX_List(RealJson.getGX_List());
			json.setStatus(RealJson.getStatus());
			CData.add(json);
		}
		return CData;
		
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://查询全部
				Sql = " select t.sn,  t.leader, t.staff, t.ctime, t.end_time, t.project_id, t.status, t.GJ_List, t.GX_List, t.Des "
						+ " from check_task t order by t.ctime";
				break;
			case 1://查询项目
				Sql = " select t.sn,  t.leader, t.staff, t.ctime, t.end_time, t.project_id, t.status, t.GJ_List, t.GX_List, t.Des "
						+ " from check_task t "
						+ "where project_id = '" + Project_Id + "' order by t.ctime";
				break;
			case 10://添加
				Sql = " insert into check_task( leader, staff, ctime, end_time, project_id, gj_list, gx_list, des)" +
					  " values('"+ Leader +"', '"+ Staff +"', '"+ CTime +"', '"+ End_Time +"', '"+ Project_Id +"', '"+ GJ_List +"', '"+ GX_List +"', '"+ Des +"')";
				break;
			case 11://编辑
				Sql = " update check_task set staff= '"+ Staff +"', end_time= '"+ End_Time +"', gj_list= '"+ GJ_List +"', gx_list= '"+ GX_List +"' , des= '"+ Des + "' " +
					  " where sn = '"+ SN +"' and leader = '" + Leader + "' and project_id = '" + Project_Id + "'";
			case 12://删除
				Sql = " delete check_task " +
					  " where sn = '"+ SN +"' and leader = '" + Leader + "' and project_id = '" + Project_Id + "'";
		}
		return Sql;
	}
	
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setLeader(pRs.getString(2));
			setStaff(pRs.getString(3));
			setCTime(pRs.getString(4));
			setEnd_Time(pRs.getString(5));
			setProject_Id(pRs.getString(6));
			setStatus(pRs.getString(7));
			setGJ_List(pRs.getString(8));
			setGX_List(pRs.getString(9));
			setDes(pRs.getString(10));
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
			setSN(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setLeader(CommUtil.StrToGB2312(request.getParameter("Leader")));
			setStaff(CommUtil.StrToGB2312(request.getParameter("Staff")));
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));
			setEnd_Time(CommUtil.StrToGB2312(request.getParameter("End_Time")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setGJ_List(CommUtil.StrToGB2312(request.getParameter("GJ_List")));
			setGX_List(CommUtil.StrToGB2312(request.getParameter("GX_List")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setToken(CommUtil.StrToGB2312(request.getParameter("Token")));

			setCmd(CommUtil.StrToInt(request.getParameter("Cmd")));
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	

	private String SN;
	private String Leader;
	private String Staff;
	private String CTime;
	private String End_Time;
	private String Project_Id;
	private String Status;
	private String GJ_List;
	private String GX_List;
	private String Des;

	private String Token;
	
	private int Cmd;
	
	
	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getLeader() {
		return Leader;
	}

	public void setLeader(String leader) {
		Leader = leader;
	}

	public String getStaff() {
		return Staff;
	}

	public void setStaff(String staff) {
		Staff = staff;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getEnd_Time() {
		return End_Time;
	}

	public void setEnd_Time(String end_Time) {
		End_Time = end_Time;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}

	public String getGJ_List() {
		return GJ_List;
	}

	public void setGJ_List(String gJ_List) {
		GJ_List = gJ_List;
	}

	public String getGX_List() {
		return GX_List;
	}

	public void setGX_List(String gX_List) {
		GX_List = gX_List;
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

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}	
}