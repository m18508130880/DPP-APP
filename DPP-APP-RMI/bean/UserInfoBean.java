package bean;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.UserInfoJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;
import util.MsgBean;

import com.alibaba.fastjson.JSONObject;

/**UserInfoBean 登录筛选\人员信息
 * @author Cui
 */
public class UserInfoBean extends RmiBean 
{
	/**
	 * 赋值serialVersionUID = 13;
	 */
	public final static long serialVersionUID =RmiBean.RMI_USER_INFO;
	
	/** 获取serialVersionUID的值
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	/**无参构造器
	 * UserInfoBean
	 */
	public UserInfoBean()
	{
		super.className = "UserInfoBean";
	}
	
	/** 所有用户登录处
	 * @param request
	 * @param response
	 * @param pRmi
	 */
	public void Login(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, String Url, HashMap<String , String> TokenList)
	{
		PrintWriter output = null;
		try
		{
			getHtmlData(request);
			
			UserInfoJsonBean json = new UserInfoJsonBean();
			json.setUrl(Url);
			json.setId(Id);
			json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_FAILED, 4));
			
			msgBean = pRmi.RmiExec(21, this, 0, 25);
			//登入成功
			if(msgBean.getStatus() == MsgBean.STA_SUCCESS)
			{
				//身份令牌
				String _Token = (CommUtil.BytesToHexString(new util.Md5().encrypt((CommUtil.SessionId()+"BESTAPP").getBytes()), 16)).toUpperCase();
				json.setToken(_Token);
				TokenList.put(_Token, Id);
				
				JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
		    	output = response.getWriter();
		    	output.write(jsonObj.toString());
		    	output.flush();
		    	
		    	System.out.println("AppLoginJson{" + jsonObj.toString() + "}");
			}
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
	}
	

	/** 密码修改
	 * @param request
	 * @param response
	 * @param pRmi
	 */
	public void PwdEdit(HttpServletRequest request, HttpServletResponse response, Rmi pRmi)
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request,false);
			
			PrintWriter outprint = response.getWriter();
			String Resp = "9999";
			
			int pCmd = currStatus.getCmd();
			switch(currStatus.getCmd())
			{
				case 12://user 个人信息
					pCmd = currStatus.getCmd();
					break;
				case 22://user 密码修改
					pCmd = currStatus.getCmd();
					break;
				case 23://admin 密码修改
					pCmd = 22;
					break;
				case 24://admin 密码重置
					pCmd = 22;
					break;
			}
			
			msgBean = pRmi.RmiExec(pCmd, this, 0, 25);
			switch(msgBean.getStatus())
			{
				case 0://成功
					Resp = "0000";
					break;
				case 1001://用户不存在或密码错误
					Resp = "1001";
					break;
				default://失败
					Resp = "9999";
					break;
			}
			
			switch(currStatus.getCmd())
			{
				case 12://user 个人信息
				case 22://user 密码修改
					msgBean = pRmi.RmiExec(2, this, 0, 25);
					request.getSession().setAttribute("UserInfo_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					break;
				case 23://admin 密码修改
					msgBean = pRmi.RmiExec(2, this, 0, 25);
					request.getSession().setAttribute("Admin_" + Sid, (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0));
					break;
				case 24://admin 密码重置
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
			outprint.write(Resp);
		} 
		catch (Exception Ex)
		{
			Ex.printStackTrace();
		}
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @param pRmi
	 * 		DaoImpl
	 * @param pFromZone
	 * 		是一个boolean值, true表示: 从其他页面 到 当前页面 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		
		msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0, 25);
		switch(currStatus.getCmd())
		{
			case 10://添加
			case 11://修改
				currStatus.setResult(MsgBean.GetResult(msgBean.getStatus()));
				msgBean = pRmi.RmiExec(1, this, 0, 25);
				request.getSession().setAttribute("User_Info_" + Sid, ((Object)msgBean.getMsg()));
			    currStatus.setJsp("User_Info.jsp?Sid=" + Sid);
			    
		    case 1://公司人员
		    	request.getSession().setAttribute("User_Info_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("User_Info.jsp?Sid=" + Sid);
		    	break;
		  
		}
		
		//功能权限
    	UserRoleBean roleBean = new UserRoleBean();
    	msgBean = pRmi.RmiExec(0, roleBean, 0, 25);
    	request.getSession().setAttribute("FP_Role_" + Sid, ((Object)msgBean.getMsg()));
    	
    	//管理权限
    	msgBean = pRmi.RmiExec(1, roleBean, 0, 25);
    	request.getSession().setAttribute("Manage_Role_" + Sid, ((Object)msgBean.getMsg()));
    	
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	}
	
	/** 根据传入的int值获取相应的数据库查询语句
	 * @see rmi.RmiBean#getSql(int)
	 *  
	 */
	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://登陆信息
				Sql = " select Id, Pwd, CName, Dept_Id, Birthday, Tel, Fp_Role, Manage_Role, Project_Id, Project_Name, Status " +
					  " from view_user_info " +
					  " where upper(Id) = '"+ StrMd5.substring(0,20).trim() +"' ";
				break;
			case 1://公司人员
				Sql = " select Id, Pwd, CName, Dept_Id, Birthday, Tel, Fp_Role, Manage_Role, Project_Id, Project_Name, Status " +
				  	  " from view_user_info " +
				  	  " where Id <> 'system' and Id <> 'admin' order by Id asc";
				break;
			case 2://帐号检测
				Sql = " select Id, Pwd, CName, Dept_Id, Birthday, Tel, Fp_Role, Manage_Role, Project_Id, Project_Name, Status " +
					  " from view_user_info " +
					  " where upper(Id) = upper('"+ Id +"') ";
				break;
			case 10://员工添加
				Sql = " insert into user_info(Id, CName, Dept_Id, Birthday, Tel, Fp_Role, Manage_Role, Project_Id , Status )" +
					  " values('"+Id+"', '"+CName+"',  '"+Dept_Id+"','"+Birthday+"', '"+Tel+"', '"+Fp_Role+"', '"+Manage_Role+"', '"+Project_Id+"', '"+Status+"')";
				break;
			case 11://员工修改
				Sql = " update user_info set cname = '"+CName+"', birthday = '"+Birthday+"',  tel = '"+Tel+"', status = '"+Status+"', " +
					  " dept_id = '"+Dept_Id+"', manage_role = '"+Manage_Role+"',  Project_Id = '"+Project_Id+"', fp_role = '"+Fp_Role+"' " +
					  " where id = '"+Id+"' ";
				break;
			case 12://个人修改
				Sql = " update user_info set cname = '"+CName+"',  birthday = '"+Birthday+"',  tel = '"+Tel+"' " +
				  	  " where id = '"+Id+"' ";
				break;
			case 21://登录验证
				Sql = "{? = call RMI_LOGIN('"+StrMd5+"', '"+currStatus.getCheckCode()+"')}";
				break;
			case 22://密码修改
				Sql = "{? = call RMI_PWD_EDIT('"+getId()+"','" + getPwd() +"', '"+getNewPwd()+"')}";
				break;
		}
		return Sql;
	}
	
	/** 获取从数据库结果集中查询到的数据封装到UserInfoBean中
	 * @see   rmi.RmiBean#getData(java.sql.ResultSet)
	 * @param ResultSet
	 * @return 返回一个boolean值   表示注入成功与否
	 */
	public boolean getData(ResultSet pRs)
	{
		boolean IsOK = true;
		try
		{
			setId(pRs.getString(1));
			setPwd(pRs.getString(2));
			setCName(pRs.getString(3));
			setDept_Id(pRs.getString(4));
			setBirthday(pRs.getString(5));
			setTel(pRs.getString(6));
			setFp_Role(pRs.getString(7));
			setManage_Role(pRs.getString(8));		
			setProject_Id(pRs.getString(9));
			setProject_Name(pRs.getString(10));
			setStatus(pRs.getString(11));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/**获取request中UserInfoBean 数据封装到bean中
	 * @param request
	 * @return 返回一个boolean值   表示注入成功与否
	 */
	public boolean getHtmlData(HttpServletRequest request) 
	{		
		boolean IsOK = true;
		try 
		{		
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setPwd(CommUtil.StrToGB2312(request.getParameter("Pwd")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setDept_Id(CommUtil.StrToGB2312(request.getParameter("Dept_Id")));
			setBirthday(CommUtil.StrToGB2312(request.getParameter("Birthday")));
			setTel(CommUtil.StrToGB2312(request.getParameter("Tel")));
			setFp_Role(CommUtil.StrToGB2312(request.getParameter("Fp_Role")));
			setManage_Role(CommUtil.StrToGB2312(request.getParameter("Manage_Role")));
			setProject_Id(CommUtil.StrToGB2312(request.getParameter("Project_Id")));
			setProject_Name(CommUtil.StrToGB2312(request.getParameter("Project_Name")));
			setStatus(CommUtil.StrToGB2312(request.getParameter("Status")));
			
			setStrMd5(CommUtil.StrToGB2312(request.getParameter("StrMd5")));
			setNewPwd(CommUtil.StrToGB2312(request.getParameter("NewPwd")));
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
		} 
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String Id;
	private String Pwd;
	private String CName;
	private String Dept_Id;
	private String Birthday;
	private String Tel;
	private String Fp_Role;
	private String Manage_Role;
	private String Project_Id;
	private String Project_Name;

	private String Status;
	
	private String StrMd5;
	private String NewPwd;
	private String Func_Corp_Id;
	private String Sid;
	
	
	
	public String getProject_Id() {
		return Project_Id;
	}

	public void setProject_Id(String project_Id) {
		Project_Id = project_Id;
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

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}


	public String getTel() {
		return Tel;
	}

	public void setTel(String tel) {
		Tel = tel;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getPwd() {
		return Pwd;
	}

	public void setPwd(String pwd) {
		Pwd = pwd;
	}

	public String getDept_Id() {
		return Dept_Id;
	}

	public void setDept_Id(String deptId) {
		Dept_Id = deptId;
	}

	public String getManage_Role() {
		return Manage_Role;
	}

	public void setManage_Role(String manageRole) {
		Manage_Role = manageRole;
	}
	
	public String getFp_Role() {
		return Fp_Role;
	}

	public void setFp_Role(String fpRole) {
		Fp_Role = fpRole;
	}
	
	public String getStrMd5() {
		return StrMd5;
	}

	public void setStrMd5(String strMd5) {
		StrMd5 = strMd5;
	}

	public String getNewPwd() {
		return NewPwd;
	}

	public void setNewPwd(String newPwd) {
		NewPwd = newPwd;
	}

	public String getFunc_Corp_Id() {
		return Func_Corp_Id;
	}

	public void setFunc_Corp_Id(String funcCorpId) {
		Func_Corp_Id = funcCorpId;
	}
	public String getProject_Name() {
		return Project_Name;
	}
	
	public void setProject_Name(String project_Name) {
		Project_Name = project_Name;
	}

	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}
}