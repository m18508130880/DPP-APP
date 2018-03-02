package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import json.UserRoleJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.*;
/** ����Ȩ�޺͹���Ȩ��
 * @author cui
 *  ��� role����
 */
public class UserRoleBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_USER_ROLE;
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public UserRoleBean()
	{
		super.className = "UserRoleBean";
	}
	
	//��ѯ
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone, String Url, HashMap<String , String> TokenList) throws ServletException, IOException
	{
		PrintWriter output = null;
	    try
	    {
	    	getHtmlData(request);
	    	getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			
	    	UserRoleJsonBean Json = new UserRoleJsonBean();
	    	Json.setUrl(Url);
	    	Json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
	    	if(TokenList.containsKey(Token))
	    	{
	    		//msgBean = pRmi.RmiExec(1, roleBeanManage, 0, 25);����Ȩ��
				msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
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
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	output.close();
	    }
		
		
	}
	
	//����
	public void RoleOP(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		PrintWriter outprint = response.getWriter();
		String Resp = "9999";
		if(13 == currStatus.getCmd())
		{
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
			if(RoleList.trim().length() > 0)
			{
				String[] List = RoleList.split("@");
				for(int i=0; i<List.length && List[i].length()>0; i++)
				{
					String[] subList = List[i].split(";");
					Id = subList[0].trim();
					CName = subList[1].trim();
					Point = subList[2].trim();
					if(null == CName){CName = "��";}
					if(null == Point){Point = "";}
					msgBean = pRmi.RmiExec(14, this, 0, 25);
					if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
					{
						Resp = "0000";
					}
				}
			}
		}
		else
		{
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
			switch(currStatus.getCmd())
			{
				case 12://����Ȩ��ɾ��
				case 11://����Ȩ���޸�
				case 10://����Ȩ�����
					if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
						Resp = "0000";
					break;
			}
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		outprint.write(Resp);
	}
	
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://����Ȩ��
				Sql = " select t.id, t.cname, t.point, t.zoomx, t.zoomy, t.zoomlev from role t where length(t.id) = 3 order by t.id";
				break;
			case 1://����Ȩ��
				Sql = " select t.id, t.cname, t.point, t.zoomx, t.zoomy, t.zoomlev from role t where substr(t.id,1,2) = '50' and (length(t.id) = 4 or length(t.id) = 6 or length(t.id) = 8) order by t.id";
				break;
			case 2://���ܵ�
				Sql = " select t.id, t.cname, '' as point from fp_info t where t.status = '0' order by t.id";
				break;
			case 10://����Ȩ�����
				Sql = " insert into role(id, cname)values('"+ Id +"', '"+ CName +"')";
				break;
			case 11://����Ȩ���޸�
				Sql = " update role t set t.cname = '"+ CName +"', t.point = '"+ Point +"' where t.id = '"+ Id +"'";
				break;
			case 12://����Ȩ��ɾ��
				Sql = " delete from role where id = '"+ Id +"'";
				break;
			case 13://����Ȩ��ɾ��
				Sql = " delete from role where substr(id,1,2) = '"+ Id +"'";
				break;
			case 14://����Ȩ�����
				Sql = " insert into role(id, cname, point)values('"+ Id +"', '"+ CName +"', '"+ Point +"')";
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
			setPoint(pRs.getString(3));
			setZoomX(pRs.getString(4));
			setZoomY(pRs.getString(5));
			setZoomLev(pRs.getString(6));
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
			setPoint(CommUtil.StrToGB2312(request.getParameter("Point")));
			setZoomX(CommUtil.StrToGB2312(request.getParameter("ZoomX")));
			setZoomY(CommUtil.StrToGB2312(request.getParameter("ZoomY")));
			setZoomLev(CommUtil.StrToGB2312(request.getParameter("ZoomLev")));
			setRoleList(CommUtil.StrToGB2312(request.getParameter("RoleList")));
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
	private String Point;
	private String ZoomX;
	private String ZoomY;
	private String ZoomLev;
	
	private String RoleList;
	private String Sid;
	
	private String Token;

	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

	public String getZoomX()
	{
		return ZoomX;
	}

	public void setZoomX(String zoomX)
	{
		ZoomX = zoomX;
	}

	public String getZoomY()
	{
		return ZoomY;
	}

	public void setZoomY(String zoomY)
	{
		ZoomY = zoomY;
	}

	public String getZoomLev()
	{
		return ZoomLev;
	}

	public void setZoomLev(String zoomLev)
	{
		ZoomLev = zoomLev;
	}

	public String getRoleList() {
		return RoleList;
	}
	public void setRoleList(String roleList) {
		RoleList = roleList;
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
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}
