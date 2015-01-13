package com.vector.jdbcinsert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class JDBCHelper {
	
	Connection conn = null;
	
	String tableName = "imusers";
	
	String sql = "INSERT INTO  `macim`.`"+tableName+"` (`id` ,`uname` ,`realName` ,`pwd` ,"+
"`status` ,`role` ,`alphabet` ,`avatar` ,`nickName` ,`title` ,`departId` ,`sex` ,`position` ," +
"`telphone` ,`mail` ,`company` ,`companyTel` ,`companyWeb` ,`showAssFlag` ,`showCompFlag` ,`assReminder` ," +
"`jobNumber` ,`created` ,`updated` ,`login_num`)" +
"VALUES (" +
	"? ,  ?,  'vector',  'e10adc3949ba59abbe56e057f20f883e',  '0',  '0',  'v',  '/avatar/avatar_default.jpg',  'U',  'UU',  '1009',  '0',  'UU',  '13428283636',  'test@mail.com',  " +
	"'vv',  '',  '',  '1',  '1',  '2',  '12345', NULL , NULL ,  '0'" +
");";
	
	public boolean init(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/macim","root","root");
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		return true;
	}
	
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量插入数据测试
	 * @param startId 开始的ID
	 * @param count 共插入 多少条数据
	 * @return 用时，毫秒数
	 */
	public long insert(int startId, int count){
		long beginTime = 0 ,endTime = 0;
		try {
			conn.setAutoCommit(false);
			beginTime = System.currentTimeMillis();
			PreparedStatement pst = conn.prepareStatement(sql);
			   
			for(int i=startId;i<startId+count;i++){    
			    pst.setInt(1, i);
			    pst.setString(2, i+"");
			    pst.addBatch();
			    if(i%1000==0){
			    	pst.executeBatch();
			    	conn.commit();
			    	pst.clearBatch();
			    }
			}
			endTime = System.currentTimeMillis();
			System.out.println("pst+batch："+(endTime-beginTime)/1000+"秒");
			pst.close();
		} catch (SQLException e) {
			   // TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (endTime - beginTime);
	}
	/**
	 * 查询整张表，使用统计条数来测试
	 * @param field  要统计的字段
	 * @return 毫秒数
	 */
	public long selectAll(String field){
		long beginTime = 0 ,endTime = 0;
		
		String sql = "SELECT COUNT(*) FROM  "+tableName+" WHERE realName = 'vector'";
		
		try {
//			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			beginTime = System.currentTimeMillis();
			PreparedStatement pst = conn.prepareStatement(sql);
			
			ResultSet set = pst.executeQuery();
			
			set.next();
			
			int length = set.getInt(1);	
			
			endTime = System.currentTimeMillis();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (endTime - beginTime);
	}
	/**
	 * 按顺序查询数据,返回全部字段
	 * @param startId 开始的ID
	 * @param count  需要查询的个数
	 * @return
	 */
	public long selectAllField(int startId, int count){
		long beginTime = 0 ,endTime = 0;
		
		String sql = "SELECT * FROM "+tableName+" ORDER BY id DESC LIMIT "+startId+","+count+" ";
		
		try {
//			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			beginTime = System.currentTimeMillis();
			PreparedStatement pst = conn.prepareStatement(sql);
			
			ResultSet set = pst.executeQuery();
			
			endTime = System.currentTimeMillis();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (endTime - beginTime);
	}
	
	/**
	 * 按顺序查询数据,返回ID字段
	 * @param startId 开始的ID
	 * @param count  需要查询的个数
	 * @return
	 */
	public long selectId(int startId, int count){
		long beginTime = 0 ,endTime = 0;
		
		String sql = "SELECT id FROM "+tableName+" ORDER BY id DESC LIMIT "+startId+","+count+" ";
		
		try {
//			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			beginTime = System.currentTimeMillis();
			PreparedStatement pst = conn.prepareStatement(sql);
			
			ResultSet set = pst.executeQuery();
			
			endTime = System.currentTimeMillis();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (endTime - beginTime);
	}
	
	/**
	 * 在 startId 到 endId 之间随机查询 count条数据返回
	 * @param startId 
	 * @param endId  
	 * @param count  需要查询的个数
	 * @return
	 */
	public long selectRandom(int startId, int endId,int count){
		long beginTime = 0 ,endTime = 0;
		
		
		int ids[] = new int[count];
		
		//create count 
		for(int i=0;i<count;i++){
			Random random = new Random(System.currentTimeMillis()+i);
			ids[i] = random.nextInt(endId-startId) + startId; //start create [0,endid-startid] + startid
//			System.out.println(ids[i]);
		}
		
		//create SQL
		
		
		//(10000,100000,500000,1000000,5000000,10000000,2000000,30000000,40000000,50000000,60000000,67015297)
		String in = "(";
		
		for(int i=0;i<count-1;i++){
			in+=ids[i]+",";
		}
		
		in+=ids[count-1]+")";
		
		String sql = "SELECT * FROM "+tableName+" where id in " + in;
		
//		System.out.println(sql);
		
		try {
//			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			beginTime = System.currentTimeMillis();
			PreparedStatement pst = conn.prepareStatement(sql);
			
			ResultSet set = pst.executeQuery();
			
			endTime = System.currentTimeMillis();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (endTime - beginTime);
	}
	
	
	public static void main(String[] args) {
		JDBCHelper h = new JDBCHelper();
		h.init();
		if(args[0].equalsIgnoreCase("a")){
			//insert 10w
			System.out.println("insert10w");
			System.out.println("one = "+h.insert(120001, 100000));
			System.out.println("twe = "+h.insert(220001, 100000));
			System.out.println("three = "+h.insert(320001, 100000));
			
			//select all
			System.out.println("select all");
			System.out.println("one = "+h.selectAll("*"));
			System.out.println("twe = "+h.selectAll("*"));
			System.out.println("three = "+h.selectAll("*"));
			
			//select all field
			System.out.println("select all field");
			System.out.println("1-100 = "+h.selectAllField(1, 100));
			System.out.println("10000-100 = "+h.selectAllField(10000, 100));
			System.out.println("400000-100 = "+h.selectAllField(400000, 100));
			
			//select id field
			System.out.println("select id field");
			System.out.println("1-100 = "+h.selectId(1, 100));
			System.out.println("10000-100 = "+h.selectId(10000, 100));
			System.out.println("400000-100 = "+h.selectId(400000, 100));
			
			//select random
			System.out.println("select random");
			System.out.println("one-10000 = "+h.selectRandom(20001, 120000, 10000));
			System.out.println("twe-10000 = "+h.selectRandom(20001, 120000, 10000));
			System.out.println("three-10000 = "+h.selectRandom(20001, 120000, 10000));
		}else if(args[0].equalsIgnoreCase("b")){
			//insert 10w
			System.out.println("insert100w");
			System.out.println("one = "+h.insert(420001, 100_0000));
			//System.out.println("twe = "+h.insert(1420001, 100_0000));
			//System.out.println("three = "+h.insert(2420001, 100_0000));
			
			//select all
			System.out.println("select all");
			System.out.println("one = "+h.selectAll("*"));
			System.out.println("twe = "+h.selectAll("*"));
			System.out.println("three = "+h.selectAll("*"));
			
			//select all field
			System.out.println("select all field");
			System.out.println("1-100 = "+h.selectAllField(1, 100));
			System.out.println("100000-100 = "+h.selectAllField(10_0000, 100));
			System.out.println("1000000-100 = "+h.selectAllField(100_0000, 100));
			
			//select id field
			System.out.println("select id field");
			System.out.println("1-100 = "+h.selectId(1, 100));
			System.out.println("100000-100 = "+h.selectId(10_0000, 100));
			System.out.println("1000000-100 = "+h.selectId(100_0000, 100));
			
			//select random
			System.out.println("select random");
			System.out.println("one-10000 = "+h.selectRandom(420001, 142_0000, 10000));
			System.out.println("twe-10000 = "+h.selectRandom(420001, 1420000, 10000));
			System.out.println("three-10000 = "+h.selectRandom(420001, 1420000, 10000));
		}else if(args[0].equalsIgnoreCase("c")){
			//insert 10w
			System.out.println("insert10w");
			System.out.println("one = "+h.insert(120001, 220000));
			System.out.println("twe = "+h.insert(220001, 320000));
			System.out.println("three = "+h.insert(320001, 420000));
			
			//select all
			System.out.println("select all");
			System.out.println("one = "+h.selectAll("*"));
			System.out.println("twe = "+h.selectAll("*"));
			System.out.println("three = "+h.selectAll("*"));
			
			//select all field
			System.out.println("select all field");
			System.out.println("1-100 = "+h.selectAllField(1, 100));
			System.out.println("10000-100 = "+h.selectAllField(10000, 100));
			System.out.println("400000-100 = "+h.selectAllField(400000, 100));
			
			//select id field
			System.out.println("select id field");
			System.out.println("1-100 = "+h.selectId(1, 100));
			System.out.println("10000-100 = "+h.selectId(10000, 100));
			System.out.println("400000-100 = "+h.selectId(400000, 100));
			
			//select random
			System.out.println("select random");
			System.out.println("one-10000 = "+h.selectRandom(20001, 120000, 10000));
			System.out.println("twe-10000 = "+h.selectRandom(20001, 120000, 10000));
			System.out.println("three-10000 = "+h.selectRandom(20001, 120000, 10000));
		}
		h.close();
	}
}
