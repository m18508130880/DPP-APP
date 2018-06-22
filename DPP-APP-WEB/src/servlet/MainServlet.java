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

////0ȫ����ѯ 2���� 3�޸� 4ɾ�� 10��19������ѯ
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
    		request.getSession().setAttribute("ErrMsg", CommUtil.StrToGB2312("RMI�����δ�������У��޷���½��"));
    		response.sendRedirect(getUrl(request) + "error.jsp");
    		return;
    	}
    	
        response.setContentType("text/html;charset=gb2312;");
        String strUrl = request.getRequestURI();
        String[] str = strUrl.split("/");
        strUrl = str[str.length - 1];
        System.out.println("********************" + strUrl + "[" + request.getRemoteAddr() + "]");
        
        /**************************************΢��С����************************************************/
        if (strUrl.equalsIgnoreCase("Login.do"))						         	 	//��¼
        	new UserInfoBean().Login(request, response, m_Rmi, strUrl, TokenList);		
        /*else if(strUrl.equals("AppLogout.do"))											//�ǳ��ӿ�    
        	new UserInfoBean().Logout(request, response, m_Rmi, strUrl, TokenList);    	*/
        else if (strUrl.equalsIgnoreCase("Manage_Role.do"))				     			//����Ȩ��
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        /*else if (strUrl.equalsIgnoreCase("FP_Role.do"))				     				//����Ȩ��
        	new UserRoleBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);*/
        
        /*****************************************�؈D************************************************/
        else if (strUrl.equalsIgnoreCase("ToPo_GJ.do"))				        			//GIS���-�ܾ�
        	new TopoGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("ToPo_GX.do"))			            			//GIS���-����
        	new TopoGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);

        /****************************************�������***********************************************/
        else if (strUrl.equalsIgnoreCase("Check_Task.do"))				        			//��ȡ�б�
        	new CheckTaskBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("Check_GJ.do"))				        			//��ȡ�ܾ�����
        	new CheckTaskGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("Check_GX.do"))			        			//��ȡ��������
        	new CheckTaskGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("uploadImg.do"))				        			//ͼƬ�ϴ�
        	new CheckTaskGJBean().uploadImg(request, response, m_Rmi, false, strUrl, TokenList);
        
        
        
        /***************************************�ܾ������б�********************************************/
        else if (strUrl.equalsIgnoreCase("GJ_Info.do"))				        			//�ܾ�
        	new DevGJBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        else if (strUrl.equalsIgnoreCase("GX_Info.do"))			            			//����
        	new DevGXBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        
        /***************************************�澯�б�**********************************************/
        else if (strUrl.equalsIgnoreCase("Alert_Info.do"))				        		//�澯��Ϣ
        	new AlertInfoBean().ExecCmd(request, response, m_Rmi, false, strUrl, TokenList);
        
        /***************************************ʵʱ���**********************************************/
        else if (strUrl.equalsIgnoreCase("Real_Water_Lev.do"))				        			//ʵʱ���-GIS
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