import java.sql.*;
import java.util.*;
import java.sql.Statement;
public class Libary 
{
	//private static final int RESERVED_BY = 0;
	//Panduwangw@hotmail.com
	static Scanner in = new Scanner(System.in);
	static String srchSubQuery = "";
	static String updateQuery = "";
	static String startquery ="";
	static String sqlUpdate ;
	static String startCount = "";
	static String STUDENTID , NAME , BRANCH ,CONTACT_DETAIL ,BOOK_BORROWED ;
	static Scanner scn = new Scanner(System.in);
	static String updateRecordInQuery;
	static String unreservedABook;
	static String insertData;
	
	public static void main(String[] args) 
	
	{
	//Display option to user
	System.out.println("\t  Welocme to libary management system");
	System.out.println("1.Search a book in libary");
	System.out.println("2.Reserve a book");
	System.out.println("3.Borrow a book");
	System.out.println("4.Unserve a book");
	System.out.println("5. Enter new record for book");
	System.out.println("Select a number from option as given above");
	int num = in.nextInt();
	System.out.println("Enter subject to search");
	String bookname = in.next();	
	  
	try 
	{		
			String startCount = "";
			String startquery = "";
			boolean update = false;
			boolean search = false;
			boolean unrerved = false;
			boolean insert = false;

			//connection to database
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:GLOBALDATABS","system","9864290117");
			//Connection conn = Libary.getConnection();
			Statement st = con.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
			
			String RSUnreserve = "";
			switch(num)
			{
			case 1:
				//search book from database
				startquery = Search(bookname);
				System.out.println(startquery);
				startCount = CountRows(bookname);
				search = true;
				break;
				
			case 2:
				//Insert student detail for reserving a book
				updateRecordInQuery = updateRecord(bookname);
				update = true;
				break;
			case 3:
				//Borrow a book
				insertData = insert();
				insert = true;
				break;
			case 4:
				//unreserve a book
				String RSUnsserve = "";
				System.out.println("Student id no : ");
				int STUDENTID = scn.nextInt();
				//System.out.println("In case 4 :::"+STUDENTID);
				//String sqlUnreserve = "SELECT * FROM BOOKS WHERE RESERVED_BY = ? ";
				String sqlUnreserve = "SELECT * FROM BOOKS";
				System.out.println(sqlUnreserve);
				//st = con.prepareStatement(sqlUnreserve);
				PreparedStatement ps = con.prepareStatement(sqlUnreserve);
				//ps.setString(1,STUDENTID);
				//int RSUnreserve = st.executeUpdate(sqlUnreserve);
				/*ResultSet rs = ps.executeQuery();
				System.out.println("Resultset "+rs.next());
				while(rs.next()){
					int check = rs.getInt("RESERVED_BY");
					System.out.println("Check Value "+check);
					
						if(check == 503){
						
						System.out.println("Book is reserved");
					}
					else
						System.out.println("Book is not reserved");
					
				}*/
				
				System.out.println(RSUnreserve);
				
				if(RSUnsserve != null)
				{
				unreservedABook = unreserveABook(bookname);//CALLED A FUNCTON TO UPDATE VALUE AS NULL
				System.out.println("SUCCESSFULLY UNRESERVED THE BOOK ");
				unrerved = true;} 
				else
					System.out.println("you have not reserved the book yet");
			case 5:
				
			}
			System.out.println("out of switch statement");
			//used in case 3
			if(insert == true){
				st = con.prepareStatement(insertData);
				int RSInsert  = st.executeUpdate(insertData);
				
				System.out.println("insert into database successfully!!!");
			}
			
			//used in case 2
			//to update data in table books
			else if(update == true){
			st = con.prepareStatement(updateRecordInQuery);
			int RSUpdate = st.executeUpdate(updateRecordInQuery);
			System.out.println("SUCCESS!!!");
			}
			else if (unrerved == true)
			{
				//3RD case unserve a book 
				st = con.prepareStatement(unreservedABook);
				int RSUnserved = st.executeUpdate(unreservedABook);
				System.out.println("SUCCESS!!!");
			}
			
			else if(search == true){
			//Count Result Set
			//Here run the Count query
			ResultSet countRS = st.executeQuery(startCount);			
			countRS.last();
			//System.out.println("the size"+countRS.getInt(1));
			ResultSet rs = null;
			if(countRS.getInt(1) > 0){
			countRS.close();				
			//Main result set to get the data
			rs = st.executeQuery(startquery );
			//ResultSetMetaData rsmd = rs.getMetaData();
			//int columnsNumber = rsmd.getColumnCount();
			
			/*another method to display result
				  
				 * while (rs.next()) {
				    for (int i = 1; i <= columnsNumber; i++) {
				        if (i > 1) System.out.print(",  ");
				        String columnValue = rs.getString(i);
				        System.out.print(columnValue + " " + rsmd.getColumnName(i));
				    }
				    System.out.println("");
				}*/
				/*System.out.println("last row"+rs.last());
				System.out.println("the row size"+rs.getRow());
				System.out.println("the size"+rs.getFetchSize());*/
				
				
		       	System.out.println("Subject found");
				//Boolean isBookAvailable;
		       	
				while(rs.next()){					
					System.out.println(rs.getString(1)+" "+rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)+ " " 
							+ rs.getString(5)+ " " + rs.getString(6)+ " " + rs.getString(7)+ " " + rs.getString(8));
					
					System.out.println("We have found the book for you. Here are the details : ");
					System.out.println("Author  : "+rs.getString("Author"));
					
					//Verify if the Book is Available
					if(rs.getString("BORROWED_BY") == null)
					{
						
						//Verify if the book is already reserved
						if(rs.getString("RESERVED_BY") == null)
						{
							System.out.println("Would you like to reserve the book");
							System.out.println("Type yes or no");
							String result = in.next();
							System.out.println(result);
								
							if( result.equalsIgnoreCase("yes") )
								{
									//called reserved function
								
								}
								else 
								{
									System.out.println("Thank you !!!");
								}
						}
						else
						{
							System.out.println("The book is not available");
						}
					}
					else if(rs.getString("BORROWED_BY") != null)
					{
						if(rs.getString("RESERVED_BY") == null)
						{
							System.out.println("Would you like to reserve the book");
								System.out.println("Type yes or no");
								String result = in.next();
								//System.out.println(result);
								if(result.equalsIgnoreCase("yes"))
								{
									System.out.println("Enter your detail");
									updateRecordInQuery = updateRecord(bookname);
									update = true;
									
								}
								else 
								{
									System.out.println("Thank you !!!");
								}
						}
						else
						{
							System.out.println("The book is not available");
						}
						
					}
					else 
					{
						System.out.println("The book is available");
					}			
					
				}
			}
			else{
				System.out.println("Please try searching with a different keyword");				
			}
			
			rs.close();	
			con.close();
			}			
	} 
	catch (Exception e) 
	{
		System.out.println(e);
		
	}
}

	private static Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String Search(String bookName)
	{	
		
		String srchSubQuery = "";	
		srchSubQuery = "SELECT * FROM BOOKS Where NAME ='" + bookName.toLowerCase() + "'";
		System.out.println("I am in 1 function");
		return srchSubQuery;
	}

	public static String CountRows(String bookName)
	{
		String countSql = "";
		countSql = "SELECT count(*) FROM BOOKS Where NAME ='" + bookName.toLowerCase() + "'";
		System.out.println("I am in 2 function");
		return countSql;
	}

	public static String insert()
	{
		//String NAME,String BRANCH
		System.out.print("Enter student code: ");
		int STUDENTID = scn.nextInt();
		System.out.print("Enter student name: ");
        String NAME = scn.next();
        System.out.print("student branch: ");
        BRANCH = scn.next();
        System.out.println("Student contact number");
        int CONTACT_DETAIL = scn.nextInt();
        System.out.println("Book id");
        String BOOK_BORROWED = scn.next();
        String sqlInsert = "INSERT INTO STUDENTS " + "VALUES ('" + STUDENTID +"','"+NAME+"','"+BRANCH+"','"+CONTACT_DETAIL+"','"+BOOK_BORROWED+"')";
        System.out.println("String query is :->"+sqlInsert);
        return sqlInsert;
 	    
	}

	public static String updateRecord(String bookname){ 
		
		//update the record in both table 
		System.out.println("Enter student id no : ");
		int studentid = scn.nextInt();
		String sqlUpdate = "UPDATE BOOKS SET BORROWED_BY = " + studentid + " WHERE NAME = \'"+ bookname+ "\'";
		System.out.println("Sql value is" + sqlUpdate);
		return sqlUpdate ;				
	}
	
	public static String unreserveABook(String bookname)
	{	
		
		String sqlDelete = "UPDATE TABLE SET RESERVED_BY = null WHERE  RESERVED_BY ='"+ STUDENTID + "'";
		return sqlDelete;
		
	}

	
}