package json;

/**UserInfoBean 登录筛选\人员信息
 * @author Cui
 */
public class UserInfoJsonBean 
{
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
	
	private String Url;
	private String Rst;
	private String Token;
	
	public String getToken()
	{
		return Token;
	}

	public void setToken(String token)
	{
		Token = token;
	}

	public String getUrl()
	{
		return Url;
	}

	public void setUrl(String url)
	{
		Url = url;
	}

	public String getRst()
	{
		return Rst;
	}

	public void setRst(String rst)
	{
		Rst = rst;
	}

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