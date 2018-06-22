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

import json.DataGJJsonBean;
import json.DevGXJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class DataGJBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_DATAGJ;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public DataGJBean()
	{
		super.className = "DataGJBean";
	}
	
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , String> TokenList) throws ServletException, IOException
	{
		PrintWriter output = null;
		try {
			getHtmlData(request);

			DevGXJsonBean json = new DevGXJsonBean();
			json.setUrl(Url);
			json.setRst(CommUtil.IntToStringLeftFillZero(
					MsgBean.STA_FAILED, 4));
			if (TokenList.containsValue(Token)) {
				switch(Cmd)
				{
				case 0:  //日
					BDate = BDate + " 00:00:00";
					EDate = EDate + " 23:59:59";
					msgBean = pRmi.RmiExec(Cmd, this, 0, 25);
					List<Object> CData = new ArrayList<Object>();
					ArrayList<?> dataGJList = (ArrayList<?>) msgBean
							.getMsg();
					CData = objToJson(dataGJList, CData);
					json.setCData(CData);
					json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));
					break;
				}
			}else {
				// 鉴权失败
				json.setRst(CommUtil.IntToStringLeftFillZero(
						MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
			}

			JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
			response.setCharacterEncoding("UTF-8");
			output = response.getWriter();
			output.write(jsonObj.toString());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}
	}
	
	public List<Object> objToJson(ArrayList<?> dataGJList, List<Object> CData){
		Iterator<?> dataGJIterator = dataGJList.iterator();
		while (dataGJIterator.hasNext()) {
			DataGJBean RealJson = (DataGJBean) dataGJIterator
					.next();
			DataGJJsonBean dataGJJson = new DataGJJsonBean();
			dataGJJson.setSN(RealJson.getSN());
			dataGJJson.setProject_Id(RealJson.getProject_Id());
			dataGJJson.setProject_Name(RealJson.getProject_Name());
			dataGJJson.setGJ_Id(RealJson.getGJ_Id());
			dataGJJson.setGJ_Name(RealJson.getGJ_Name());
			dataGJJson.setAttr_Name(RealJson.getAttr_Name());			
			dataGJJson.setCTime(RealJson.getCTime());
			dataGJJson.setValue(RealJson.getValue());
			dataGJJson.setUnit(RealJson.getUnit());
			dataGJJson.setLev(RealJson.getLev());
			dataGJJson.setDes(RealJson.getDes());
			dataGJJson.setTop_Height(RealJson.getTop_Height());
			dataGJJson.setBase_Height(RealJson.getBase_Height());
			dataGJJson.setMaterial(RealJson.getMaterial());
			dataGJJson.setMin_CTime(RealJson.getMin_CTime());
			dataGJJson.setMax_CTime(RealJson.getMax_CTime());
			CData.add(dataGJJson);
		}
		return CData;
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
		case 0: // 根据日期查询
			Sql = " select '' AS sn, t.project_id, t.project_name, t.gj_id, t.gj_name, t.attr_name, t.ctime, round(avg(t.value),2), t.unit, t.lev, t.des, t.top_height, t.base_height, t.material, " +
					" left((select min(t.ctime) from view_data_gj where gj_id = '" + GJ_Id + "'), 10) AS Min_CTime, " + 
					" left((SELECT MAX(ctime) FROM view_data_gj WHERE gj_id = '" + GJ_Id + "'), 10) AS Max_CTime" +
					" FROM view_data_gj t  " +
					" where t.gj_id = '"+ GJ_Id +"'" + 
					" and t.project_id = '" + Project_Id + "'" +
					" and t.ctime >= date_format('"+BDate+"', '%Y-%m-%d %H-%i-%S')" +
					" and t.ctime <= date_format('"+EDate+"', '%Y-%m-%d %H-%i-%S')" +
					" GROUP BY SUBSTR(ctime,1,13)" +
					" ORDER BY t.ctime " ;
			break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setProject_Id(pRs.getString(2));
			setProject_Name(pRs.getString(3));
			setGJ_Id(pRs.getString(4));
			setGJ_Name(pRs.getString(5));
			setAttr_Name(pRs.getString(6));			
			setCTime(pRs.getString(7));
			setValue(pRs.getString(8));
			setUnit(pRs.getString(9));
			setLev(pRs.getString(10));
			setDes(pRs.getString(11));
			setTop_Height(pRs.getString(12));
			setBase_Height(pRs.getString(13));
			setMaterial(pRs.getString(14));
			setMin_CTime(pRs.getString(15));
			setMax_CTime(pRs.getString(16));
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
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setGJ_Id(CommUtil.StrToGB2312(request.getParameter("GJ_Id")));
			setYear(CommUtil.StrToGB2312(request.getParameter("Year")));
			setMonth(CommUtil.StrToGB2312(request.getParameter("Month")));		
			setWeek(CommUtil.StrToGB2312(request.getParameter("Week")));

			setBDate(CommUtil.StrToGB2312(request.getParameter("BDate")));		
			setEDate(CommUtil.StrToGB2312(request.getParameter("EDate")));

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
	private String Project_Id;
	private String Project_Name;
	private String GJ_Id;
	private String GJ_Name;
	private String Attr_Name;
	private String CTime;
	private String Value;
	private String Unit;
	private String Lev;
	private String Des;
	private String Top_Height;
	private String Base_Height;
	private String Material;
	private String Min_CTime;
	private String Max_CTime;
	
	private int Level;
	private String Year;
	private String Month;
	private String	Week;
	private boolean	Flag;
	private String	Flag_Year;
	
	private String	BDate;
	private String	EDate;
	
	private String Token;
	private int Cmd;

	public String getMin_CTime() {
		return Min_CTime;
	}

	public void setMin_CTime(String min_CTime) {
		Min_CTime = min_CTime;
	}

	public String getMax_CTime() {
		return Max_CTime;
	}

	public void setMax_CTime(String max_CTime) {
		Max_CTime = max_CTime;
	}

	public String getBDate() {
		return BDate;
	}

	public void setBDate(String bDate) {
		BDate = bDate;
	}

	public String getEDate() {
		return EDate;
	}

	public void setEDate(String eDate) {
		EDate = eDate;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getFlag_Year()
	{
		return Flag_Year;
	}

	public void setFlag_Year(String flag_Year)
	{
		Flag_Year = flag_Year;
	}

	public String getWeek()
	{
		return Week;
	}

	public void setWeek(String week)
	{
		Week = week;
	}

	public boolean isFlag()
	{
		return Flag;
	}

	public void setFlag(boolean flag)
	{
		Flag = flag;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getProject_Name() {
		return Project_Name;
	}

	public void setProject_Name(String project_Name) {
		Project_Name = project_Name;
	}

	public String getGJ_Id() {
		return GJ_Id;
	}

	public void setGJ_Id(String gJ_Id) {
		GJ_Id = gJ_Id;
	}

	public String getGJ_Name() {
		return GJ_Name;
	}

	public void setGJ_Name(String gJ_Name) {
		GJ_Name = gJ_Name;
	}

	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
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

	public String getMaterial() {
		return Material;
	}

	public void setMaterial(String material) {
		Material = material;
	}

	public String getAttr_Name() {
		return Attr_Name;
	}

	public void setAttr_Name(String attrName) {
		Attr_Name = attrName;
	}

	public String getCTime() {
		return CTime;
	}

	public void setCTime(String cTime) {
		CTime = cTime;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
	}

	public String getLev() {
		return Lev;
	}

	public void setLev(String lev) {
		Lev = lev;
	}

	public String getDes() {
		return Des;
	}

	public void setDes(String des) {
		Des = des;
	}

	public int getCmd() {
		return Cmd;
	}

	public void setCmd(int cmd) {
		Cmd = cmd;
	}

	public int getLevel()
	{
		return Level;
	}

	public void setLevel(int level)
	{
		Level = level;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getMonth() {
		return Month;
	}

	public void setMonth(String month) {
		Month = month;
	}
}