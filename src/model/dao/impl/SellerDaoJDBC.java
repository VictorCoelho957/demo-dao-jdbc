package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
//impleta interface

public class SellerDaoJDBC  implements SellerDao{
	
		private Connection conn; //força conexão com banco de dados
		public SellerDaoJDBC(Connection conn) {
			this.conn=conn;
		}
		
	

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st= conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+"VALUES " 
					+"(?, ?, ?, ?, ?)",  
					Statement.RETURN_GENERATED_KEYS);
		
		st.setString(1, obj.getName());
		st.setString(2, obj.getEmail());
		st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
		st.setDouble(4, obj.getBaseSalary());
		st.setInt(5, obj.getDepartment().getId());
		
		int rowsAffected= st.executeUpdate();
		
		if(rowsAffected >0) {
			ResultSet rs = st.getGeneratedKeys();
			//pegar o valor do id novo
			if (rs.next()) {
				int id = rs.getInt(1);
				obj.setId(id);
			}
			//fechar fora do finally pois ele nao existe fora do seçaõ
			DB.closeResultSet(rs);
		}
		//excessão caso ocorra algum vazio 
		else {
			throw new DbException ("Unexpected error! No rows affected ");
		}
	}
		catch(SQLException e) {
		throw new DbException(e.getMessage());
	}
		finally {
		DB.closeStatement(st);
	}
	
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st= null;
		ResultSet rs=null;
		try {
			st= conn.prepareStatement("SELECT seller.*,department.Name as DepName "
					+"FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id " 
					+"WHERE seller.Id = ?");
			
			st.setInt(1,id);
			rs=st.executeQuery();
			//testar se veio resultado, caso contrari, resulta em nulo:
			if (rs.next()) {
				//instancias de departamentos
				Department dep = instantiateDepartment(rs);
				//associação de departaemntos com vendodores 
				Seller obj= instantionSeller(rs, dep);
				return obj;
				}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instantionSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj=new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);	
		return obj;
	}



	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep =new Department ();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}



	@Override
	//todos vendedores ordenados por nomes
	public List<Seller> findAll() {
		PreparedStatement st= null;
		ResultSet rs=null;
		try {
			st= conn.prepareStatement("SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id "
					+"ORDER BY Name");
			
			rs=st.executeQuery();
			//testar se veio resultado, caso contrari, resulta em nulo:
			List<Seller> list= new ArrayList<>();
			Map<Integer, Department> map= new HashMap<>();
			while (rs.next()) {
				//instancias de departamentos, verifica se o departamneto ja existe e, posteriormente o adciona como nulo
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep==null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//associação de departaemntos com vendodores 
				Seller obj= instantionSeller(rs, dep);
				list.add(obj);
				}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}


    //metodo de pesquisa por departamento
	@Override
	public List<Seller> finByDepartment(Department department) {
		PreparedStatement st= null;
		ResultSet rs=null;
		try {
			st= conn.prepareStatement("SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name");
			
			st.setInt(1,department.getId());
			rs=st.executeQuery();
			//testar se veio resultado, caso contrari, resulta em nulo:
			List<Seller> list= new ArrayList<>();
			Map<Integer, Department> map= new HashMap<>();
			while (rs.next()) {
				//instancias de departamentos, verifica se o departamneto ja existe e, posteriormente o adciona como nulo
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep==null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				//associação de departaemntos com vendodores 
				Seller obj= instantionSeller(rs, dep);
				list.add(obj);
				}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	
}
