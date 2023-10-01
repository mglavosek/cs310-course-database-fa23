package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class RegistrationDAO {
    
    private static final String QUERY_CREATE = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
    private static final String QUERY_WITHDRAW = "DELETE FROM registration WHERE studentid = ? AND termid = ?";
    private static final String QUERY_LIST = "SELECT studentid, termid, crn FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public boolean create(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_CREATE);
                
                ps.setInt(1,studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                int rowsAffected = ps.executeUpdate();
                
                return rowsAffected > 0; //Regestration is successful if at least 1 row was affected
                        
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result; //Registration was unsuccessful
        
    }

    public boolean delete(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareCall(QUERY_DELETE);
                
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                int rowsAffected = ps.executeUpdate();
                
                return rowsAffected > 0; //Deletion is successful if at least 1 row was affected
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result; //Deletion is unsuccessful
        
    }
    
    public boolean delete(int studentid, int termid) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_WITHDRAW);
                
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                int rowsAffected = ps.executeUpdate();
                
                return rowsAffected > 0; //Withdraw was successful
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result; //Withdraw failed
        
    }

    public String list(int studentid, int termid) {
        
        String result = "[]";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_LIST);
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    
                    result.concat(DAOUtility.getResultSetAsJson(rs));
                }
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}
