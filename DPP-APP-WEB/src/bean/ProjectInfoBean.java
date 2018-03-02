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
import json.UserRoleJsonBean;

import com.alibaba.fastjson.JSONObject;

import rmi.Rmi;
import rmi.RmiBean;
import util.*;

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
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			
	    	ProjectInfoJsonBean Json = new ProjectInfoJsonBean();
	    	Json.setUrl(Url);
	    	Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
	    	if(TokenList.containsKey(Token))
	    	{
	    		//msgBean = pRmi.RmiExec(1, roleBeanManage, 0, 25);����Ȩ��
	    		
				msgBean = pRmi.RmiExec(currStatus.getCmd(), roleBeanManage, 0, 25);
				switch(currStatus.getCmd())
				{
					case 0:
						//����Ȩ��
				    	request.getSession().setAttribute("FP_Role_" + Sid, ((Object)msgBean.getMsg()));
				    	
				    	//���ܵ�
				    	msgBean = pRmi.RmiExec(2, this, 0, 25);
				    	request.getSession().setAttribute("FP_Info_" + Sid, ((Object)msgBean.getMsg()));
				    	break;
				    case 1:
				    	//����Ȩ��
				    	if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
						{
							UserRoleBean Manage_Role = (UserRoleBean)msgBean.getMsg();
							Json.setRst("0000");
							String [] points = Manage_Role.getPoint().split(",");
							for(int i=0; i<points.length && points[i].length()>0; i++)
							{
								ProjectInfoBean projectInfoBean = new ProjectInfoBean();
								projectInfoBean.setId("");
								msgBean = pRmi.RmiExec(0, projectInfoBean, 0, 25);
								projectInfoBean = (ProjectInfoBean)msgBean.getMsg();
								RealJson.setId(List[i].split(",")[0]);
								CData.add(RealJson);
							}
							Json.setCData(CData);
							}
						}
				    	
				    	/*//�豸����
						ProjectInfoBean projectInfoBean = new ProjectInfoBean();
						msgBean = pRmi.RmiExec(0, projectInfoBean, 0,25);
						request.getSession().setAttribute("project_Info_" + Sid, (Object)msgBean.getMsg());*/
				    	break;
				}
		    }
	    	else
	    	{
	    		//��Ȩʧ��
	    		Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_ACCOUNT_NOT_LOGIN, 4));
	    	}
	    	
	    	JSONObject jsonObj = (JSONObject) JSONObject.toJSON(Json);
	    	response.setCharacterEncoding("UTF-8");
	    	output = response.getWriter();
	    	output.write(jsonObj.toString());
	    	output.flush();
	    	
	    	System.out.println("AppGisJson:" + jsonObj.toString());
	    
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://��ѯ
				Sql = " select  t.id, t.cname, t.Longitude, t.Latitude, t.MapLev , t.MapAngle , t.Demo "
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
			setMapLev(pRs.getString(5));
			setMapAngle(pRs.getString(6));
			setDemo(pRs.getString(7));
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