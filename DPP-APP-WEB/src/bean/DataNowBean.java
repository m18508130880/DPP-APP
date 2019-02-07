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

import json.DataNowJsonBean;
import json.DevGXJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

public class DataNowBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_DATA_NOW;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public DataNowBean()
	{
		super.className = "DataNowBean";
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
			DataNowBean RealJson = (DataNowBean) dataGJIterator
					.next();
			DataNowJsonBean dataGJJson = new DataNowJsonBean();
			dataGJJson.setSN(RealJson.getSN());
			dataGJJson.setGJ_Id((RealJson.getGJ_Id());
			dataGJJson.setGJ_Name(RealJson.getGJ_Name());
			dataGJJson.setLongitude(RealJson.getLongitude());
			dataGJJson.setLatitude(RealJson.getLatitude());
			dataGJJson.setProject_Id(RealJson.getProject_Id());
			dataGJJson.setTop_Height(RealJson.getTop_Height());
			dataGJJson.setBase_Height(RealJson.getBase_Height());
			dataGJJson.setEquip_Height(RealJson.getEquip_Height());
			dataGJJson.setPId(RealJson.getPId());
			dataGJJson.setAddrs(RealJson.getAddrs());
			dataGJJson.setCode(RealJson.getCode());
			dataGJJson.setSign(RealJson.getSign());
			dataGJJson.setCName(RealJson.getCName());
			dataGJJson.setAttr_Id(RealJson.getAttr_Id());
			dataGJJson.setAttr_Name;
			dataGJJson.setCTime;
			dataGJJson.setValue;
			dataGJJson.setUnit;
		}
		return CData;
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
		case 0: // 根据日期查询
			
			break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			private String SN;
			private String GJ_Id;
			private String GJ_Name;
			private String Longitude;
			private String Latitude;
			private String Project_Id;
			private String Top_Height;
			private String Base_Height;
			private String Equip_Height;
			private String PId;
			private String Addrs;
			private String Code;
			private String Sign;
			private String CName;
			private String Attr_Id;
			private String Attr_Name;
			private String CTime;
			private String Value;
			private String Unit;
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
	private String GJ_Id;
	private String GJ_Name;
	private String Longitude;
	private String Latitude;
	private String Project_Id;
	private String Top_Height;
	private String Base_Height;
	private String Equip_Height;
	private String PId;
	private String Addrs;
	private String Code;
	private String Sign;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String Value;
	private String Unit;

	private String Token;
	private int Cmd;
	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
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

	public String getEquip_Height() {
		return Equip_Height;
	}

	public void setEquip_Height(String equip_Height) {
		Equip_Height = equip_Height;
	}

	public String getPId() {
		return PId;
	}

	public void setPId(String pId) {
		PId = pId;
	}

	public String getAddrs() {
		return Addrs;
	}

	public void setAddrs(String addrs) {
		Addrs = addrs;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getSign() {
		return Sign;
	}

	public void setSign(String sign) {
		Sign = sign;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
	}

	public String getAttr_Id() {
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id) {
		Attr_Id = attr_Id;
	}

	public String getAttr_Name() {
		return Attr_Name;
	}

	public void setAttr_Name(String attr_Name) {
		Attr_Name = attr_Name;
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