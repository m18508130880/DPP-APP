package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rmi.Rmi;
import util.CommUtil;
import bean.AlertInfoBean;
import bean.CheckTaskBean;
import bean.CheckTaskGJBean;
import bean.CheckTaskGXBean;
import bean.DataGJBean;
import bean.DevGJBean;
import bean.DevGXBean;
import bean.TopoGJBean;
import bean.TopoGXBean;
import bean.UserInfoBean;
import bean.UserRoleBean;

////0全部查询 2插入 3修改 4删除 10～19单个查询
public class MainServlet extends HttpServlet
{
	public final static long serialVersionUID = 1000;
	private Rmi m_Rmi = null;
	private String rmiUrl = null;
	private Connect connect = null;
	public ServletConfig Config;
	public HashMap<String , String> TokenList = new HashMap<String , String>();
	
	public final ServletConfig getServletConfig() 
	{
		return Config;
	}
	
	public void init(ServletConfig pConfig) throws ServletException
	{	
		Config = pConfig;
		connect = new Connect();
		connect.config = pConfig;
		connect.ReConnect();
	}		
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doPut(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    protected void doTrace(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }
    

    protected void processRequest(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException
    {
    	if(connect.Test()== false)
    	{
    		request.getSession().setAttribute("ErrMsg", CommUtil.StrToGB2312("RMI服务端未正常运行，无法登陆！"));
    		response.sendRedirect(getUrl(request) + "error.jsp");
    		return;
    	}
    	
        response.setContentType("text/html;charset=gb2312;");
        String strUrl = request.getRequestURI();
        String[] str = strUrl.split("/");
        strUrl = str[str.length - 1];
        System.out.println("********************" + strUrl + "[" + request.getRemoteAddr() + "]");
        
        /**************************************微信小程序************************************************/
        if (strUrl.equalsIgnoreCase("Login.do"))						         	 	//登录
        	new UserInfoBean().Login(request, response, m_Rmi, strUrl, TokenList);		
        /*else if(strUrl.equals("AppLogout.do"))											//登出接口    
        	new UserInfoBean().Logout(request, response, m_Rmi, strUrl, TokenList);    	*/
        else if (strUrl.equalsIgnoreCase("Manage_Role.do"))				     			//管理权限
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        /*else if (strUrl.equalsIgnoreCase("FP_Role.do"))				     				//功能权限
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);*/
        
        /*****************************************地圖************************************************/
        else if (strUrl.equalsIgnoreCase("ToPo_GJ.do"))				        			//GIS监控-管井
        	new TopoGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("ToPo_GX.do"))			            			//GIS监控-管线
        	new TopoGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);

        /****************************************检测任务***********************************************/
        else if (strUrl.equalsIgnoreCase("Check_Task.do"))				        			//获取列表
        	new CheckTaskBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("Check_GJ.do"))				        			//获取管井数据
        	new CheckTaskGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("Check_GX.do"))			        			//获取管线数据
        	new CheckTaskGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("uploadImg.do"))				        			//图片上传
        	new CheckTaskGJBean().uploadImg(request, response, m_Rmi, false, strUrl, TokenList);
        
        
        
        /***************************************管井管线列表********************************************/
        else if (strUrl.equalsIgnoreCase("GJ_Info.do"))				        			//管井
        	new DevGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("GX_Info.do"))			            			//管线
        	new DevGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        
        /***************************************告警列表**********************************************/
        else if (strUrl.equalsIgnoreCase("Alert_Info.do"))				        		//告警信息
        	new AlertInfoBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        
        /***************************************实时监测**********************************************/
        else if (strUrl.equalsIgnoreCase("Real_Water_Lev.do"))				        			//实时监测-GIS
        	new DataGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        
    }
    
    private class Connect extends Thread
	{
    	private ServletConfig config = null;
    	public boolean Test()
    	{
    		int i = 0;
        	boolean ok = false;
        	while(3 > i)
    		{        		
    	    	try
    			{   
    	    		if(i != 0) sleep(500);
    	    		ok = m_Rmi.Test();
    	    		i = 3;
    	    		ok = true;
    			}
    	    	catch(Exception e)
    			{    	    		
    	    		ReConnect();
    	    		i++;
    			}
    		}
    		return ok;
    	}
    	private void ReConnect()
    	{
    		try
    		{
    			rmiUrl = config.getInitParameter("rmiUrl");
    			Context context = new InitialContext();
    			m_Rmi = (Rmi) context.lookup(rmiUrl);
    		}
    		catch(Exception e)
    		{	
    			e.printStackTrace();
    		}
    	}
    }
	public final static String getUrl(HttpServletRequest request)
	{
		String url = "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath() + "/";
		return url;
	}
	
} 