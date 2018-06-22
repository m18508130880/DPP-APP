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

import json.CheckTaskGXJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class CheckTaskGXBean extends RmiBean
{
	public final static long	serialVersionUID	= RmiBean.RMI_CHECK_GX;

	public long getClassId()
	{
		return serialVersionUID;
	}

	public CheckTaskGXBean()
	{
		super.className = "CheckTaskGXBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response,
			Rmi pRmi, boolean pFromZone, String Url,
			HashMap<String, Date> TokenList) throws ServletException,
			IOException {
		PrintWriter output = null;
		try {
			getHtmlData(request);

			CheckTaskGXJsonBean json = new CheckTaskGXJsonBean();
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
						ArrayList<?> checkTaskGXList = (ArrayList<?>) msgBean
								.getMsg();
						CData = objToJson(checkTaskGXList, CData);
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
			CheckTaskGXBean RealJson = (CheckTaskGXBean) iterator.next();
			CheckTaskGXJsonBean json = new CheckTaskGXJsonBean();
			json.setSN(RealJson.getSN());
			json.setId(RealJson.getId());
			json.setProject_Id(RealJson.getProject_Id());
			json.setCT_SN(RealJson.getCT_SN());
			json.setStatus(RealJson.getStatus());
			json.setImages(RealJson.getImages());
			json.setCheck_Time(RealJson.getCheck_Time());
			json.setStart_Id(RealJson.getStart_Id());
			json.setEnd_Id(RealJson.getEnd_Id());
			json.setRoad(RealJson.getRoad());
			json.setDes(RealJson.getDes());

			CData.add(json);
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
				Sql = " select t.sn, t.id, t.project_id, t.ct_sn, t.status, t.images, t.check_time, t.start_id, t.end_id, t.road, t.des " +
					  " from view_check_gx t " + 
					  " where t.project_id = '" + Project_Id + "'" + 
					  " and t.id like '" + Id + "%'" + 
					  " order by t.sn ";
				break;
			case 1:// 查询根据sn查询
				Sql = " select t.sn, t.id, t.project_id, t.ct_sn, t.status, t.images, t.check_time, t.start_id, t.end_id, t.road, t.des " +
					  " from view_check_gx t " + 
					  " where t.ct_sn = '" + CT_SN + "'" +
					  " order by t.sn ";
				break;
			case 12:// 删除
				Sql = " delete from check_task_gx where id = '" + Id + "' and project_id = '" + Project_Id + "'";
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
			setSN(pRs.getString(1));
			setId(pRs.getString(2));
			setProject_Id(pRs.getString(3));
			setCT_SN(pRs.getString(4));
			setStatus(pRs.getString(5));
			setImages(pRs.getString(6));
			setCheck_Time(pRs.getString(7));
			setStart_Id(pRs.getString(8));
			setEnd_Id(pRs.getString(9));
			setRoad(pRs.getString(10));
			setDes(pRs.getString(11));
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
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setCT_SN(CommUtil.StrToGB2312(request.getParameter("CT_SN")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			setImages(CommUtil.StrToGB2312(request.getParameter("Images")));
			setCheck_Time(CommUtil.StrToGB2312(request.getParameter("Check_Time")));
			setStart_Id(CommUtil.StrToGB2312(request.getParameter("Start_Id")));
			setEnd_Id(CommUtil.StrToGB2312(request.getParameter("End_Id")));
			setRoad(CommUtil.StrToGB2312(request.getParameter("Road")));
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

	private String	SN;
	private String	Id;
	private String	Project_Id;
	private String	CT_SN;
	private String	Status;
	private String	Images;
	private String	Check_Time;
	private String	Start_Id;
	private String	End_Id;
	private String	Road;
	private String	Des;

	private String Token;
	
	private int Cmd;

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
	}

	public String getCT_SN() {
		return CT_SN;
	}

	public void setCT_SN(String cT_SN) {
		CT_SN = cT_SN;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getImages() {
		return Images;
	}

	public void setImages(String images) {
		Images = images;
	}

	public String getCheck_Time() {
		return Check_Time;
	}

	public void setCheck_Time(String check_Time) {
		Check_Time = check_Time;
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

	public String getRoad() {
		return Road;
	}

	public void setRoad(String road) {
		Road = road;
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