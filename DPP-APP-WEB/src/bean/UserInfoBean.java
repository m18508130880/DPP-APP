package bean;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import json.UserInfoJsonBean;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
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
				String _Token = (CommUtil.BytesToHexString(new util.Md5().encrypt((CommUtil.SessionId()+"CHENGJI").getBytes()), 16)).toUpperCase();
				json.setToken(_Token);
				TokenList.put(_Token, Id);
				//用户信息
				msgBean = pRmi.RmiExec(0, this, 0, 25);
				UserInfoBean RealJson = (UserInfoBean)((ArrayList<?>)msgBean.getMsg()).get(0);
				
				json.setCName(RealJson.getCName());
				json.setDept_Id(RealJson.getDept_Id());
				json.setTel(RealJson.getTel());
				json.setFp_Role(RealJson.getFp_Role());
				json.setManage_Role(RealJson.getManage_Role());
				
				json.setRst(CommUtil.IntToStringLeftFillZero(MsgBean.STA_SUCCESS, 4));

				JSONObject jsonObj = (JSONObject) JSONObject.toJSON(json);
				output = response.getWriter();
				output.write(jsonObj.toString());
				output.flush();

				System.out.println("AppLoginJson:" + jsonObj.toString() + ";");
			}
		}
		catch (Exception Exp)
		{
			Exp.printStackTrace();
		}
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
					" where Id = '"+ Id +"' ";
			break;
		case 21://登录验证
			Sql = "{? = call APP_LOGIN('"+Id+"', '"+StrMd5+"')}";
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